package com.java.gmall.list.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.java.gmall.bean.SkuLsInfo;
import com.java.gmall.bean.SkuLsParam;
import com.java.gmall.service.ListService;
import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Service
public class ListServiceimpl implements ListService {

    @Autowired
    JestClient jestClient;

    @Override
    public List<SkuLsInfo> search(SkuLsParam skuLsParam) {

        String dsl = getMyDsl(skuLsParam);
        System.out.println(dsl);

        Search build = new Search.Builder(dsl).addIndex("gmall0725").addType("SkuLsInfo").build();

        SearchResult execute = null;
        try {
            execute = jestClient.execute(build);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<SearchResult.Hit<SkuLsInfo,Void>> hits = execute.getHits(SkuLsInfo.class);
        List<SkuLsInfo> skuLsInfos = new ArrayList<>();
        for(SearchResult.Hit<SkuLsInfo,Void> hit : hits){
            SkuLsInfo source = hit.source;
            skuLsInfos.add(source);
        }
        return skuLsInfos;
    }

    private String getMyDsl(SkuLsParam skuLsParam) {

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //bool
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        //must
        String keyword = skuLsParam.getKeyword();
        if(StringUtils.isNotBlank(keyword)){
            MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("skuName",keyword);
            boolQueryBuilder.must(matchQueryBuilder);
        }

        //filter
        String[] valueIds = skuLsParam.getValueId();

        if(null!=valueIds&&valueIds.length>0){
            for (String valueId : valueIds) {
                TermQueryBuilder termQueryBuilder = new TermQueryBuilder("skuAttrValueList.valueId", valueId);
                boolQueryBuilder.filter(termQueryBuilder);
            }
        }

        searchSourceBuilder.query(boolQueryBuilder);
        //分页
        searchSourceBuilder.size(20);
        searchSourceBuilder.from(0);

        return searchSourceBuilder.toString();
    }
}
