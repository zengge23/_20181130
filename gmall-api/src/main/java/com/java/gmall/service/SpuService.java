package com.java.gmall.service;

import com.java.gmall.bean.BaseSaleAttr;
import com.java.gmall.bean.SpuInfo;

import java.util.List;

public interface SpuService {
    List<SpuInfo> getSpuList(Integer catalog3Id);

    List<BaseSaleAttr> baseSaleAttrList();

    void saveSpu(SpuInfo spuInfo);
}