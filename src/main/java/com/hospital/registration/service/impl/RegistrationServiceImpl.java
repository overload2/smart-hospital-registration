package com.hospital.registration.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hospital.registration.common.BusinessException;
import com.hospital.registration.common.PaymentStatus;
import com.hospital.registration.common.RegistrationStatus;
import com.hospital.registration.common.ResultCode;
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
import java.util.List;

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
        log.info("创建挂号 - 患者ID: {}, 排班ID: {}",
                registrationDTO.getPatientId(), registrationDTO.getScheduleId());

        // 检查患者是否存在
        User patient = userMapper.selectById(registrationDTO.getPatientId());
        if (patient == null) {
            log.warn("患者不存在 - 患者ID: {}", registrationDTO.getPatientId());
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "患者不存在");
        }

        // 查询排班详情
        ScheduleVO scheduleVO = scheduleMapper.selectDetailById(registrationDTO.getScheduleId());
        if (scheduleVO == null) {
            log.warn("排班不存在 - 排班ID: {}", registrationDTO.getScheduleId());
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "排班不存在");
        }

        // 检查排班状态
        if (scheduleVO.getStatus() != 1) {
            log.warn("排班不可预约 - 排班ID: {}, 状态: {}",
                    registrationDTO.getScheduleId(), scheduleVO.getStatus());
            throw new BusinessException(ResultCode.FAIL.getCode(), "该排班不可预约");
        }

        // 检查剩余号源
        if (scheduleVO.getRemainingNumber() <= 0) {
            log.warn("号源已满 - 排班ID: {}", registrationDTO.getScheduleId());
            throw new BusinessException(ResultCode.FAIL.getCode(), "号源已满,无法挂号");
        }

        // 检查挂号日期是否已过期
        if (scheduleVO.getScheduleDate().isBefore(LocalDate.now())) {
            log.warn("挂号日期已过期 - 排班日期: {}", scheduleVO.getScheduleDate());
            throw new BusinessException(ResultCode.FAIL.getCode(), "挂号日期已过期");
        }

        // 减少排班剩余号源
        int updateResult = scheduleMapper.decreaseRemainingNumber(registrationDTO.getScheduleId());
        if (updateResult <= 0) {
            log.error("减少号源失败 - 排班ID: {}", registrationDTO.getScheduleId());
            throw new BusinessException(ResultCode.FAIL.getCode(), "号源不足,挂号失败");
        }

        // 获取当前排队号
        Integer maxQueueNumber = registrationMapper.selectMaxQueueNumber(registrationDTO.getScheduleId());
        int queueNumber = (maxQueueNumber == null ? 0 : maxQueueNumber) + 1;

        // 生成挂号单号
        String registrationNo = generateRegistrationNo();

        // 创建挂号记录
        Registration registration = new Registration();
        registration.setRegistrationNo(registrationNo);
        registration.setPatientId(registrationDTO.getPatientId());
        registration.setDoctorId(scheduleVO.getDoctorId());
        registration.setDepartmentId(scheduleVO.getDepartmentId());
        registration.setScheduleId(registrationDTO.getScheduleId());
        registration.setRegistrationDate(scheduleVO.getScheduleDate());
        registration.setTimeSlot(scheduleVO.getTimeSlot());
        registration.setQueueNumber(queueNumber);
        registration.setRegistrationFee(scheduleVO.getRegistrationFee());
        registration.setStatus(RegistrationStatus.PENDING);
        registration.setSymptom(registrationDTO.getSymptom());
        registration.setPaymentStatus(PaymentStatus.PENDING);

        // 保存到数据库
        int result = registrationMapper.insert(registration);
        if (result <= 0) {
            log.error("挂号创建失败 - 患者ID: {}", registrationDTO.getPatientId());
            throw new BusinessException(ResultCode.FAIL.getCode(), "挂号创建失败");
        }

        log.info("挂号创建成功 - ID: {}, 挂号单号: {}, 排队号: {}",
                registration.getId(), registrationNo, queueNumber);

        // 查询并返回完整的挂号信息
        RegistrationVO registrationVO = registrationMapper.selectDetailById(registration.getId());
        enrichRegistrationVO(registrationVO);
        //发送挂号成功通知
        messageService.sendRegistrationSuccessNotice(registration, scheduleVO.getDepartmentName(), scheduleVO.getDoctorName());
        return registrationVO;
    }

    /**
     * 取消挂号
     *
     * @param id 挂号ID
     * @throws BusinessException 当挂号不存在或状态不允许取消时抛出异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelRegistration(Long id) {
        log.info("取消挂号 - ID: {}", id);

        // 检查挂号是否存在
        Registration registration = registrationMapper.selectById(id);
        if (registration == null) {
            log.warn("挂号记录不存在 - ID: {}", id);
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "挂号记录不存在");
        }

        // 检查挂号状态
        if (registration.getStatus() == RegistrationStatus.CANCELLED) {
            log.warn("挂号已取消 - ID: {}", id);
            throw new BusinessException(ResultCode.FAIL.getCode(), "挂号已取消");
        }

        if (registration.getStatus() == RegistrationStatus.COMPLETED) {
            log.warn("挂号已完成,无法取消 - ID: {}", id);
            throw new BusinessException(ResultCode.FAIL.getCode(), "挂号已完成,无法取消");
        }

        if (registration.getStatus() == RegistrationStatus.CONSULTING) {
            log.warn("就诊中,无法取消 - ID: {}", id);
            throw new BusinessException(ResultCode.FAIL.getCode(), "就诊中,无法取消");
        }

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
        Registration registration = registrationMapper.selectById(id);
        if (registration == null) {
            log.warn("挂号记录不存在 - ID: {}", id);
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "挂号记录不存在");
        }

        // 创建新的挂号对象,只设置需要更新的字段
        Registration updateRegistration = new Registration();
        updateRegistration.setId(id);
        updateRegistration.setStatus(RegistrationStatus.valueOf(status));

        int result = registrationMapper.updateById(updateRegistration);
        if (result <= 0) {
            log.error("挂号状态更新失败 - ID: {}", id);
            throw new BusinessException(ResultCode.FAIL.getCode(), "挂号状态更新失败");
        }

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
        Registration registration = registrationMapper.selectById(id);
        if (registration == null) {
            log.warn("挂号记录不存在 - ID: {}", id);
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "挂号记录不存在");
        }

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
}
