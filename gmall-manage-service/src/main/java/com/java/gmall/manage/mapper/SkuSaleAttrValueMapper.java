package com.java.gmall.manage.mapper;

import com.java.gmall.bean.SkuInfo;
import com.java.gmall.bean.SkuSaleAttrValue;
import tk.mybatis.mapper.common.Mapper;
;import java.util.List;

public interface SkuSaleAttrValueMapper extends Mapper<SkuSaleAttrValue> {
    List<SkuInfo> selectSkuSaleAttrValueListBySpu(String spuId);
}
