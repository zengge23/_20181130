package com.java.gmall.order.mapper;

import com.java.gmall.bean.OrderInfo;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface OrderInfoMapper extends Mapper<OrderInfo> {
    void deleteCheckedCart(@Param("cartIds") String cartIds);
}
