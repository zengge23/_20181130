package com.java.gmall.payment.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.java.gmall.bean.PaymentInfo;
import com.java.gmall.payment.mapper.PaymentInfoMapper;
import com.java.gmall.util.ActiveMQUtil;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQMapMessage;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import javax.jms.*;
import javax.management.JMException;

@Service
public class PaymentServiceImpl implements PaymentService{

    @Autowired
    PaymentInfoMapper paymentInfoMapper;

    @Autowired
    ActiveMQUtil activeMQUtil;

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

    @Override
    public void sendPaymentSuccessQueue(PaymentInfo paymentInfo) {

        //发送支付成功的消息队列

        //建立mq工厂
        try {
            Connection connection = activeMQUtil.getConnection();
            connection.start();
            //第一个值表示是否使用事务，如果选择true，第二个值相当于选择0
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination testqueue = session.createQueue("PAYMENT_SUCCESS_QUEUE");

            MessageProducer producer = session.createProducer(testqueue);
            MapMessage mapMessage = new ActiveMQMapMessage();
            mapMessage.setString("out_trade_no",paymentInfo.getOutTradeNo());
            mapMessage.setString("trade_no",paymentInfo.getAlipayTradeNo());

            producer.setDeliveryMode(DeliveryMode.PERSISTENT);
            producer.send(mapMessage);

            session.commit();
            connection.close();

        }catch (JMSException e){
            e.printStackTrace();
        }
    }
}
