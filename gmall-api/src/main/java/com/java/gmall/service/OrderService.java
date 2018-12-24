package com.java.gmall.service;

import com.java.gmall.bean.CartInfo;
import com.java.gmall.bean.OrderDetail;
import com.java.gmall.bean.OrderInfo;
import org.springframework.core.annotation.Order;

import java.util.List;

public interface OrderService {
    List<OrderDetail> getOrderDetailList(List<CartInfo> cartListByUserId);

    void putTradeCode(String tradeCode, String userId);

    boolean checkTradeCode(String userId, String tradeCode);

    void saveOrder(OrderInfo orderInfo);

    void deleteCheckedCart(List<String> delList);
}
