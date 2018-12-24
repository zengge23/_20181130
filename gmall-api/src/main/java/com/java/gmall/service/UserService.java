package com.java.gmall.service;

import com.java.gmall.bean.UserAddress;
import com.java.gmall.bean.UserInfo;

import java.util.List;

public interface UserService {
    List<UserInfo> userList();

    UserInfo login(UserInfo userInfo);

    List<UserAddress> getAddressListByUserId(String userId);

    UserAddress getAddressListById(String addressId);
}
