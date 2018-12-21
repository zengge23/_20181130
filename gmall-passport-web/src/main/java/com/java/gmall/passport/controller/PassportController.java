package com.java.gmall.passport.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.java.gmall.bean.CartInfo;
import com.java.gmall.bean.UserInfo;
import com.java.gmall.service.CartService;
import com.java.gmall.service.UserService;
import com.java.gmall.util.CookieUtil;
import com.java.gmall.util.JwtUtil;
import org.apache.catalina.connector.Response;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class PassportController {

    @Reference
    UserService userService;

    @Reference
    CartService cartService;

    @RequestMapping("index")
    public String index(String returnUrl, ModelMap map){
        map.put("returnUrl",returnUrl);
        return "index";
    }

    @RequestMapping("login")
    @ResponseBody
    public String login(UserInfo userInfo, HttpServletRequest request, HttpServletResponse response){
        //验证用户名和密码,返回token
        userInfo = userService.login(userInfo);
        if(null == userInfo){
            //用户名或密码错误
            return "fail";
        }else{
            Map<String,String> map = new HashMap<>();

            //****************************
            map.put("userId","2");
            map.put("nickName","jerry");

            //通过负载均衡nginx
            String ip = request.getHeader("x-forwarded-for");
            if(StringUtils.isBlank(ip)){
                ip = request.getRemoteAddr();
                if(StringUtils.isBlank(ip)){
                    ip = "127.0.0.1";
                }
            }
            String token = JwtUtil.encode("gmall0725",map,ip);

            //合并购物车
            String cartListCookie = CookieUtil.getCookieValue(request,"cartListCookie",true);
            if(StringUtils.isNotBlank(cartListCookie)){
                List<CartInfo> cartInfos = JSON.parseArray(cartListCookie,CartInfo.class);
                //合并当前用户的购物车
                cartService.mergCart(cartInfos,userInfo.getId());
                //清理cookie
                CookieUtil.setCookie(request, response,"cartListCookie","",0,true);
            }else{

                cartService.flushCache(userInfo.getId());
            }

            return token;
        }
    }

    @RequestMapping("verify")
    @ResponseBody
    public String verify(String token,HttpServletRequest request,String currentIp){

        Map gmall0725 = JwtUtil.decode("gmall0725", token, currentIp);

        if(null==gmall0725){
            return "fail";
        }else{
            return "success";
        }
    }

//    @RequestMapping("verify")
//    @ResponseBody
//    public String verify(String token,HttpServletRequest request,String currentIp){
//        Map gmall0725 = JwtUtil.decode("gmall0725", token, currentIp);
//        if(null == gmall0725){
//            return  "fail";
//        }else{
//            return "success";
//        }
//    }
}

