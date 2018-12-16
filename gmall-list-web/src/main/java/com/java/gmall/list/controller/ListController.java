package com.java.gmall.list.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.java.gmall.bean.SkuLsInfo;
import com.java.gmall.bean.SkuLsParam;
import com.java.gmall.service.ListService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class ListController {

    @Reference
    ListService listService;

    @RequestMapping("list.html")
    public String list(SkuLsParam skuLsParam, ModelMap map){
        //调用商品列表的搜索服务
        List<SkuLsInfo> skuLsInfos = listService.search(skuLsParam);
        map.put("skuLsInfoList",skuLsInfos);
        return "list";
    }

    @RequestMapping("index")
    public String index(){
        return "index";
    }

}
