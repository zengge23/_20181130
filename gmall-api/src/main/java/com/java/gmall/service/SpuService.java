package com.java.gmall.service;

import com.java.gmall.bean.BaseSaleAttr;
import com.java.gmall.bean.SpuImage;
import com.java.gmall.bean.SpuInfo;
import com.java.gmall.bean.SpuSaleAttr;

import java.util.List;

public interface SpuService {
    List<SpuInfo> getSpuList(Integer catalog3Id);

    List<BaseSaleAttr> baseSaleAttrList();

    void saveSpu(SpuInfo spuInfo);

    List<SpuSaleAttr> SpuSaleAttrList(String spuId);

    List<SpuImage> spuImageList(String spuId);
}