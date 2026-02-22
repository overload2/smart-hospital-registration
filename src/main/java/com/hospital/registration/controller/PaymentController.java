package com.hospital.registration.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hospital.registration.annotation.OperationLog;
import com.hospital.registration.common.Constants;
import com.hospital.registration.common.Result;
import com.hospital.registration.dto.PaymentDTO;
import com.hospital.registration.dto.PaymentQueryDTO;
import com.hospital.registration.service.PaymentService;
import com.hospital.registration.utils.JwtUtil;
import com.hospital.registration.vo.PaymentVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @title: PaymentController
 * @author: Su
 * @date: 2026/2/16
 * @version: 1.0
 * @description: 支付控制器
 */
@Slf4j
@RestController
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentService paymentService;
    private final JwtUtil jwtUtil;

    public PaymentController(PaymentService paymentService, JwtUtil jwtUtil) {
        this.paymentService = paymentService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * 创建支付订单
     */
    @PostMapping
    @OperationLog(module = "支付管理", operation = "ADD")
    public Result createPayment(@Validated @RequestBody PaymentDTO paymentDTO) {
        log.info("创建支付订单 - 挂号ID: {}", paymentDTO.getRegistrationId());
        PaymentVO paymentVO = paymentService.createPayment(paymentDTO);
        return Result.ok("支付订单创建成功").data("payment", paymentVO);
    }

    /**
     * 支付回调（模拟）
     */
    @PostMapping("/callback/{transactionNo}")
    @OperationLog(module = "支付管理", operation = "UPDATE")
        public Result paymentCallback(@PathVariable String transactionNo) {
        log.info("支付回调 - 交易流水号: {}", transactionNo);
        boolean success = paymentService.handlePaymentCallback(transactionNo);
        if (success) {
            return Result.ok("支付成功");
        } else {
            return Result.error("支付失败");
        }
    }

    /**
     * 根据挂号ID查询支付记录
     */
    @GetMapping("/registration/{registrationId}")
    public Result getPaymentByRegistrationId(@PathVariable Long registrationId) {
        log.info("查询支付记录 - 挂号ID: {}", registrationId);
        PaymentVO paymentVO = paymentService.getPaymentByRegistrationId(registrationId);
        return Result.ok().data("payment", paymentVO);
    }

    /**
     * 根据交易流水号查询支付记录
     */
    @GetMapping("/transaction/{transactionNo}")
    public Result getPaymentByTransactionNo(@PathVariable String transactionNo) {
        log.info("查询支付记录 - 交易流水号: {}", transactionNo);
        PaymentVO paymentVO = paymentService.getPaymentByTransactionNo(transactionNo);
        return Result.ok().data("payment", paymentVO);
    }

    /**
     * 查询我的支付记录
     */
    @GetMapping("/my")
    public Result getMyPayments(@RequestHeader("Authorization") String authHeader) {
        // 从 Authorization 头中提取 token
        String token = authHeader.replace(Constants.Jwt.PREFIX, "");

        // 从 token 中获取用户ID
        Long userId = jwtUtil.getUserIdFromToken(token);
        if (userId == null) {
            return Result.error("无效的token");
        }

        log.info("查询我的支付记录 - 用户ID: {}", userId);
        List<PaymentVO> payments = paymentService.getUserPayments(userId);
        return Result.ok().data("payments", payments);
    }
    /**
     * 分页查询支付记录
     */
    @PostMapping("/page")
    public Result getPaymentPage(@RequestBody PaymentQueryDTO queryDTO) {
        log.info("分页查询支付记录");
        Page<PaymentVO> page = paymentService.getPaymentPage(queryDTO);
        return Result.ok().data("page", page);
    }
    /**
     * 手动退款（人工窗口退款）
     */
    @PostMapping("/manual-refund/{id}")
    @OperationLog(module = "支付管理", operation = "UPDATE")
    public Result manualRefund(@PathVariable Long id, @RequestBody Map<String, String> params) {
        String remark = params.get("remark");
        log.info("手动退款 - 支付ID: {}, 备注: {}", id, remark);
        paymentService.manualRefund(id, remark);
        return Result.ok("退款成功");
    }
}