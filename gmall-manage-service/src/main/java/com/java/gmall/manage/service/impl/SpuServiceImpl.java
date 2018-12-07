package com.java.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.java.gmall.bean.BaseSaleAttr;
import com.java.gmall.bean.SpuInfo;
import com.java.gmall.bean.SpuSaleAttr;
import com.java.gmall.bean.SpuSaleAttrValue;
import com.java.gmall.manage.mapper.BaseSaleAttrMapper;
import com.java.gmall.manage.mapper.SpuInfoMapper;
import com.java.gmall.manage.mapper.SpuSaleAttrMapper;
import com.java.gmall.manage.mapper.SpuSaleAttrValueMapper;
import com.java.gmall.service.SpuService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class SpuServiceImpl implements SpuService {

    @Autowired
    SpuInfoMapper spuInfoMapper;

    @Autowired
    BaseSaleAttrMapper baseSaleAttrMapper;

    @Autowired
    SpuSaleAttrMapper spuSaleAttrMapper;

    @Autowired
    SpuSaleAttrValueMapper spuSaleAttrValueMapper;

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
        //保存spu信息
        spuInfoMapper.insertSelective(spuInfo);
        String spuId = spuInfo.getId();
        //保存spu图片信息

        //保存spu的销售属性
        List<SpuSaleAttr> spuSaleAttrList = spuInfo.getSpuSaleAttrList();
        for(SpuSaleAttr spuSaleAttr : spuSaleAttrList){
            //销售属性
            spuSaleAttr.setSpuId(spuId);
            spuSaleAttrMapper.insertSelective(spuSaleAttr);

            List<SpuSaleAttrValue> spuSaleAttrValueList = spuSaleAttr.getSpuSaleAttrValueList();
            for(SpuSaleAttrValue spuSaleAttrValue : spuSaleAttrValueList){
                //销售属性值
                spuSaleAttrValue.setSpuId(spuId);
                spuSaleAttrValueMapper.insertSelective(spuSaleAttrValue);
            }
        }
    }
}
