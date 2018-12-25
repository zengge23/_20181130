package com.java.gmall.payment.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.java.gmall.bean.PaymentInfo;
import com.java.gmall.payment.mapper.PaymentInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

@Service
public class PaymentServiceImpl implements PaymentService{

    @Autowired
    PaymentInfoMapper paymentInfoMapper;

    @Override
    public void updatePayment(PaymentInfo paymentInfo) {
        Example e = new Example(PaymentInfo.class);
        e.createCriteria().andEqualTo("outTradeNo",paymentInfo.getOutTradeNo());
        paymentInfoMapper.updateByExampleSelective(paymentInfo,e);
    }

    @Override
    public void savePayment(PaymentInfo paymentInfo) {
        paymentInfoMapper.insertSelective(paymentInfo);
    }

    @Override
    public boolean checkPayStatus(String out_trade_no) {
        boolean b = true;

        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setOutTradeNo(out_trade_no);
        PaymentInfo paymentInfo1 = paymentInfoMapper.selectOne(paymentInfo);
        if(null != paymentInfo1 && paymentInfo1.getPaymentStatus().equals("未支付")){
            b = false;
        }
        return b;
    }
}
