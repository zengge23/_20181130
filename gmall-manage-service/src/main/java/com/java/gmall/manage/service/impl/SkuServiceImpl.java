package com.java.gmall.manage.service.impl;

import com.java.gmall.bean.SkuInfo;
import com.java.gmall.manage.mapper.SkuInfoMapper;
import com.java.gmall.service.SkuService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class SkuServiceImpl implements SkuService {

    @Autowired
    SkuInfoMapper skuInfoMapper;

    @Override
    public List<SkuInfo> skuInfoListBySpu(String spuId) {
        return null;
    }
}
