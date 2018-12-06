package com.java.gmall.user.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.java.gmall.bean.UserInfo;
import com.java.gmall.service.UserService;
import com.java.gmall.user.mapper.UserInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserInfoMapper userInfoMapper;

    @Override
    public List<UserInfo> userList(){
        List<UserInfo> userInfolist = userInfoMapper.selectUserAndAddress();
        return userInfolist;
    }
//
//    @Override
//    public List<UserInfo> userList(){
//        List<UserInfo> userInfolist = userInfoMapper.selectAllUserAddress();
//        return userInfolist;
//    }

}
