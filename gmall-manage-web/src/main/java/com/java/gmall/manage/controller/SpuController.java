package com.java.gmall.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.java.gmall.bean.BaseSaleAttr;
import com.java.gmall.bean.SpuInfo;
import com.java.gmall.manage.util.GmallUploadUtil;
import com.java.gmall.service.SpuService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
public class SpuController {

    @Reference
    SpuService spuService;

    @ResponseBody
    @RequestMapping("baseSaleAttrList")
    public List<BaseSaleAttr> baseSaleAttrList(){
        List<BaseSaleAttr> baseSaleAttrs = spuService.baseSaleAttrList();
        return baseSaleAttrs;
    }

    @RequestMapping("spuListPage")
    public String spuListPage(){
        return "spuListPage";
    }

    @ResponseBody
    @RequestMapping("getSpuList")
    public List<SpuInfo> getSpuList(Integer catalog3Id){
        List<SpuInfo> spuInfos = spuService.getSpuList(catalog3Id);
        return spuInfos;
    }

    @ResponseBody
    @RequestMapping("saveSpu")
    public String saveSpu(SpuInfo spuInfo){
        spuService.saveSpu(spuInfo);

        //保存spu
        return "success";
    }

    @RequestMapping("fileUpload")
    @ResponseBody
    public String fileUpload(@RequestParam("file") MultipartFile multipartFile){
        String imgUrl = GmallUploadUtil.uploadImage(multipartFile);
        //保存spu
        return imgUrl;
    }

}
