package com.java.gmall.interceptors;

import com.java.gmall.annotations.LoginRequire;
import com.java.gmall.util.CookieUtil;
import com.java.gmall.util.HttpClientUtil;
import com.java.gmall.util.JwtUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
public class AuthInterceptor extends HandlerInterceptorAdapter{

        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

            //判断当前请求方法的拦截类型
            HandlerMethod handler1 = (HandlerMethod) handler;
            LoginRequire methodAnnotation = handler1.getMethodAnnotation(LoginRequire.class);

            if(null == methodAnnotation){
                //不需要验证
                return true;
            }
            //是否必须验证通过
            boolean neededSuccess = methodAnnotation.isNeededSuccess();

            //获取用户的token
            String token = "";
            String oldToken = CookieUtil.getCookieValue(request,"userToken",true);
            String newToken = request.getParameter("newToken");

            if (StringUtils.isNotBlank(oldToken)){
                token = oldToken;
            }
            if(StringUtils.isNotBlank(newToken)){
                token = newToken;
            }

//            if(neededSuccess == true && StringUtils.isBlank(token)){
//                String returnUrl = request.getRequestURL().toString();
//                response.sendRedirect( "http://passport.gmall.com:8085/index?returnUrl=" + returnUrl);
//                return false;
//            }

            if(StringUtils.isNotBlank(token)){
                //验证token, http的工具

                //通过负载均衡nginx
                String ip = request.getHeader("x-forwarded-for");
                if(StringUtils.isBlank(ip)){
                    ip = request.getRemoteAddr();
                    System.out.println(ip);
                    if(StringUtils.isBlank(ip)){
                        ip = "127.0.0.1";
                    }
                }
//                String doGet = HttpClientUtil.doGet("http://passport.gmall.com:8085/verify?token="+ token + "&currentIp=" + ip);
                String doGet = HttpClientUtil.doGet("http://passport.gmall.com:8085/verify?token="+token+"&currentIp="+ip);
                if(doGet.equals("success")){
                    //刷新用户cookie中的token
                    CookieUtil.setCookie(request,response,"userToken",token,60*30,true);
                    //将用户id和昵称写入
                    Map gmall0725 = JwtUtil.decode("gmall0725",token,ip);
                    request.setAttribute("userId",gmall0725.get("userId"));
                    request.setAttribute("nickName",gmall0725.get("nickName"));
                    return true;
                }
                //验证不通过
//                if(neededSuccess == true){
//                    String returnUrl = request.getRequestURL().toString();
//                    response.sendRedirect("http://passport.gmall.com:8085/index?returnUrl=" + returnUrl);
//                    return false;
//                }else{
//                    return true;
//                }
            }

            //token为空或验证失败
            if(neededSuccess == true){
                String returnUrl = request.getRequestURL().toString();
                response.sendRedirect( "http://passport.gmall.com:8085/index?returnUrl=" + returnUrl);
                return false;
            }

            //token为空且不需要登录
            return true;
        }
}
