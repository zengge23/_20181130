package com.java.gmall.item.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ItemController {

    @RequestMapping("{skuId}.html")
    public String item(@PathVariable String skuId){
        return "item";
    }

    @RequestMapping("test")
    public String test(ModelMap map){
        //取值
        map.put("hello","hello thymaleaf");

        return "test";
    }
}
