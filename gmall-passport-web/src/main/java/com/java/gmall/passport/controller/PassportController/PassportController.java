package com.java.gmall.passport.controller.PassportController;

import com.java.gmall.bean.UserInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PassportController {

    @RequestMapping("index")
    public String index(String returnUrl, ModelMap map){
        map.put("returnUrl",returnUrl);
        return "index";
    }

    @RequestMapping("login")
    @ResponseBody
    public String login(UserInfo userInfo){
        return "token";
    }

    @RequestMapping("verify")
    @ResponseBody
    public String verify(String token){
        return "success";
    }

}

