package com.java.gmall.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.java.gmall.bean.BaseCatalog1;
import com.java.gmall.bean.BaseCatalog2;
import com.java.gmall.bean.BaseCatalog3;
import com.java.gmall.service.ManageService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ManageController {

    @Reference
    ManageService manageService;

    @RequestMapping("index")
    public String index(){
        return "index";
    }

    @ResponseBody
    @RequestMapping("getCatalog1")
    public List<BaseCatalog1> getCatalog1(){
        List<BaseCatalog1> catalog1s = manageService.getCatalog1();
        //调用后台查询一级分类的集合
        return catalog1s;
    }


    @ResponseBody
    @RequestMapping("getCatalog2")
    public List<BaseCatalog2> getCatalog2(Integer catalog1Id){
        List<BaseCatalog2> catalog2s = manageService.getCatalog2(catalog1Id);
        //调用后台查询一级分类的集合
        return catalog2s;
    }

    @ResponseBody
    @RequestMapping("getCatalog3")
    public List<BaseCatalog3> getCatalog3(Integer catalog2Id){
        List<BaseCatalog3> catalog3s = manageService.getCatalog3(catalog2Id);
        //调用后台查询一级分类的集合
        return catalog3s;
    }

}
