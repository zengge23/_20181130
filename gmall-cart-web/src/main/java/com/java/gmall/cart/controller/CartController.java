package com.java.gmall.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.java.gmall.bean.CartInfo;
import com.java.gmall.bean.SkuInfo;
import com.java.gmall.service.CartService;
import com.java.gmall.service.SkuService;
import com.java.gmall.util.CookieUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CartController {

    @Reference
    SkuService skuService;

    @Reference
    CartService cartService;

    @RequestMapping("addToCart")
    public String addToCart(HttpServletResponse response,HttpServletRequest request, String skuId, Integer num){
        //购物车添加逻辑
        SkuInfo skuInfo = skuService.getSkuById(skuId);
        CartInfo cartInfo = new CartInfo();
        cartInfo.setCartPrice(skuInfo.getPrice());
        cartInfo.setSkuNum(num);
        cartInfo.setSkuPrice(skuInfo.getPrice());
        cartInfo.setIsChecked("1");;
        cartInfo.setSkuName(skuInfo.getSkuName());
        cartInfo.setSkuId(skuInfo.getId());
        cartInfo.setImgUrl(skuInfo.getSkuDefaultImg());
        //用户ID
        String userId = "";
        List<CartInfo> cartInfos = new ArrayList<>();
        if(StringUtils.isNotBlank(userId)){
            //用户已经登录
            cartInfo.setUserId("2");
            CartInfo cartInfoDB = cartService.selectCartExists(cartInfo);
            if(cartInfoDB == null){
                //DB中没有,需要进行插入
                cartService.addCart(cartInfo);
            }else{
                //DB中有,进行更新
                cartInfoDB.setSkuNum(cartInfoDB.getSkuNum() + num);
                cartInfoDB.setCartPrice(cartInfoDB.getSkuPrice().multiply(new BigDecimal(cartInfoDB.getSkuNum())));
                cartService.updateCart(cartInfoDB);
            }
            cartService.flushCache(userId);
        }else{
            //用户没有登录
            String cartListCookie = CookieUtil.getCookieValue(request,"cartListCookie",true);
            if(StringUtils.isBlank(cartListCookie)){
                //将购物车集合放入cookie
                cartInfos.add(cartInfo);
            }else{
                cartInfos = JSON.parseArray(cartListCookie, CartInfo.class);
                for(CartInfo info : cartInfos){
                    if(skuId.equals(info.getSkuId())){
                        //更新
                        info.setSkuNum(info.getSkuNum() + num);
                        info.setCartPrice(info.getSkuPrice().multiply(new BigDecimal(info.getSkuNum())));
                    }else{
                        cartInfos.add(cartInfo);
                    }
                }
            }
            CookieUtil.setCookie(request,response,"cartListCookie", JSON.toJSONString(cartInfos),60*60*24,true);
        }

        return "redirect:/cartAddSuccess";
    }

    @RequestMapping("cartAddSuccess")
    public String cartAddSuccess(){

        return "success";
    }
}
