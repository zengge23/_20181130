package com.java.gmall.list.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.java.gmall.bean.BaseAttrInfo;
import com.java.gmall.bean.SkuLsAttrValue;
import com.java.gmall.bean.SkuLsInfo;
import com.java.gmall.bean.SkuLsParam;
import com.java.gmall.service.BaseAttrService;
import com.java.gmall.service.ListService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class ListController {

    @Reference
    ListService listService;

    @Reference
    BaseAttrService baseAttrService;

    @RequestMapping("list.html")
    public String list(SkuLsParam skuLsParam, ModelMap map){
        //调用商品列表的搜索服务
        List<SkuLsInfo> skuLsInfos = listService.search(skuLsParam);
        map.put("skuLsInfoList",skuLsInfos);

        //sku列表结果中包含的属性列表
        Set<String> valueIds = new HashSet<>();
        for(SkuLsInfo skuLsInfo : skuLsInfos){
            List<SkuLsAttrValue> skuAttrValueList = skuLsInfo.getSkuAttrValueList();
            for(SkuLsAttrValue skuLsAttrValue : skuAttrValueList){
                String valueId = skuLsAttrValue.getValueId();
                valueIds.add(valueId);
            }
        }
        List<BaseAttrInfo> baseAttrInfos = baseAttrService.getAttrListByValueIds(valueIds);
        map.put("attrList",baseAttrInfos);
        map.put("urlParam","20188102");
        return "list";
    }

    @RequestMapping("index")
    public String index(){
        return "index";
    }

}
