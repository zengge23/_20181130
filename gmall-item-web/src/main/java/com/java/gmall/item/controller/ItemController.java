package com.java.gmall.item.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

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
        //判断
        map.put("num","1");
        //循环
        List<String> list = new ArrayList<>();
        for(int i=0; i<5; i++){
            list.add("数据"+1);
        }
        map.put("list",list);
        map.put("name","毒液");

        return "test";
    }
}
