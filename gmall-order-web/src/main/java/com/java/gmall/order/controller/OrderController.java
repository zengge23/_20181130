package com.java.gmall.order.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.java.gmall.annotations.LoginRequire;
import com.java.gmall.bean.CartInfo;
import com.java.gmall.bean.OrderDetail;
import com.java.gmall.bean.OrderInfo;
import com.java.gmall.bean.UserAddress;
import com.java.gmall.bean.enums.PaymentWay;
import com.java.gmall.service.CartService;
import com.java.gmall.service.OrderService;
import com.java.gmall.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class OrderController {

    @Reference
    UserService userService;

    @Reference
    OrderService orderService;

    @Reference
    CartService cartService;

    @LoginRequire(isNeededSuccess = true)
    @RequestMapping("submitOrder")
    public String submitOrder(HttpServletRequest request,ModelMap map,String tradeCode,String addressId){
        String userId = (String)request.getAttribute("userId");
        String nickName = (String)request.getAttribute("nickName");
        //防止订单重复提交
        boolean b = orderService.checkTradeCode(userId,tradeCode);
        if(b){
            //需要被删除的购物车的集合
            List<String> delList = new ArrayList<>();

            UserAddress userAddress = userService.getAddressListById(addressId);
            //生成订单和订单数据DB  //删除购物车数据
            List<CartInfo> cartInfos = cartService.getCartListByUserId(userId);
            OrderInfo orderInfoForDB = new OrderInfo();
            //外部订单号
            //atguigugmall + 毫秒时间戳字符串 + 订单生成的时间字符串
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String format = sdf.format(new Date());
            orderInfoForDB.setOutTradeNo("atguigugmall" + System.currentTimeMillis() + format);
            orderInfoForDB.setOrderStatus("订单已提交");
//            orderInfoForDB.setTrackingNo("");
            orderInfoForDB.setProcessStatus("订单已提交");
            Calendar calender = Calendar.getInstance();
            //订单过期时间
            calender.add(Calendar.DATE,1);
            orderInfoForDB.setExpireTime(calender.getTime());
            orderInfoForDB.setConsigneeTel(userAddress.getPhoneNum());
            orderInfoForDB.setConsignee(userAddress.getConsignee());
            orderInfoForDB.setCreateTime(new Date());
            orderInfoForDB.setDeliveryAddress(userAddress.getUserAddress());
            orderInfoForDB.setOrderComment("硅谷订单");
            orderInfoForDB.setTotalAmount(getCartSum(cartInfos));
            orderInfoForDB.setPaymentWay(PaymentWay.ONLINE);
            orderInfoForDB.setUserId(userId);
            List<OrderDetail> orderDetails = new ArrayList<>();
            for(CartInfo cartInfo : cartInfos){
                if(cartInfo.getIsChecked().equals("1")){

                    //验价

                    //验库存,远程通过ws调用库存接口


                    OrderDetail orderDetail = new OrderDetail();
                    orderDetail.setSkuName(cartInfo.getSkuName());
                    orderDetail.setImgUrl(cartInfo.getImgUrl());
                    orderDetail.setOrderPrice(cartInfo.getCartPrice());
                    orderDetail.setSkuId(cartInfo.getSkuId());
                    orderDetail.setSkuName(cartInfo.getSkuName());
                    orderDetails.add(orderDetail);

                    delList.add(cartInfo.getId());
                }
            }
            orderInfoForDB.setOrderDetailList(orderDetails);
            orderService.saveOrder(orderInfoForDB);

            //删除购物车数据
//            orderService.deleteCheckedCart(delList);

            //刷新购物车缓存
            cartService.flushCache(userId);

            //重定向到支付页面
            return "redirect:http://payment.gmall.com:8090/index?outTradeNo=" + orderInfoForDB.getOutTradeNo() + "&totalAmount=" + getCartSum(cartInfos);
        }else{
            return "tradeFail";
        }
    }

    @LoginRequire(isNeededSuccess = true)
    @RequestMapping("toTrade")
    public String toTrade(HttpServletRequest request, ModelMap map){
        String userId = (String)request.getAttribute("userId");
        String nickName = (String)request.getAttribute("nickName");

        List<UserAddress> addressListByUserId = userService.getAddressListByUserId(userId);

        List<CartInfo> cartListByUserId = cartService.getCartListByUserId(userId);
//        List<OrderDetail> orderDetails = orderService.getOrderDetailList(cartListByUserId);
        List<OrderDetail> orderDetails = new ArrayList<>();
        for(CartInfo cartInfo : cartListByUserId){
            if(cartInfo.getIsChecked().equals("1")){
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setSkuName(cartInfo.getSkuName());
                orderDetail.setImgUrl(cartInfo.getImgUrl());
                orderDetail.setOrderPrice(cartInfo.getCartPrice());
                orderDetail.setSkuId(cartInfo.getSkuId());
                orderDetail.setSkuName(cartInfo.getSkuName());
                orderDetail.setSkuNum(cartInfo.getSkuNum());
                orderDetails.add(orderDetail);
            }
        }

        map.put("userAddressList",addressListByUserId);
        map.put("nickName",nickName);
        map.put("orderDetailList",orderDetails);
        map.put("totalAmount",getCartSum(cartListByUserId));
        //生成交易码
        String tradeCode = UUID.randomUUID().toString();
        orderService.putTradeCode(tradeCode,userId);
        map.put("tradeCode",tradeCode);
        return "trade";
    }

    private BigDecimal getCartSum(List<CartInfo> cartInfos) {
        BigDecimal sum = new BigDecimal("0");
        //计算购物车中被选中商品的总价格
        for (CartInfo cartInfo : cartInfos) {
            if(cartInfo.getIsChecked().equals("1")){
                sum = sum.add(cartInfo.getCartPrice());
            }
        }
        return sum;
    }

}
