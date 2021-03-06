package com.java.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.java.gmall.bean.BaseAttrInfo;
import com.java.gmall.bean.BaseAttrValue;
import com.java.gmall.manage.mapper.BaseAttrInfoMapper;
import com.java.gmall.manage.mapper.BaseAttrValueMapper;
import com.java.gmall.service.BaseAttrService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;

@Service
public class BaseAttrServiceImpl implements BaseAttrService {
    @Autowired
    BaseAttrInfoMapper baseAttrInfoMapper;

    @Autowired
    BaseAttrValueMapper baseAttrValueMapper;

    @Override
    public List<BaseAttrInfo> getAttrList(Integer catalog3Id) {

        BaseAttrInfo baseAttrInfo = new BaseAttrInfo();
        baseAttrInfo.setCatalog3Id(catalog3Id);
        List<BaseAttrInfo> select = baseAttrInfoMapper.select(baseAttrInfo);
        return select;
    }

    @Override
    public void saveAttr(BaseAttrInfo baseAttrInfo) {

//        String id = String.valueOf(baseAttrInfo.getId());
//        System.out.println(baseAttrInfo.getId() + "*************************************************");
//        if(StringUtils.isBlank(id)){
        System.out.println(baseAttrInfo.getId());
            baseAttrInfoMapper.insertSelective(baseAttrInfo);
            Integer attrId = baseAttrInfo.getId();

            List<BaseAttrValue> attrValueList = baseAttrInfo.getAttrValueList();

            for(BaseAttrValue baseAttrValue : attrValueList){
                baseAttrValue.setAttrId(attrId);

                baseAttrValueMapper.insertSelective(baseAttrValue);
            }
//        }else{
//
//        }
    }

    @Override
    public List<BaseAttrInfo> attrInfoList(Integer catalog3Id) {
        BaseAttrInfo baseAttrInfo = new BaseAttrInfo();
        baseAttrInfo.setCatalog3Id(catalog3Id);
        List<BaseAttrInfo> baseAttrInfos = baseAttrInfoMapper.select(baseAttrInfo);

        for (BaseAttrInfo attrInfo : baseAttrInfos) {
            BaseAttrValue baseAttrValue = new BaseAttrValue();
            baseAttrValue.setAttrId(attrInfo.getId());
            List<BaseAttrValue> baseAttrValues = baseAttrValueMapper.select(baseAttrValue);
            attrInfo.setAttrValueList(baseAttrValues);
        }

        return baseAttrInfos;
    }

    @Override
    public List<BaseAttrInfo> getAttrListByValueIds(Set<String> valueIds) {
        String valueIdStr = StringUtils.join(valueIds,",");
        List<BaseAttrInfo> baseAttrInfos = baseAttrValueMapper.getAttrListByValueIds(valueIdStr);
        return baseAttrInfos;
    }
}
