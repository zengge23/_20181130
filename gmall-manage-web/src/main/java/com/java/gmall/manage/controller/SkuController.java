package com.java.gmall.manage.controller;

import com.java.gmall.bean.SkuInfo;
import com.java.gmall.service.SkuService;
import jdk.nashorn.internal.ir.annotations.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class SkuController {

    @Reference
    SkuService skuService;

    @ResponseBody
    @RequestMapping("skuInfoListBySpu")
    public List<SkuInfo> skuInfoListBySpu(String spuId){

        //根据spuId获取sku列表
        List<SkuInfo> skuInfos = skuService.skuInfoListBySpu(spuId);
        return skuInfos;
    }
}
