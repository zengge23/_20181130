package com.java.gmall.item.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.java.gmall.bean.SkuInfo;
import com.java.gmall.bean.SkuSaleAttrValue;
import com.java.gmall.bean.SpuSaleAttr;
import com.java.gmall.service.SkuService;
import com.java.gmall.service.SpuService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
public class ItemController {

    @Reference
    SkuService skuService;

    @Reference
    SpuService spuService;

    @RequestMapping("{skuId}.html")
    public String item(@PathVariable String skuId,ModelMap map){
        //查询当前sku的详情
        SkuInfo skuInfo = skuService.getSkuById(skuId);
        map.put("skuInfo",skuInfo);
        //通过skuId获得spuId
        String spuId = skuInfo.getSpuId();

        //根据spuId查询销售属性集合
        List<SpuSaleAttr> spuSaleAttrs = spuService.SpuSaleAttrListBySpuId(spuId,skuId);
        map.put("spuSaleAttrListCheckBySku",spuSaleAttrs);

        //根据spuId制作页面销售属性的hash表
        //销售属性集合:skuId
        List<SkuInfo> skuInfos = skuService.skuSaleAttrValueListBySpu(spuId);
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        for(SkuInfo info : skuInfos){
            String skuSaleAttrValueIdKey = "";
            List<SkuSaleAttrValue> skuSaleAttrValueList = info.getSkuSaleAttrValueList();
            for(SkuSaleAttrValue SkuSaleAttrValue : skuSaleAttrValueList) {
                skuSaleAttrValueIdKey = skuSaleAttrValueIdKey + "|" + SkuSaleAttrValue.getSaleAttrValueId();
            }
            String skuIdValue = info.getId();
            stringStringHashMap.put(skuSaleAttrValueIdKey,skuIdValue);
        }
        String s = JSON.toJSONString(stringStringHashMap);
        map.put("valuesSkuJson",s);
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
