package com.java.gmall.list.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.java.gmall.bean.*;
import com.java.gmall.service.BaseAttrService;
import com.java.gmall.service.ListService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import tk.mybatis.mapper.util.StringUtil;

import java.util.HashSet;
import java.util.Iterator;
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
        //根据sku列表中的属性值查询出的属性列表集合
        List<BaseAttrInfo> baseAttrInfos = baseAttrService.getAttrListByValueIds(valueIds);
        //删除已经被选择过的属性列表
        String[] delValueIds = skuLsParam.getValueId();

        Iterator<BaseAttrInfo> iterator = baseAttrInfos.iterator();
        if(null != delValueIds && delValueIds.length > 0){
            while(iterator.hasNext()){
                BaseAttrInfo baseAttrInfo = iterator.next();

                List<BaseAttrValue> attrValueList = baseAttrInfo.getAttrValueList();
                for(BaseAttrValue baseAttrValue : attrValueList){
                    String valueId = baseAttrValue.getId();
                    for(String delValueId : delValueIds){
                        if(delValueId.equals(valueId)){
                            iterator.remove();
                        }
                    }
                }

            }
        }

        map.put("attrList",baseAttrInfos);

        //上次请求的参数列表
        String urlParam = getMyUrlParam(skuLsParam);
        map.put("urlParam",urlParam);
        map.put("keyword",skuLsInfos);

        //面包屑

        return "list";
    }

    private String getMyUrlParam(SkuLsParam skuLsParam) {
        String urlParam = "";
        String keyword = skuLsParam.getKeyword();
        String catalog3Id = skuLsParam.getCatalog3Id();
        String[] valueIds = skuLsParam.getValueId();

        if(StringUtils.isNotBlank(catalog3Id)){
            if(StringUtils.isNotBlank(urlParam)){
                urlParam = urlParam = "&";
            }
            urlParam = urlParam + "catalog3Id=" + catalog3Id;
        }

        if(StringUtils.isNotBlank(keyword)){
            if(StringUtils.isNotBlank(urlParam)){
                urlParam = urlParam = "&";
            }
            urlParam = urlParam + "keyword=" + keyword;
        }
        if(null != valueIds){
            for(String valueId : valueIds){
                urlParam = urlParam + "&valueId=" + valueId;
            }
        }
        return urlParam;
    }

    @RequestMapping("index")
    public String index(){
        return "index";
    }

}
