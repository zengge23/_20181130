package com.java.gmall.cart.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CartController {

    @RequestMapping("addToCart")
    public String addToCart(String skuId, String num){

        return "redirect:/cartAddSuccess";
    }

    @RequestMapping("cartAddSuccess")
    public String cartAddSuccess(){

        return "success";
    }
}
