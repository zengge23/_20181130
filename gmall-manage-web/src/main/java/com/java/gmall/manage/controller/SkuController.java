package com.java.gmall.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.java.gmall.bean.BaseAttrInfo;
import com.java.gmall.bean.SkuInfo;
import com.java.gmall.bean.SpuImage;
import com.java.gmall.bean.SpuSaleAttr;
import com.java.gmall.service.BaseAttrService;
import com.java.gmall.service.SkuService;
import com.java.gmall.service.SpuService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class SkuController {

    @Reference
    SkuService skuService;

    @Reference
    BaseAttrService baseAttrService;

    @Reference
    SpuService spuService;

    @RequestMapping("saveSku")
    @ResponseBody
    public String saveSku(SkuInfo skuInfo){

        //保存sku
        skuService.saveSku(skuInfo);

        return "success";
    }

    @ResponseBody
    @RequestMapping("skuInfoListBySpu")
    public List<SkuInfo> skuInfoListBySpu(String spuId){
        //根据spuId获取sku列表
        List<SkuInfo> skuInfos = skuService.skuInfoListBySpu(spuId);
        return skuInfos;
    }

    @ResponseBody
    @RequestMapping("attrInfoList")
    public List<BaseAttrInfo> attrInfoList(Integer catalog3Id){
        List<BaseAttrInfo> baseAttrInfos = baseAttrService.attrInfoList(catalog3Id);

        return baseAttrInfos;
    }

    @ResponseBody
    @RequestMapping("spuSaleAttrList")
    public List<SpuSaleAttr> spuSaleAttrList(String spuId){
        List<SpuSaleAttr> SpuSaleAttrs = spuService.SpuSaleAttrList(spuId);

        return SpuSaleAttrs;
    }

    @ResponseBody
    @RequestMapping("spuImageList")
    public List<SpuImage> spuImageList(String spuId){
        List<SpuImage> spuImages = spuService.spuImageList(spuId);

        return spuImages;
    }
}
