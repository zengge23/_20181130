package com.java.gmall.service;

import com.java.gmall.bean.BaseAttrInfo;

import java.util.List;

public interface BaseAttrService {
    List<BaseAttrInfo> getAttrList(Integer catalog3Id);

    void saveAttr(BaseAttrInfo baseAttrInfo);

    List<BaseAttrInfo> attrInfoList(Integer catalog3Id);
}
