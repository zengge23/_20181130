package com.java.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.java.gmall.bean.BaseSaleAttr;
import com.java.gmall.bean.SpuInfo;
import com.java.gmall.manage.mapper.BaseSaleAttrMapper;
import com.java.gmall.manage.mapper.SpuInfoMapper;
import com.java.gmall.service.SpuService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class SpuServiceImpl implements SpuService {

    @Autowired
    SpuInfoMapper spuInfoMapper;

    @Autowired
    BaseSaleAttrMapper baseSaleAttrMapper;

    @Override
    public List<SpuInfo> getSpuList(Integer catalog3Id) {
        SpuInfo spuInfo = new SpuInfo();
        spuInfo.setCatalog3Id(catalog3Id);
        List<SpuInfo> select = spuInfoMapper.select(spuInfo);
        return select;
    }

    @Override
    public List<BaseSaleAttr> baseSaleAttrList() {
        return baseSaleAttrMapper.selectAll();
    }

    @Override
    public void saveSpu(SpuInfo spuInfo) {

    }
}
