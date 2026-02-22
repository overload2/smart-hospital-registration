package com.hospital.registration.service.impl;

import com.hospital.registration.dto.AiChatDTO;
import com.hospital.registration.entity.AiConsultationMessage;
import com.hospital.registration.entity.AiConsultationSession;
import com.hospital.registration.entity.Department;
import com.hospital.registration.mapper.AiConsultationMessageMapper;
import com.hospital.registration.mapper.AiConsultationSessionMapper;
import com.hospital.registration.mapper.DepartmentMapper;
import com.hospital.registration.mapper.DoctorMapper;
import com.hospital.registration.service.AiConsultationService;
import com.hospital.registration.service.DeepSeekService;
import com.hospital.registration.vo.AiChatVO;
import com.hospital.registration.vo.AiMessageVO;
import com.hospital.registration.vo.AiSessionVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @title AI问诊服务实现类
 * @author developer
 * @date 2026-02-18
 * @version 1.0
 * @description AI问诊业务实现
 */
@Slf4j
@Service
public class AiConsultationServiceImpl implements AiConsultationService {

    @Autowired
    private DeepSeekService deepSeekService;

    @Autowired
    private AiConsultationSessionMapper sessionMapper;

    @Autowired
    private AiConsultationMessageMapper messageMapper;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private DoctorMapper doctorMapper;

    // 智能导诊系统提示词
    private static final String GUIDE_SYSTEM_PROMPT = """
              你是一个专业的医院智能导诊助手。你的任务是根据患者描述的症状，推荐合适的科室。

              医院现有科室： %s

              请根据患者的症状描述：
              1. 简要分析可能的健康问题
              2. 推荐最合适的科室（必须从上述科室中选择）
              3. 给出就诊建议

              回复格式要求：
              - 在回复末尾用【推荐科室：XXX】的格式标注推荐的科室名称
              - 语气要专业、温和、有同理心
              - 提醒患者这只是初步建议，具体诊断需要医生面诊
              """;

    // 健康咨询系统提示词
    private static final String CONSULT_SYSTEM_PROMPT = """
              你是一个专业的健康咨询助手。你可以回答用户关于健康、疾病预防、生活方式等方面的问题。

              注意事项：
              1. 提供专业、准确的健康知识
              2. 不进行具体的疾病诊断
              3. 对于严重症状，建议用户及时就医
              4. 语气要专业、温和、有同理心
              5. 回答要简洁明了，避免过于冗长
              """;

    /**
     * AI对话
     *
     * @param userId 用户ID
     * @param dto 对话请求
     * @return 对话响应
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AiChatVO chat(Long userId, AiChatDTO dto) {
        log.info("AI对话 - 用户ID: {}, 会话ID: {}, 消息: {}", userId, dto.getSessionId(), dto.getMessage());

        AiConsultationSession session;

        // 获取或创建会话
        if (dto.getSessionId() != null) {
            session = sessionMapper.selectById(dto.getSessionId());
            if (session == null || !session.getUserId().equals(userId)) {
                throw new RuntimeException("会话不存在");
            }
            if (session.getStatus() == 0) {
                throw new RuntimeException("会话已结束");
            }
        } else {
            // 创建新会话
            if (dto.getSessionType() == null) {
                throw new RuntimeException("新会话必须指定会话类型");
            }
            session = new AiConsultationSession();
            session.setUserId(userId);
            session.setSessionType(dto.getSessionType());
            session.setStatus(1);
            sessionMapper.insert(session);
        }

        // 构建消息上下文
        List<Map<String, String>> messages = buildMessageContext(session, dto.getMessage());

        // 调用DeepSeek
        String aiReply = deepSeekService.chat(messages);

        // 保存用户消息
        AiConsultationMessage userMessage = new AiConsultationMessage();
        userMessage.setSessionId(session.getId());
        userMessage.setRole("user");
        userMessage.setContent(dto.getMessage());
        messageMapper.insert(userMessage);

        // 解析AI回复，提取推荐科室
        Long departmentId = null;
        String departmentName = null;
        if ("GUIDE".equals(session.getSessionType())) {
            departmentName = extractDepartmentName(aiReply);
            if (departmentName != null) {
                Department department = departmentMapper.selectByName(departmentName);
                if (department != null) {
                    departmentId = department.getId();
                }
            }
        }

        // 保存AI回复
        AiConsultationMessage aiMessage = new AiConsultationMessage();
        aiMessage.setSessionId(session.getId());
        aiMessage.setRole("assistant");
        aiMessage.setContent(aiReply);
        aiMessage.setRecommendedDepartmentId(departmentId);
        messageMapper.insert(aiMessage);

        // 更新会话标题（首次对话时）
        if (session.getTitle() == null) {
            String title = dto.getMessage().length() > 20 ? dto.getMessage().substring(0, 20) + "..." : dto.getMessage();
            session.setTitle(title);
            sessionMapper.updateById(session);
        }

        // 构建响应
        AiChatVO vo = new AiChatVO();
        vo.setSessionId(session.getId());
        vo.setReply(aiReply);
        vo.setRecommendedDepartmentId(departmentId);
        vo.setRecommendedDepartmentName(departmentName);

        return vo;
    }

    /**
     * 获取用户的会话列表
     *
     * @param userId 用户ID
     * @return 会话列表
     */
    @Override
    public List<AiSessionVO> getUserSessions(Long userId) {
        List<AiConsultationSession> sessions = sessionMapper.selectByUserId(userId);
        List<AiSessionVO> voList = new ArrayList<>();
        for (AiConsultationSession session : sessions) {
            AiSessionVO vo = new AiSessionVO();
            BeanUtils.copyProperties(session, vo);
            vo.setSessionTypeName("GUIDE".equals(session.getSessionType()) ? "智能导诊" : "健康咨询");
            voList.add(vo);
        }
        return voList;
    }

    /**
     * 获取会话详情（包含消息）
     *
     * @param sessionId 会话ID
     * @param userId 用户ID
     * @return 会话详情
     */
    @Override
    public AiSessionVO getSessionDetail(Long sessionId, Long userId) {
        AiConsultationSession session = sessionMapper.selectById(sessionId);
        if (session == null || !session.getUserId().equals(userId)) {
            throw new RuntimeException("会话不存在");
        }

        AiSessionVO vo = new AiSessionVO();
        BeanUtils.copyProperties(session, vo);
        vo.setSessionTypeName("GUIDE".equals(session.getSessionType()) ? "智能导诊" : "健康咨询");

        // 查询消息列表
        List<AiMessageVO> messages = messageMapper.selectBySessionId(sessionId);
        vo.setMessages(messages);

        return vo;
    }

    /**
     * 结束会话
     *
     * @param sessionId 会话ID
     * @param userId 用户ID
     */
    @Override
    public void endSession(Long sessionId, Long userId) {
        AiConsultationSession session = sessionMapper.selectById(sessionId);
        if (session == null || !session.getUserId().equals(userId)) {
            throw new RuntimeException("会话不存在");
        }
        session.setStatus(0);
        sessionMapper.updateById(session);
    }

    /**
     * 构建消息上下文
     */
    private List<Map<String, String>> buildMessageContext(AiConsultationSession session, String userMessage) {
        List<Map<String, String>> messages = new ArrayList<>();

        // 添加系统提示词
        Map<String, String> systemMessage = new HashMap<>();
        systemMessage.put("role", "system");

        // 根据会话类型选择提示词
        String systemPrompt;
        if ("GUIDE".equals(session.getSessionType())) {
            // 智能导诊：动态获取科室列表
            String departmentNames = getActiveDepartmentNames();
            systemPrompt = String.format(GUIDE_SYSTEM_PROMPT, departmentNames);
        } else {
            systemPrompt = CONSULT_SYSTEM_PROMPT;
        }
        systemMessage.put("content", systemPrompt);
        messages.add(systemMessage);

        // 添加历史消息（最近10条）
        List<AiConsultationMessage> historyMessages = messageMapper.selectMessagesBySessionId(session.getId());
        int start = Math.max(0, historyMessages.size() - 10);
        for (int i = start; i < historyMessages.size(); i++) {
            AiConsultationMessage msg = historyMessages.get(i);
            Map<String, String> historyMsg = new HashMap<>();
            historyMsg.put("role", msg.getRole());
            historyMsg.put("content", msg.getContent());
            messages.add(historyMsg);
        }

        // 添加当前用户消息
        Map<String, String> currentMessage = new HashMap<>();
        currentMessage.put("role", "user");
        currentMessage.put("content", userMessage);
        messages.add(currentMessage);

        return messages;
    }

    /**
     * 从AI回复中提取推荐科室名称
     */
    private String extractDepartmentName(String reply) {
        Pattern pattern = Pattern.compile("【推荐科室[：:]\\s*(.+?)】");
        Matcher matcher = pattern.matcher(reply);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return null;
    }
    /**
     * 获取所有启用科室的名称列表
     */
    private String getActiveDepartmentNames() {
        List<Department> departments = departmentMapper.selectActiveList();
        if (departments == null || departments.isEmpty()) {
            return "内科、外科、妇产科、儿科、眼科、耳鼻喉科、口腔科、皮肤科、骨科";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < departments.size(); i++) {
            sb.append(departments.get(i).getName());
            if (i < departments.size() - 1) {
                sb.append("、");
            }
        }
        return sb.toString();
    }
}