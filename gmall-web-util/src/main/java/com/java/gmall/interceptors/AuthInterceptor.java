package com.java.gmall.interceptors;

import com.java.gmall.annotations.LoginRequire;
import com.java.gmall.util.CookieUtil;
import com.java.gmall.util.HttpClientUtil;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.groovy.util.StringUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthInterceptor extends HandlerInterceptorAdapter{

        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//            String newToken = request.getParameter("newToken");
//            if (newToken != null && newToken.length() > 0) {
//                CookieUtil.setCookie(request, response, "token", newToken, WebConst.cookieExpire, false);
//            }
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

            if(neededSuccess == true && StringUtils.isNotBlank(token)){
                response.sendRedirect( "redirect:http://passport.gmall.com:8085/index");
                return false;
            }

            //验证token, http的工具
            String doGet = HttpClientUtil.doGet("http://passport.gmall.com:8085/verify");

            return true;
        }
}
