package com.java.gmall.list.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.java.gmall.bean.SkuLsInfo;
import com.java.gmall.bean.SkuLsParam;
import com.java.gmall.service.ListService;
import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.core.search.aggregation.Aggregation;
import io.searchbox.core.search.aggregation.MetricAggregation;
import io.searchbox.core.search.aggregation.TermsAggregation;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.highlight.HighlightBuilder;
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

            //替换高亮字段
            if(null != hit.highlight){
                List<String> skuName = hit.highlight.get("skuName");
                String s = skuName.get(0);
                source.setSkuName(s);
            }
            skuLsInfos.add(source);
        }
        //取聚合对象
        MetricAggregation aggregations = execute.getAggregations();
        //声明聚合的valueId的集合
        List<String> attrValueIdList = new ArrayList<>();
        TermsAggregation groupby_attr = aggregations.getTermsAggregation("groupby_attr");
        if(groupby_attr != null){
            List<TermsAggregation.Entry> buckets = groupby_attr.getBuckets();
            for(TermsAggregation.Entry bucket : buckets){
                //去聚合函数中的valueId
                attrValueIdList.add(bucket.getKey());//聚合的字段值,属性值id
                Long count = bucket.getCount();//聚合字段值出现的次数
            }
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

            //高亮 highlight
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.preTags("<span style='color:red;font-weight:bolder;'>");
            highlightBuilder.field("skuName");
            highlightBuilder.postTags("</span>");
            searchSourceBuilder.highlight(highlightBuilder);
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

        //加聚合函数
        TermsBuilder groupby_attr = AggregationBuilders.terms("groupby_attr").field("skuAttrValueList.valueId");
        searchSourceBuilder.aggregation(groupby_attr);

        return searchSourceBuilder.toString();
    }
}
