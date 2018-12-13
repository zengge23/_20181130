package com.java.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.java.gmall.bean.*;
import com.java.gmall.manage.mapper.*;
import com.java.gmall.service.SkuService;
import com.java.gmall.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

import java.util.List;

@Service
public class SkuServiceImpl implements SkuService {

    @Autowired
    SkuInfoMapper skuInfoMapper;

    @Autowired
    SkuImageMapper skuImageMapper;

    @Autowired
    SkuAttrValueMapper skuAttrValueMapper;

    @Autowired
    SkuSaleAttrValueMapper skuSaleAttrValueMapper;

    @Autowired
    BaseAttrInfoMapper baseAttrInfoMapper;

    @Autowired
    RedisUtil redisUtil;

    @Override
    public List<SkuInfo> skuInfoListBySpu(String spuId) {
        SkuInfo skuInfo = new SkuInfo();
        skuInfo.setSpuId(spuId);
        List<SkuInfo> skuInfoList = skuInfoMapper.select(skuInfo);
        return skuInfoList;
    }

    @Override
    public void saveSku(SkuInfo skuInfo) {
        skuInfoMapper.insertSelective(skuInfo);
        String skuId = skuInfo.getId();

        //保存sku图片信息
        List<SkuImage> skuImageList = skuInfo.getSkuImageList();
        for(SkuImage skuImage : skuImageList){
            skuImage.setSkuId(skuId);
            skuImageMapper.insertSelective(skuImage);
        }

        //保存sku平台属性
        List<SkuAttrValue> skuAttrValueList = skuInfo.getSkuAttrValueList();
        for(SkuAttrValue skuAttrValue : skuAttrValueList){
            skuAttrValue.setSkuId(skuId);
            skuAttrValueMapper.insertSelective(skuAttrValue);
        }

        //保存sku的销售属性
        List<SkuSaleAttrValue> skuSaleAttrValueList = skuInfo.getSkuSaleAttrValueList();
        for(SkuSaleAttrValue skuSaleAttrValue : skuSaleAttrValueList){
            skuSaleAttrValue.setSkuId(skuId);
            skuSaleAttrValueMapper.insertSelective(skuSaleAttrValue);
        }
    }

    @Override
    public SkuInfo getSkuById(String skuId) {
        SkuInfo skuInfo = null;
        //查询缓存
        Jedis jedis = redisUtil.getJedis();
        String skuKey = jedis.get("sku"+skuId+":info");
        String cacheJson = jedis.get(skuKey);
        if(StringUtils.isBlank(cacheJson)){
            //缓存查询未果查询数据库
            skuInfo = getSkuByIdFromDb(skuId);

            //将数据库的信息同步到缓存
            jedis.set("sku:" + skuId +":Info", JSON.toJSONString(skuInfo));
        }else{
            skuInfo = JSON.parseObject(cacheJson, SkuInfo.class);
        }

        return skuInfo;
    }

    private SkuInfo getSkuByIdFromDb(String skuId) {
        SkuInfo skuInfo = skuInfoMapper.selectByPrimaryKey(skuId);

        SkuImage skuImage = new SkuImage();
        skuImage.setSkuId(skuId);
        List<SkuImage> skuImages = skuImageMapper.select(skuImage);
        skuInfo.setSkuImageList(skuImages);
        return skuInfo;
    }

    @Override
    public List<SkuInfo> skuSaleAttrValueListBySpu(String spuId) {
        List<SkuInfo> skuInfos = skuSaleAttrValueMapper.selectSkuSaleAttrValueListBySpu(spuId);
        return skuInfos;
    }

//    @Override
//    public List<BaseAttrInfo> attrInfoList(Integer catalog3Id) {
//        BaseAttrInfo baseAttrInfo = new BaseAttrInfo();
//        baseAttrInfo.setCatalog3Id(catalog3Id);
//        List<BaseAttrInfo> baseAttrInfos = baseAttrInfoMapper.select(baseAttrInfo);
//        return baseAttrInfos;
//    }
}
