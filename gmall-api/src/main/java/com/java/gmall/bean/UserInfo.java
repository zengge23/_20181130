package com.java.gmall.bean;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.List;

public class UserInfo implements Serializable {

//    @Id
    private String id;
//    @Column(name="login_name")
    private String loginName;
//    @Column(name="nick_name")
    private String nickName;
//    @Column(name="password")
    private Integer passwd;
//    @Column(name="name")
    private String name;
//    @Column(name="phone_num")
    private String phoneNum;
//    @Column(name="email")
    private String email;
//    @Column(name="head_img")
    private String headImg;
//    @Column(name="user_level")
    private String userLevel;

    private List<UserAddress> userAddresses;

    public List<UserAddress> getUserAddresses() {
        return userAddresses;
    }

    public void setUserAddresses(List<UserAddress> userAddresses) {
        this.userAddresses = userAddresses;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Integer getPasswd() {
        return passwd;
    }

    public void setPasswd(Integer passwd) {
        this.passwd = passwd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(String userLevel) {
        this.userLevel = userLevel;
    }
}
