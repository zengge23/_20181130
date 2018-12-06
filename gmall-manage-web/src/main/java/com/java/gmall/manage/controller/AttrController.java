package com.java.gmall.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.java.gmall.bean.BaseAttrInfo;
import com.java.gmall.bean.SpuInfo;
import com.java.gmall.service.BaseAttrService;

import com.java.gmall.service.SpuService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Controller
public class AttrController {

    @Reference
    BaseAttrService baseAttrService;

    @Reference
    SpuService spuService;

    @RequestMapping("attrListPage")
    public String attrListPage(){
        return "attrListPage";
    }

    @ResponseBody
    @RequestMapping("getAttrList")
    public List<BaseAttrInfo> getAttrList(Integer catalog3Id){
        List<BaseAttrInfo> baseAttrInfos = baseAttrService.getAttrList(catalog3Id);
        //调用后台查询一级分类的集合
        return baseAttrInfos;
    }

    @ResponseBody
    @RequestMapping("saveAttr")
    public String saveAttr(BaseAttrInfo baseAttrInfo){
        baseAttrService.saveAttr(baseAttrInfo);
        //保存属性信息
        return "seccess";
    }

}
