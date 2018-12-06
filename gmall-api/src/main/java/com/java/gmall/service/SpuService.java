package com.java.gmall.service;

import com.java.gmall.bean.SpuInfo;

import java.util.List;

public interface SpuService {
    List<SpuInfo> getSpuList(Integer catalog3Id);
}