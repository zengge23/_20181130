package com.java.gmall.service;

import com.java.gmall.bean.BaseCatalog1;
import com.java.gmall.bean.BaseCatalog2;
import com.java.gmall.bean.BaseCatalog3;

import java.util.List;

public interface ManageService {

    List<BaseCatalog1> getCatalog1();

    List<BaseCatalog2> getCatalog2(Integer catalog1Id);

    List<BaseCatalog3> getCatalog3(Integer catalog2Id);

}
