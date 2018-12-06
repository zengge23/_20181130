package com.java.gmall.user.mapper;

import com.java.gmall.bean.UserInfo;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface UserInfoMapper extends Mapper<UserInfo> {
    List<UserInfo> selectUserAndAddress();

}
