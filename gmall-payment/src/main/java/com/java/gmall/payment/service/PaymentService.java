package com.java.gmall.payment.service;

import com.java.gmall.bean.PaymentInfo;

public interface PaymentService {
    void updatePayment(PaymentInfo paymentInfo);

    void savePayment(PaymentInfo paymentInfo);

    boolean checkPayStatus(String out_trade_no);
}
