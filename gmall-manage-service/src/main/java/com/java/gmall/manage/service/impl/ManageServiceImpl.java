package com.java.gmall.manage.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.java.gmall.bean.BaseCatalog1;
import com.java.gmall.bean.BaseCatalog2;
import com.java.gmall.bean.BaseCatalog3;
import com.java.gmall.manage.mapper.BaseCatalog1Mapper;
import com.java.gmall.manage.mapper.BaseCatalog2Mapper;
import com.java.gmall.manage.mapper.BaseCatalog3Mapper;
import com.java.gmall.service.ManageService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class ManageServiceImpl implements ManageService {

    @Autowired
    BaseCatalog1Mapper baseCatalog1Mapper;
    @Autowired
    BaseCatalog2Mapper baseCatalog2Mapper;
    @Autowired
    BaseCatalog3Mapper baseCatalog3Mapper;

    @Override
    public List<BaseCatalog1> getCatalog1() {

        return baseCatalog1Mapper.selectAll();
    }

    @Override
    public List<BaseCatalog2> getCatalog2(Integer catalog1Id) {
        BaseCatalog2 baseCatalog2 = new BaseCatalog2();
        baseCatalog2.setCatalog1Id(catalog1Id);
        List<BaseCatalog2> select = baseCatalog2Mapper.select(baseCatalog2);

        return select;
    }

    @Override
    public List<BaseCatalog3> getCatalog3(Integer catalog2Id) {
        BaseCatalog3 baseCatalog3 = new BaseCatalog3();
        baseCatalog3.setCatalog2Id(catalog2Id);
        List<BaseCatalog3> select = baseCatalog3Mapper.select(baseCatalog3);

        return select;
    }
}
