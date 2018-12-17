package com.java.gmall.service;

import com.java.gmall.bean.BaseAttrInfo;

import java.util.List;
import java.util.Set;

public interface BaseAttrService {
    List<BaseAttrInfo> getAttrList(Integer catalog3Id);

    void saveAttr(BaseAttrInfo baseAttrInfo);

    List<BaseAttrInfo> attrInfoList(Integer catalog3Id);

    List<BaseAttrInfo> getAttrListByValueIds(Set<String> valueIds);
}
