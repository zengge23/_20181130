package com.java.gmall.payment.service;

import com.java.gmall.bean.PaymentInfo;

import java.util.Map;

public interface PaymentService {
    void updatePayment(PaymentInfo paymentInfo);

    void savePayment(PaymentInfo paymentInfo);

    boolean checkPayStatus(String out_trade_no);

    void sendPaymentSuccessQueue(PaymentInfo paymentInfo);

    void sendDelayPaymentResult(PaymentInfo paymentInfo, int count);

    Map<String, String> checkAlipayPayment(String out_trade_no);

    PaymentInfo getPaymentByOrderOutTradeNo(String out_trade_no);
}
