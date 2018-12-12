package com.java.gmall.service;

import com.java.gmall.bean.BaseAttrInfo;
import com.java.gmall.bean.SkuInfo;

import java.util.List;

public interface SkuService {


    List<SkuInfo> skuInfoListBySpu(String spuId);

    void saveSku(SkuInfo skuInfo);

//    List<BaseAttrInfo> attrInfoList(Integer catalog3Id);
}
