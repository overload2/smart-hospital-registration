package com.hospital.registration.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hospital.registration.entity.Payment;
import com.hospital.registration.vo.PaymentVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @title: PaymentMapper
 * @author: Su
 * @date: 2026/2/16
 * @version: 1.0
 * @description: 支付Mapper
 */
@Mapper
public interface PaymentMapper extends BaseMapper<Payment> {

    /**
     * 根据挂号ID查询支付记录
     */
    Payment selectByRegistrationId(@Param("registrationId") Long registrationId);

    /**
     * 根据交易流水号查询支付记录
     */
    Payment selectByTransactionNo(@Param("transactionNo") String transactionNo);

    /**
     * 查询用户的支付记录
     */
    List<Payment> selectByUserId(@Param("userId") Long userId);

    /**
     * 分页查询支付记录（关联挂号和患者信息）
     */
    Page<PaymentVO> selectPageWithDetails(Page<PaymentVO> page,
                                          @Param("transactionNo") String transactionNo,
                                          @Param("registrationNo") String registrationNo,
                                          @Param("paymentStatus") String paymentStatus,
                                          @Param("paymentMethod") String paymentMethod,
                                          @Param("startDate") String startDate,
                                          @Param("endDate") String endDate);
}
