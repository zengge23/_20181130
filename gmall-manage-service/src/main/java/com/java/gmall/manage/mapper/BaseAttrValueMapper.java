package com.java.gmall.manage.mapper;

import com.java.gmall.bean.BaseAttrInfo;
import com.java.gmall.bean.BaseAttrValue;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface BaseAttrValueMapper extends Mapper<BaseAttrValue> {
    List<BaseAttrInfo> getAttrListByValueIds(@Param("valueIdStr") String valueIdStr);
}
