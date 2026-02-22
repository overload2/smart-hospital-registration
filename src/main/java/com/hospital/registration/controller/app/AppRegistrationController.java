package com.hospital.registration.controller.app;

import com.hospital.registration.common.Result;
import com.hospital.registration.dto.RegistrationDTO;
import com.hospital.registration.service.PaymentService;
import com.hospital.registration.service.RegistrationService;
import com.hospital.registration.vo.PaymentVO;
import com.hospital.registration.vo.RegistrationVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @title: AppRegistrationController
 * @author: Su
 * @date: 2026/2/20
 * @version: 1.0
 * @description: 患者端挂号控制器
 */
@Slf4j
@RestController
@RequestMapping("/app/registration")
public class AppRegistrationController {

    private final RegistrationService registrationService;
    private final PaymentService paymentService;

    /**
     * 构造器注入
     */
    public AppRegistrationController(RegistrationService registrationService, PaymentService paymentService) {
        this.registrationService = registrationService;
        this.paymentService = paymentService;
    }

    /**
     * 创建挂号
     */
    @PostMapping("/create")
    public Result create(@Valid @RequestBody RegistrationDTO registrationDTO, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        log.info("患者端创建挂号 - 用户ID: {}, 排班ID: {}", userId, registrationDTO.getScheduleId());
        // 设置患者ID为当前登录用户
        registrationDTO.setPatientId(userId);
        RegistrationVO registrationVO = registrationService.createRegistration(registrationDTO);
        return Result.ok("挂号成功").data("registration", registrationVO);
    }

    /**
     * 我的挂号列表
     */
    @GetMapping("/my-list")
    public Result myList(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        log.info("患者端获取我的挂号列表 - 用户ID: {}", userId);
        List<RegistrationVO> registrations = registrationService.getPatientRegistrations(userId);
        return Result.ok().data("registrations", registrations);
    }

    /**
     * 挂号详情
     */
    @GetMapping("/{id}")
    public Result detail(@PathVariable Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        log.info("患者端获取挂号详情 - ID: {}, 用户ID: {}", id, userId);
        RegistrationVO registrationVO = registrationService.getRegistrationById(id);
        // 验证是否是当前用户的挂号
        if (!registrationVO.getPatientId().equals(userId)) {
            return Result.error("无权查看该挂号记录");
        }
        return Result.ok().data("registration", registrationVO);
    }

    /**
     * 取消挂号
     */
    @PostMapping("/cancel/{id}")
    public Result cancel(@PathVariable Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        log.info("患者端取消挂号 - ID: {}, 用户ID: {}", id, userId);
        // 先验证是否是当前用户的挂号
        RegistrationVO registrationVO = registrationService.getRegistrationById(id);
        if (!registrationVO.getPatientId().equals(userId)) {
            return Result.error("无权取消该挂号记录");
        }
        registrationService.cancelRegistration(id);
        return Result.ok("取消成功");
    }

    /**
     * 支付挂号费（模拟支付）
     */
    @PostMapping("/pay/{id}")
    public Result pay(@PathVariable Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        log.info("患者端支付挂号费 - 挂号ID: {}, 用户ID: {}", id, userId);

        // 验证是否是当前用户的挂号
        RegistrationVO registrationVO = registrationService.getRegistrationById(id);
        if (!registrationVO.getPatientId().equals(userId)) {
            return Result.error("无权操作该挂号记录");
        }

        // 查询支付记录
        PaymentVO paymentVO = paymentService.getPaymentByRegistrationId(id);
        if (paymentVO == null) {
            return Result.error("支付记录不存在");
        }

        // 模拟支付回调
        boolean success = paymentService.handlePaymentCallback(paymentVO.getTransactionNo());
        if (success) {
            return Result.ok("支付成功");
        } else {
            return Result.error("支付失败");
        }
    }
}
