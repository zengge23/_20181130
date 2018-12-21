package com.java.gmall.cart.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.java.gmall.bean.CartInfo;
import com.java.gmall.cart.mapper.CartInfoMapper;
import com.java.gmall.service.CartService;
import com.java.gmall.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    CartInfoMapper cartInfoMapper;

    @Autowired
    RedisUtil redisUtil;

    @Override
    public CartInfo selectCartExists(CartInfo cartInfo) {
        CartInfo cartInfo1 = new CartInfo();
        cartInfo1.setUserId(cartInfo.getUserId());
        cartInfo1.setSkuId(cartInfo.getSkuId());
        CartInfo cartInfo2 = cartInfoMapper.selectOne(cartInfo1);
        return cartInfo2;
    }

    @Override
    public void addCart(CartInfo cartInfo) {
        cartInfoMapper.insertSelective(cartInfo);
    }

    @Override
    public void updateCart(CartInfo cartInfoDB) {
        Example e = new Example(CartInfo.class);
        e.createCriteria().andEqualTo("userId",cartInfoDB.getUserId()).andEqualTo("skuId",cartInfoDB.getSkuId());
        cartInfoMapper.updateByExampleSelective(cartInfoDB, e);
    }

    @Override
    public void flushCache(String userId) {
        Jedis jedis = redisUtil.getJedis();
        CartInfo cartInfo = new CartInfo();
        cartInfo.setUserId(userId);
        List<CartInfo> cartInfos = cartInfoMapper.select(cartInfo);

        jedis.del("user:" + userId + ":cart");

        Map<String, String> map = new HashMap<>();
        for (CartInfo info : cartInfos) {
            map.put(info.getSkuId(),JSON.toJSONString(info));
        }
        jedis.hmset("user:" + userId + ":cart",map);
        jedis.close();
    }

    @Override
    public List<CartInfo> getCartListFromCache(String userId) {
        List<CartInfo> cartInfos = new ArrayList<>();
        Jedis jedis = redisUtil.getJedis();
        List<String> hvals = jedis.hvals("user:" + userId + ":cart");
        if(hvals != null && hvals.size() > 0){
            for(String hval : hvals){
                CartInfo cartInfo = new CartInfo();
                cartInfo = JSON.parseObject(hval,CartInfo.class);
                cartInfos.add(cartInfo);
            }
        }
        jedis.close();
        return cartInfos;
    }

    @Override
    public List<CartInfo> getCartListByUserId(String userId) {
        CartInfo cartInfo = new CartInfo();
        cartInfo.setUserId(userId);
        List<CartInfo> cartInfos = cartInfoMapper.select(cartInfo);
        return cartInfos;
    }

    @Override
    public void mergCart(List<CartInfo> cartListCookie, String userId) {
        CartInfo cartInfo = new CartInfo();
        cartInfo.setUserId(userId);
        List<CartInfo> cartListDB = cartInfoMapper.select(cartInfo);
        for(CartInfo cartInfoCookie : cartListCookie){
            boolean b = if_new_cart(cartListCookie,cartInfoCookie);
            if(b){
                //在db中新增购物车
                cartInfoCookie.setUserId(userId);
                cartInfoMapper.insertSelective(cartInfoCookie);
            }else{
                //更新db
                for(CartInfo cartInfoDB : cartListDB){
                    if(cartInfoDB.getSkuId().equals(cartInfoCookie.getSkuId())){
                        cartInfoDB.setSkuNum(cartInfoDB.getSkuNum() + cartInfoCookie.getSkuNum());
                        cartInfoDB.setCartPrice(cartInfoDB.getSkuPrice().multiply(new BigDecimal(cartInfoDB.getSkuNum())));
                        cartInfoMapper.updateByPrimaryKeySelective(cartInfoDB);


                    }
                }
            }
        }
        flushCache(userId);
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
}
