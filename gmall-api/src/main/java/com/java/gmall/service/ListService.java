package com.java.gmall.service;

import com.java.gmall.bean.SkuLsInfo;
import com.java.gmall.bean.SkuLsParam;

import java.util.List;

public interface ListService {

    List<SkuLsInfo> search(SkuLsParam skuLsParam);
}
