package com.java.gmall.service;

import com.java.gmall.bean.CartInfo;

public interface CartService {
    CartInfo selectCartExists(CartInfo cartInfo);

    void addCart(CartInfo cartInfo);

    void updateCart(CartInfo cartInfoDB);

    void flushCache(String userId);
}
