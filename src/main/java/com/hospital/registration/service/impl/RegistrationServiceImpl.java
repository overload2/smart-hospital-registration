package com.hospital.registration.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hospital.registration.common.BusinessException;
import com.hospital.registration.common.PaymentStatus;
import com.hospital.registration.common.RegistrationStatus;
import com.hospital.registration.common.ResultCode;
import com.hospital.registration.dto.PaymentDTO;
import com.hospital.registration.dto.RegistrationDTO;
import com.hospital.registration.entity.Registration;
import com.hospital.registration.entity.User;
import com.hospital.registration.mapper.RegistrationMapper;
import com.hospital.registration.mapper.ScheduleMapper;
import com.hospital.registration.mapper.UserMapper;
import com.hospital.registration.service.MessageService;
import com.hospital.registration.service.PaymentService;
import com.hospital.registration.service.RegistrationService;
import com.hospital.registration.vo.RegistrationVO;
import com.hospital.registration.vo.ScheduleVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @title: RegistrationServiceImpl
 * @author: Su
 * @date: 2026/2/14
 * @version: 1.0
 * @description: 挂号服务实现类
 */
@Slf4j
@Service
public class RegistrationServiceImpl implements RegistrationService {

    private final RegistrationMapper registrationMapper;
    private final ScheduleMapper scheduleMapper;
    private final UserMapper userMapper;
    private final PaymentService paymentService;
    private final MessageService messageService;

    /**
     * 构造器注入
     */
    public RegistrationServiceImpl(RegistrationMapper registrationMapper,
                                   ScheduleMapper scheduleMapper,
                                   UserMapper userMapper,
                                   PaymentService paymentService,
                                   MessageService messageService) {
        this.registrationMapper = registrationMapper;
        this.scheduleMapper = scheduleMapper;
        this.userMapper = userMapper;
        this.paymentService = paymentService;
        this.messageService = messageService;
    }

    /**
     * 创建挂号
     *
     * @param registrationDTO 挂号信息DTO
     * @return 新增后的挂号VO
     * @throws BusinessException 当患者不存在、排班不存在或号源不足时抛出异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public RegistrationVO createRegistration(RegistrationDTO registrationDTO) {
        log.info("创建挂号 - 患者ID: {}, 排班ID: {}, 细分时段: {}",
                registrationDTO.getPatientId(),
                registrationDTO.getScheduleId(),
                registrationDTO.getDetailTimeSlot());

        // 1. 验证患者
        User patient = validatePatient(registrationDTO.getPatientId());

        // 2. 验证排班
        ScheduleVO scheduleVO = validateSchedule(registrationDTO.getScheduleId());

        // 3. 验证细分时段
        validateDetailTimeSlot(registrationDTO, scheduleVO);

        // 4. 扣减号源
        decreaseScheduleNumber(registrationDTO.getScheduleId());

        // 5. 创建挂号记录
        Registration registration = buildRegistration(registrationDTO, scheduleVO);
        registrationMapper.insert(registration);

        // 6. 创建支付订单
        createPaymentOrder(registration);

        // 7. 发送通知
        sendRegistrationNotice(registration, scheduleVO);

        log.info("挂号创建成功 - ID: {}, 挂号单号: {}", registration.getId(), registration.getRegistrationNo());

        RegistrationVO registrationVO = registrationMapper.selectDetailById(registration.getId());
        enrichRegistrationVO(registrationVO);
        return registrationVO;
    }

    /**
     * 验证患者是否存在
     */
    private User validatePatient(Long patientId) {
        User patient = userMapper.selectById(patientId);
        if (patient == null) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "患者不存在");
        }
        return patient;
    }

    /**
     * 验证排班是否可预约
     */
    private ScheduleVO validateSchedule(Long scheduleId) {
        ScheduleVO scheduleVO = scheduleMapper.selectDetailById(scheduleId);
        if (scheduleVO == null) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "排班不存在");
        }
        if (scheduleVO.getStatus() != 1) {
            throw new BusinessException(ResultCode.FAIL.getCode(), "该排班不可预约");
        }
        if (scheduleVO.getRemainingNumber() <= 0) {
            throw new BusinessException(ResultCode.FAIL.getCode(), "号源已满");
        }
        if (scheduleVO.getScheduleDate().isBefore(LocalDate.now())) {
            throw new BusinessException(ResultCode.FAIL.getCode(), "挂号日期已过期");
        }
        return scheduleVO;
    }

    /**
     * 验证细分时段是否可预约
     */
    private void validateDetailTimeSlot(RegistrationDTO dto, ScheduleVO scheduleVO) {
        String detailTimeSlot = dto.getDetailTimeSlot();
        if (detailTimeSlot == null || detailTimeSlot.isEmpty()) {
            throw new BusinessException(ResultCode.FAIL.getCode(), "请选择就诊时段");
        }

        // 查询该细分时段已预约数量
        List<Map<String, Object>> bookedCounts = registrationMapper.countByScheduleAndDetailSlot(dto.getScheduleId());
        Map<String, Integer> bookedMap = new HashMap<>();
        for (Map<String, Object> item : bookedCounts) {
            String slot = (String) item.get("detailTimeSlot");
            Integer count = ((Number) item.get("count")).intValue();
            bookedMap.put(slot, count);
        }

        Integer slotCapacity = scheduleVO.getSlotCapacity() != null ? scheduleVO.getSlotCapacity() : 5;
        Integer bookedCount = bookedMap.getOrDefault(detailTimeSlot, 0);
        if (bookedCount >= slotCapacity) {
            throw new BusinessException(ResultCode.FAIL.getCode(), "该时段已满，请选择其他时段");
        }

        // 检查用户是否已预约该时段
        List<String> userBookedSlots = registrationMapper.selectBookedDetailSlots(dto.getPatientId(), dto.getScheduleId());
        if (userBookedSlots.contains(detailTimeSlot)) {
            throw new BusinessException(ResultCode.FAIL.getCode(), "您已预约该时段");
        }
    }

    /**
     * 扣减排班号源
     */
    private void decreaseScheduleNumber(Long scheduleId) {
        int result = scheduleMapper.decreaseRemainingNumber(scheduleId);
        if (result <= 0) {
            throw new BusinessException(ResultCode.FAIL.getCode(), "号源不足，挂号失败");
        }
    }

    /**
     * 构建挂号实体
     */
    private Registration buildRegistration(RegistrationDTO dto, ScheduleVO scheduleVO) {
        Integer maxQueueNumber = registrationMapper.selectMaxQueueNumber(dto.getScheduleId());
        int queueNumber = (maxQueueNumber == null ? 0 : maxQueueNumber) + 1;

        Registration registration = new Registration();
        registration.setRegistrationNo(generateRegistrationNo());
        registration.setPatientId(dto.getPatientId());
        registration.setDoctorId(scheduleVO.getDoctorId());
        registration.setDepartmentId(scheduleVO.getDepartmentId());
        registration.setScheduleId(dto.getScheduleId());
        registration.setRegistrationDate(scheduleVO.getScheduleDate());
        registration.setTimeSlot(scheduleVO.getTimeSlot());
        registration.setQueueNumber(queueNumber);
        registration.setRegistrationFee(scheduleVO.getRegistrationFee());
        registration.setStatus(RegistrationStatus.PENDING);
        registration.setSymptom(dto.getSymptom());
        registration.setPaymentStatus(PaymentStatus.PENDING);
        registration.setDetailTimeSlot(dto.getDetailTimeSlot());
        return registration;
    }

    /**
     * 创建支付订单
     */
    private void createPaymentOrder(Registration registration) {
        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setRegistrationId(registration.getId());
        paymentDTO.setAmount(registration.getRegistrationFee());
        paymentDTO.setPaymentMethod("WECHAT");
        paymentService.createPayment(paymentDTO);
    }

    /**
     * 发送挂号成功通知
     */
    private void sendRegistrationNotice(Registration registration, ScheduleVO scheduleVO) {
        messageService.sendRegistrationSuccessNotice(registration, scheduleVO.getDepartmentName(), scheduleVO.getDoctorName());
    }



    /**
     * 取消挂号pp/schedule
     *
     * @param id 挂号ID
     * @throws BusinessException 当挂号不存在或状态不允许取消时抛出异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelRegistration(Long id) {
        log.info("取消挂号 - ID: {}", id);

        // 检查挂号是否存在
        Registration registration = getRegistrationEntityById(id);

        // 检查挂号状态
        validateStatus(registration,RegistrationStatus.CANCELLED, "挂号已取消");
        validateStatus(registration,RegistrationStatus.COMPLETED, "挂号已完成,无法取消");
        validateStatus(registration,RegistrationStatus.CONSULTING, "就诊中,无法取消");

        // 增加排班剩余号源
        int updateResult = scheduleMapper.increaseRemainingNumber(registration.getScheduleId());
        if (updateResult <= 0) {
            log.error("增加号源失败 - 排班ID: {}", registration.getScheduleId());
            throw new BusinessException(ResultCode.FAIL.getCode(), "取消挂号失败");
        }
        // 更新挂号状态为已取消
        Registration updateRegistration = new Registration();
        updateRegistration.setId(id);
        updateRegistration.setStatus(RegistrationStatus.CANCELLED);
        //处理退费逻辑
        if (registration.getPaymentStatus() == PaymentStatus.PAID) {
            log.info("挂号已支付，需要退费 - ID: {}，挂号单号: {}，挂号费: {}", id, registration.getRegistrationNo(), registration.getRegistrationFee());
            //更新支付状态为已退款
            updateRegistration.setPaymentStatus(PaymentStatus.REFUNDED);
            //发送退款消息到RabbitMQ队列，由消费者异步处理退款
            paymentService.refundAsync(registration.getRegistrationNo(), registration.getRegistrationFee());
            log.info("挂号取消成功，已发起退款 - ID: {}，退款金额: {}", id, registration.getRegistrationFee());
        }
        int result = registrationMapper.updateById(updateRegistration);
        if (result <= 0) {
            log.error("挂号取消失败 - ID: {}", id);
            throw new BusinessException(ResultCode.FAIL.getCode(), "挂号取消失败");
        }
        log.info("挂号取消成功 - ID: {}", id);
        // 发送挂号取消通知
        RegistrationVO registrationVO = registrationMapper.selectDetailById(id);
        messageService.sendRegistrationCancelNotice(registration, registrationVO.getDepartmentName(), registrationVO.getDoctorName());
    }

    /**
     * 根据ID查询挂号详情
     *
     * @param id 挂号ID
     * @return 挂号VO(包含患者、医生、科室信息)
     * @throws BusinessException 当挂号不存在时抛出异常
     */
    @Override
    public RegistrationVO getRegistrationById(Long id) {
        log.info("查询挂号详情 - ID: {}", id);

        RegistrationVO registrationVO = registrationMapper.selectDetailById(id);
        if (registrationVO == null) {
            log.warn("挂号记录不存在 - ID: {}", id);
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "挂号记录不存在");
        }

        enrichRegistrationVO(registrationVO);

        log.info("查询挂号成功 - ID: {}, 挂号单号: {}", id, registrationVO.getRegistrationNo());
        return registrationVO;
    }

    /**
     * 根据挂号单号查询挂号详情
     *
     * @param registrationNo 挂号单号
     * @return 挂号VO
     * @throws BusinessException 当挂号不存在时抛出异常
     */
    @Override
    public RegistrationVO getRegistrationByNo(String registrationNo) {
        log.info("根据挂号单号查询挂号详情 - 挂号单号: {}", registrationNo);

        Registration registration = registrationMapper.selectByRegistrationNo(registrationNo);
        if (registration == null) {
            log.warn("挂号记录不存在 - 挂号单号: {}", registrationNo);
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "挂号记录不存在");
        }

        RegistrationVO registrationVO = registrationMapper.selectDetailById(registration.getId());
        enrichRegistrationVO(registrationVO);

        log.info("查询挂号成功 - 挂号单号: {}", registrationNo);
        return registrationVO;
    }

    /**
     * 分页查询挂号列表
     *
     * @param pageNum 页码(从1开始)
     * @param pageSize 每页大小
     * @param patientId 患者ID(可选)
     * @param doctorId 医生ID(可选)
     * @param departmentId 科室ID(可选)
     * @param registrationDate 挂号日期(可选)
     * @param status 挂号状态(可选)
     * @return 挂号VO分页对象
     */
    @Override
    public Page<RegistrationVO> getRegistrationPage(Integer pageNum, Integer pageSize,
                                                    Long patientId, Long doctorId,
                                                    Long departmentId, LocalDate registrationDate,
                                                    String status) {
        log.info("分页查询挂号列表 - 页码: {}, 每页大小: {}, 患者ID: {}, 医生ID: {}, 科室ID: {}, 日期: {}, 状态: {}",
                pageNum, pageSize, patientId, doctorId, departmentId, registrationDate, status);

        // 创建分页对象
        Page<RegistrationVO> page = new Page<>(pageNum, pageSize);

        // 执行分页查询
        Page<RegistrationVO> registrationPage = registrationMapper.selectPageWithDetails(
                page, patientId, doctorId, departmentId, registrationDate, status);

        // 丰富VO信息
        registrationPage.getRecords().forEach(this::enrichRegistrationVO);

        log.info("查询到 {} 条挂号记录,共 {} 页",
                registrationPage.getRecords().size(), registrationPage.getPages());
        return registrationPage;
    }

    /**
     * 查询患者的挂号记录列表
     *
     * @param patientId 患者ID
     * @return 挂号VO列表
     */
    @Override
    public List<RegistrationVO> getPatientRegistrations(Long patientId) {
        log.info("查询患者的挂号记录列表 - 患者ID: {}", patientId);

        // 检查患者是否存在
        User patient = userMapper.selectById(patientId);
        if (patient == null) {
            log.warn("患者不存在 - 患者ID: {}", patientId);
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "患者不存在");
        }

        List<RegistrationVO> registrations = registrationMapper.selectByPatientId(patientId);

        // 丰富VO信息
        registrations.forEach(this::enrichRegistrationVO);

        log.info("查询到 {} 条挂号记录", registrations.size());
        return registrations;
    }

    /**
     * 查询医生指定日期的挂号记录列表
     *
     * @param doctorId 医生ID
     * @param registrationDate 挂号日期
     * @return 挂号VO列表
     */
    @Override
    public List<RegistrationVO> getDoctorRegistrations(Long doctorId, LocalDate registrationDate) {
        log.info("查询医生的挂号记录列表 - 医生ID: {}, 日期: {}", doctorId, registrationDate);

        List<RegistrationVO> registrations = registrationMapper.selectByDoctorAndDate(
                doctorId, registrationDate);

        // 丰富VO信息
        registrations.forEach(this::enrichRegistrationVO);

        log.info("查询到 {} 条挂号记录", registrations.size());
        return registrations;
    }

    /**
     * 更新挂号状态
     *
     * @param id 挂号ID
     * @param status 挂号状态
     * @throws BusinessException 当挂号不存在时抛出异常
     */
    @Override
    public void updateRegistrationStatus(Long id, String status) {
        log.info("更新挂号状态 - ID: {}, 状态: {}", id, status);
        // 检查挂号是否存在
        getRegistrationEntityById(id);
        // 创建新的挂号对象,只设置需要更新的字段
        doUpdateStatus(id, RegistrationStatus.valueOf(status));
        log.info("挂号状态更新成功 - ID: {}, 新状态: {}", id, status);
    }

    /**
     * 确认支付
     *
     * @param id 挂号ID
     * @throws BusinessException 当挂号不存在或已支付时抛出异常
     */
    @Override
    public void confirmPayment(Long id) {
        log.info("确认支付 - 挂号ID: {}", id);

        // 检查挂号是否存在
        Registration registration = getRegistrationEntityById(id);

        // 检查支付状态
        if (registration.getPaymentStatus() == PaymentStatus.PAID) {
            log.warn("挂号已支付 - ID: {}", id);
            throw new BusinessException(ResultCode.FAIL.getCode(), "挂号已支付");
        }

        // 更新支付状态
        Registration updateRegistration = new Registration();
        updateRegistration.setId(id);
        updateRegistration.setPaymentStatus(PaymentStatus.PAID);
        updateRegistration.setPaymentTime(LocalDateTime.now());

        int result = registrationMapper.updateById(updateRegistration);
        if (result <= 0) {
            log.error("支付确认失败 - ID: {}", id);
            throw new BusinessException(ResultCode.FAIL.getCode(), "支付确认失败");
        }

        log.info("支付确认成功 - ID: {}", id);
    }
    /**
     * 分页查询挂号列表（增强版）
     */
    @Override
    public Page<RegistrationVO> getRegistrationPage(Integer pageNum, Integer pageSize,
                                                    Long patientId, Long doctorId,
                                                    Long departmentId, LocalDate registrationDate,
                                                    String status, String registrationNo,
                                                    String patientName, String patientPhone,
                                                    String patientIdCard) {
        log.info("分页查询挂号列表 - 页码: {}, 每页大小: {}", pageNum, pageSize);

        Page<RegistrationVO> page = new Page<>(pageNum, pageSize);
        Page<RegistrationVO> registrationPage = registrationMapper.selectPageWithDetails(
                page, patientId, doctorId, departmentId, registrationDate, status,
                registrationNo, patientName, patientPhone, patientIdCard);

        // 丰富VO信息
        for (RegistrationVO vo : registrationPage.getRecords()) {
            enrichRegistrationVO(vo);
        }

        return registrationPage;
    }

    /**
     * 叫号
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public RegistrationVO callNumber(Long id) {
        log.info("叫号 - 挂号ID: {}", id);

        // 检查挂号是否存在
        Registration registration = getRegistrationEntityById( id);
        validateStatus(registration, RegistrationStatus.PENDING,"当前状态不允许叫号");

        // 检查支付状态
        if (registration.getPaymentStatus() != PaymentStatus.PAID) {
            throw new BusinessException(ResultCode.FAIL.getCode(), "该挂号未支付，无法叫号");
        }

        // 更新状态为已叫号
        doUpdateStatus(id, RegistrationStatus.CALLED);

        // 发送叫号通知给患者
        RegistrationVO registrationVO = registrationMapper.selectDetailById(id);
        enrichRegistrationVO(registrationVO);
        messageService.sendQueueCallNotice(registration, registrationVO.getDepartmentName(), registrationVO.getDoctorName());

        log.info("叫号成功 - 挂号ID: {}, 排队号: {}", id, registration.getQueueNumber());
        return registrationVO;
    }

    /**
     * 过号处理 - 重新排队
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void missedRequeue(Long id) {
        log.info("过号重新排队 - 挂号ID: {}", id);

        Registration registration = getRegistrationEntityById(id);
        validateStatus(registration, RegistrationStatus.CALLED, "当前状态不允许过号处理");

        // 获取当前最大排队号
        Integer maxQueueNumber = registrationMapper.selectMaxQueueNumber(registration.getScheduleId());
        int newQueueNumber = (maxQueueNumber == null ? 0 : maxQueueNumber) + 1;

        // 更新状态为待就诊，排队号改为最后
        Registration updateRegistration = new Registration();
        updateRegistration.setId(id);
        updateRegistration.setStatus(RegistrationStatus.PENDING);
        updateRegistration.setQueueNumber(newQueueNumber);
        registrationMapper.updateById(updateRegistration);

        log.info("过号重新排队成功 - 挂号ID: {}, 新排队号: {}", id, newQueueNumber);
    }

    /**
     * 过号处理 - 标记爽约
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void missedNoShow(Long id) {
        log.info("过号标记爽约 - 挂号ID: {}", id);

        Registration registration = getRegistrationEntityById(id);
        validateStatus(registration, RegistrationStatus.CALLED, "当前状态不允许标记爽约");

        // 更新状态为已过号（爽约）
        doUpdateStatus(id, RegistrationStatus.MISSED);

        log.info("过号标记爽约成功 - 挂号ID: {}", id);
    }

    /**
     * 获取今日候诊队列
     */
    @Override
    public List<RegistrationVO> getTodayQueue(Long doctorId) {
        log.info("获取今日候诊队列 - 医生ID: {}", doctorId);

        List<RegistrationVO> queue = registrationMapper.selectTodayQueue(doctorId, LocalDate.now());
        for (RegistrationVO vo : queue) {
            enrichRegistrationVO(vo);
        }

        return queue;
    }

    /**
     * 获取当前叫号信息
     */
    @Override
    public RegistrationVO getCurrentCalled(Long doctorId) {
        log.info("获取当前叫号信息 - 医生ID: {}", doctorId);

        RegistrationVO vo = registrationMapper.selectCurrentCalled(doctorId, LocalDate.now());
        if (vo != null) {
            enrichRegistrationVO(vo);
        }

        return vo;
    }

    /**
     * 生成挂号单号
     * 格式: REG + yyyyMMddHHmmss + 4位随机数
     */
    private String generateRegistrationNo() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        int random = (int) (Math.random() * 9000) + 1000;
        return "REG" + timestamp + random;
    }

    /**
     * 丰富RegistrationVO信息(添加时间段名称、状态名称等)
     */
    private void enrichRegistrationVO(RegistrationVO registrationVO) {
        if (registrationVO.getTimeSlot() != null) {
            registrationVO.setTimeSlotName(registrationVO.getTimeSlot().getName());
            registrationVO.setTimeRange(registrationVO.getTimeSlot().getTimeRange());
        }
        if (registrationVO.getStatus() != null) {
            registrationVO.setStatusName(registrationVO.getStatus().getName());
        }
        if (registrationVO.getPaymentStatus() != null) {
            registrationVO.setPaymentStatusName(registrationVO.getPaymentStatus().getName());
        }
    }
    /**
     * 根据ID查询挂号实体（内部使用）
     * @param id 挂号ID
     * @return 挂号实体
     * @throws BusinessException 当挂号不存在时抛出异常
     */
    private Registration getRegistrationEntityById(Long id) {
        Registration registration = registrationMapper.selectById(id);
        if (registration == null) {
            log.warn("挂号记录不存在 - ID: {}", id);
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "挂号记录不存在");
        }
        return registration;
    }

    /**
     * 更新挂号状态（内部使用，不做存在性检查）
     * @param id 挂号ID
     * @param status 目标状态
     */
    private void doUpdateStatus(Long id, RegistrationStatus status) {
        Registration updateRegistration = new Registration();
        updateRegistration.setId(id);
        updateRegistration.setStatus(status);
        registrationMapper.updateById(updateRegistration);
    }

    /**
     * 验证挂号状态是否为指定状态
     * @param registration 挂号记录
     * @param expectedStatus 期望的状态
     * @param errorMessage 不匹配时的错误信息
     */
    private void validateStatus(Registration registration, RegistrationStatus expectedStatus, String errorMessage) {
        if (registration.getStatus() != expectedStatus) {
            throw new BusinessException(ResultCode.FAIL.getCode(), errorMessage);
        }
    }

    /**
     * 验证挂号状态不为指定状态
     * @param registration 挂号记录
     * @param forbiddenStatus 禁止的状态
     * @param errorMessage 匹配时的错误信息
     */
    private void validateStatusNot(Registration registration, RegistrationStatus forbiddenStatus, String errorMessage) {
        if (registration.getStatus() == forbiddenStatus) {
            throw new BusinessException(ResultCode.FAIL.getCode(), errorMessage);
        }
    }
}
