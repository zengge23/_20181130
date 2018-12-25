package com.java.gmall.order.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.java.gmall.bean.CartInfo;
import com.java.gmall.bean.OrderDetail;
import com.java.gmall.bean.OrderInfo;
import com.java.gmall.order.mapper.OrderDetailMapper;
import com.java.gmall.order.mapper.OrderInfoMapper;
import com.java.gmall.service.OrderService;
import com.java.gmall.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import redis.clients.jedis.Jedis;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    OrderInfoMapper orderInfoMapper;

    @Autowired
    OrderDetailMapper orderDetailMapper;

    @Override
    public List<OrderDetail> getOrderDetailList(List<CartInfo> cartListByUserId) {
        return null;
    }

    @Override
    public void putTradeCode(String tradeCode, String userId) {

        Jedis jedis = redisUtil.getJedis();
        jedis.setex("user:" + userId + ":tradeCode",60*30,tradeCode);
        jedis.close();
    }

    @Override
    public boolean checkTradeCode(String userId, String tradeCode) {
        Jedis jedis = redisUtil.getJedis();
        boolean b = false;
        String tradeCodeCache = jedis.get("user:" + userId + ":tradeCode");
        if(StringUtils.isNotBlank(tradeCodeCache)){
            if(tradeCodeCache.equals(tradeCode)) {
                jedis.del("user:" + userId + ":tradeCode");
                b = true;
            }
        }
        jedis.close();
        return b;
    }

    @Override
    public void saveOrder(OrderInfo orderInfo) {
        //新增订单
        orderInfoMapper.insertSelective(orderInfo);
        String orderId = orderInfo.getId();

        //新增订单详情
        List<OrderDetail> orderDetailList = orderInfo.getOrderDetailList();
        for (OrderDetail orderDetail : orderDetailList) {
            orderDetail.setOrderId(orderId);
            orderDetailMapper.insertSelective(orderDetail);
        }

    }

    @Override
    public void deleteCheckedCart(List<String> delList) {
        String delStr = StringUtils.join(delList,",");
        orderInfoMapper.deleteCheckedCart(delStr);
    }

    @Override
    public OrderInfo getOrderByOutTradeNo(String outTradeNo) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOutTradeNo(outTradeNo);
        OrderInfo orderInfo1 = orderInfoMapper.selectOne(orderInfo);

        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderId(orderInfo1.getId());
        List<OrderDetail> orderDetails = orderDetailMapper.select(orderDetail);

        orderInfo1.setOrderDetailList(orderDetails);

        return orderInfo1;
    }
}
