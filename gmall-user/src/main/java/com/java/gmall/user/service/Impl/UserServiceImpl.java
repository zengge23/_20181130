package com.java.gmall.user.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.java.gmall.bean.UserAddress;
import com.java.gmall.bean.UserInfo;
import com.java.gmall.service.UserService;
import com.java.gmall.user.mapper.UserAddressMapper;
import com.java.gmall.user.mapper.UserInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserInfoMapper userInfoMapper;

    @Autowired
    UserAddressMapper userAddressMapper;

    @Override
    public List<UserInfo> userList(){
        List<UserInfo> userInfolist = userInfoMapper.selectUserAndAddress();
        return userInfolist;
    }

    @Override
    public UserInfo login(UserInfo userInfo) {
        UserInfo userInfo1 = userInfoMapper.selectOne(userInfo);
        if(userInfo1 != null){
            //用户数据放入redis
        }
        System.out.println(userInfo1+"1111111");
        return userInfo1;
    }


    @Override
    public List<UserAddress> getAddressListByUserId(String userId) {
        UserAddress userAddress = new UserAddress();
        userAddress.setUserId(userId);
        List<UserAddress> userAddresses = userAddressMapper.select(userAddress);
        return userAddresses;
    }

    @Override
    public UserAddress getAddressListById(String addressId) {
        UserAddress userAddress = new UserAddress();
        userAddress.setId(addressId);
        UserAddress userAddress1 = userAddressMapper.selectOne(userAddress);
        return userAddress1;
    }
//
//    @Override
//    public List<UserInfo> userList(){
//        List<UserInfo> userInfolist = userInfoMapper.selectAllUserAddress();
//        return userInfolist;
//    }

}
