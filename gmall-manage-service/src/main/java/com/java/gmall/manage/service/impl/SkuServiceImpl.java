package com.java.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.java.gmall.bean.BaseAttrInfo;
import com.java.gmall.bean.SkuInfo;
import com.java.gmall.manage.mapper.BaseAttrInfoMapper;
import com.java.gmall.manage.mapper.SkuInfoMapper;
import com.java.gmall.service.SkuService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class SkuServiceImpl implements SkuService {

    @Autowired
    SkuInfoMapper skuInfoMapper;

    @Autowired
    BaseAttrInfoMapper baseAttrInfoMapper;

    @Override
    public List<SkuInfo> skuInfoListBySpu(String spuId) {
        SkuInfo skuInfo = new SkuInfo();
        skuInfo.setSpuId(spuId);
        List<SkuInfo> skuInfoList = skuInfoMapper.select(skuInfo);
        return skuInfoList;
    }

//    @Override
//    public List<BaseAttrInfo> attrInfoList(Integer catalog3Id) {
//        BaseAttrInfo baseAttrInfo = new BaseAttrInfo();
//        baseAttrInfo.setCatalog3Id(catalog3Id);
//        List<BaseAttrInfo> baseAttrInfos = baseAttrInfoMapper.select(baseAttrInfo);
//        return baseAttrInfos;
//    }
}
