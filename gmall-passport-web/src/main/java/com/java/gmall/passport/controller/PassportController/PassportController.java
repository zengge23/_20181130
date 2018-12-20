package com.java.gmall.passport.controller.PassportController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PassportController {

    @RequestMapping("index")
    public String index(){
        return "index";
    }

    @RequestMapping("verify")
    public String verify(){
        return "success";
    }

}

