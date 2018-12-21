package com.java.gmall.service;

import com.java.gmall.bean.CartInfo;

import java.util.List;

public interface CartService {
    CartInfo selectCartExists(CartInfo cartInfo);

    void addCart(CartInfo cartInfo);

    void updateCart(CartInfo cartInfoDB);

    void flushCache(String userId);

    List<CartInfo> getCartListFromCache(String userId);

    List<CartInfo> getCartListByUserId(String userId);

    void mergCart(List<CartInfo> cartInfos, String id);
}
