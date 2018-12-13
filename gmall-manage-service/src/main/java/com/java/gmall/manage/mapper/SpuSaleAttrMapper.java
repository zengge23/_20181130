package com.java.gmall.manage.mapper;

import com.java.gmall.bean.SpuSaleAttr;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;


public interface SpuSaleAttrMapper extends Mapper<SpuSaleAttr> {
    List<SpuSaleAttr> selectSpuSaleAttrListBySpuId(Map<String,Object> map);
}
