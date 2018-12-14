package com.java.gmall.service;

import com.java.gmall.bean.BaseAttrInfo;
import com.java.gmall.bean.SkuInfo;

import java.util.List;

public interface SkuService {


    List<SkuInfo> skuInfoListBySpu(String spuId);

    void saveSku(SkuInfo skuInfo);

    SkuInfo getSkuById(String skuId);

    List<SkuInfo> skuSaleAttrValueListBySpu(String spuId);

    List<SkuInfo> getMySkuInfo(String catalog3Id);

//    List<BaseAttrInfo> attrInfoList(Integer catalog3Id);
}
