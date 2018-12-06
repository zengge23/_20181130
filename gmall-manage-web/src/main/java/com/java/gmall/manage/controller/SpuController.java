package com.java.gmall.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.java.gmall.bean.SpuInfo;
import com.java.gmall.service.SpuService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class SpuController {

    @Reference
    SpuService spuService;

    @RequestMapping("spuListPage")
    public String spuListPage(){
        return "spuListPage";
    }

    @ResponseBody
    @RequestMapping("getSpuList")
    public List<SpuInfo> getSpuList(Integer catalog3Id){
        List<SpuInfo> spuInfos = spuService.getSpuList(catalog3Id);
        return spuInfos;
    }

}
