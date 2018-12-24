package com.java.gmall.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.java.gmall.annotations.LoginRequire;
import com.java.gmall.bean.CartInfo;
import com.java.gmall.bean.SkuInfo;
import com.java.gmall.service.CartService;
import com.java.gmall.service.SkuService;
import com.java.gmall.util.CookieUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import tk.mybatis.mapper.util.StringUtil;

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

    @LoginRequire(isNeededSuccess = false)
    @RequestMapping("checkCart")
    public String checkCart(HttpServletRequest request,HttpServletResponse response,String isCheckedFlag,String skuId,ModelMap map){
//        System.out.println("???");
        List<CartInfo> cartInfos = new ArrayList<>();
//        String userId="2";
        String userId = (String)request.getAttribute("userId");
        if(StringUtils.isNotBlank(userId)){
            //改DB
            cartInfos = cartService.getCartListByUserId(userId);
        }else{
            String cartListCookie = CookieUtil.getCookieValue(request,"cartListCookie",true);
            cartInfos = JSON.parseArray(cartListCookie,CartInfo.class);
        }
        //修改购物车状态
        for (CartInfo cartInfo : cartInfos) {
            if(cartInfo.getSkuId().equals(skuId)){
                cartInfo.setIsChecked(isCheckedFlag);
                if(StringUtils.isNotBlank(userId)){
                    //更新DB
                    cartService.updateCart(cartInfo);
                }
            }
        }

        if(StringUtils.isNotBlank(userId)){
            //刷新缓存
            cartService.flushCache(userId);
        }else{
            //改cookie
            CookieUtil.setCookie(request,response,"cartListCookie", JSON.toJSONString(cartInfos),60*60*24,true);
        }

        //刷新最新的列表
        map.put("cartList",cartInfos);
        map.put("totalPrice",getCartSum(cartInfos));
        return "cartListInner";
    }

    @LoginRequire(isNeededSuccess = false)
    @RequestMapping("cartList")
    public String cartList(HttpServletRequest request, ModelMap map){
        //判断用户是否登录
//        String userId = "2";
        String userId = (String)request.getAttribute("userId");
        List<CartInfo> cartInfos = new ArrayList<>();
        if (StringUtils.isBlank(userId)) {
            //从cookie中取数据
            String cartListCookie = CookieUtil.getCookieValue(request,"cartListCookie",true);
            if(StringUtils.isNotBlank(cartListCookie)){
                cartInfos = JSON.parseArray(cartListCookie,CartInfo.class);
            }
        }else{
            //取缓存中的数据
            cartInfos = cartService.getCartListFromCache(userId);

        }
        map.put("cartList",cartInfos);
        BigDecimal totalPrice = getCartSum(cartInfos);
        map.put("totalPrice",totalPrice);
        return "cartList";
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

    @LoginRequire(isNeededSuccess = false)
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
//        String userId = "2";
        String userId = (String)request.getAttribute("userId");
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
            //同步购物车缓存
            cartService.flushCache(userId);
        }else{
            //用户没有登录
            String cartListCookie = CookieUtil.getCookieValue(request,"cartListCookie",true);
            if(StringUtils.isBlank(cartListCookie)){
                //将购物车集合放入cookie
                cartInfos.add(cartInfo);
            }else{
                cartInfos = JSON.parseArray(cartListCookie, CartInfo.class);
                boolean b = if_new_cart(cartInfos,cartInfo);
                if(b){
                    cartInfos.add(cartInfo);
                }else{
                    for (CartInfo info : cartInfos) {
                        if(info.getSkuId().equals(cartInfo.getSkuId())){
                            info.setSkuNum(info.getSkuNum() + num);
                            info.setCartPrice(info.getSkuPrice().multiply(new BigDecimal(info.getSkuNum())));
                        }
                    }
                }
//                for(CartInfo info : cartInfos){
//                    if(skuId.equals(info.getSkuId())){
//                        //更新
//                        info.setSkuNum(info.getSkuNum() + num);
//                        info.setCartPrice(info.getSkuPrice().multiply(new BigDecimal(info.getSkuNum())));
//                    }else{
//                        cartInfos.add(cartInfo);
//                        break;
//                    }
//                }
            }
            CookieUtil.setCookie(request,response,"cartListCookie", JSON.toJSONString(cartInfos),60*60*24,true);
        }

        return "redirect:/cartAddSuccess";
    }

    private boolean if_new_cart(List<CartInfo> cartInfos, CartInfo cartInfo) {
        boolean b = true;

        for(CartInfo info : cartInfos){
            if(cartInfo.getSkuId().equals(info.getSkuId())){
                b = false;
            }
        }
        return b;
    }

    @LoginRequire(isNeededSuccess = false)
    @RequestMapping("cartAddSuccess")
    public String cartAddSuccess(){

        return "success";
    }

//    @LoginRequire(isNeededSuccess = true)
//    @RequestMapping("toTrade")
//    public String toTrade(HttpServletRequest request){
//        String userId = (String)request.getAttribute("userId");
////        String userId = "";
////        if (StringUtils.isNotBlank(userId)){
//            return "tradeTest";
////        }else{
////            return "redirect:http://passport.gmall.com:8085/index";
////        }
//
//    }
}
