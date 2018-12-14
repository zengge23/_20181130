package com.java.gmall.list;

import com.alibaba.dubbo.config.annotation.Reference;
import com.java.gmall.bean.SkuInfo;
import com.java.gmall.bean.SkuLsInfo;
import com.java.gmall.service.SkuService;
import io.searchbox.client.JestClient;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GmallListServiceApplicationTests {

    @Autowired
    JestClient jestClient;

    @Reference
    SkuService skuService;

    @Test
    public void contextLoads() throws Exception {

        String dsl = getMyDsl();
        System.out.println(dsl);
//        List<SkuInfo> skuInfos = skuService.getMySkuInfo("61");
//        System.out.println(skuInfos);
//
//        List<SkuLsInfo> skuLsInfos = new ArrayList<>();
//        for(SkuInfo skuInfo : skuInfos){
//            SkuLsInfo skuLsInfo = new SkuLsInfo();
//            BeanUtils.copyProperties(skuInfo,skuLsInfo);
//
//            skuLsInfos.add(skuLsInfo);
//        }
//        for(SkuLsInfo skuLsInfo : skuLsInfos){
//           Index build = new Index.Builder(skuLsInfo).index("gmall0725").type("SkuLsInfo").id(skuLsInfo.getId()).build();
//           jestClient.execute(build);
//        }
//
//        System.out.println(skuLsInfos.size());



//        Search build = new Search.Builder(
//                "{\n" +
//                "  \"query\": {\n" +
//                "   \"bool\": {\n" +
//                "     \"must\": [\n" +
//                "       {\"match\": {\n" +
//                "         \"name\": \"行动\"\n" +
//                "       }}\n" +
//                "     ],\n" +
//                "     \"filter\": {\n" +
//                "       \"term\": {\n" +
//                "         \"actorList.id\": \"3\"\n" +
//                "       }\n" +
//                "     }\n" +
//                "   }\n" +
//                "  }\n" +
//                "}").addIndex("movie_chn").addType("movie").build();
//
//        SearchResult execute = jestClient.execute(build);
//
//        List<SearchResult.Hit<Object,Void>> hits = execute.getHits(Object.class);
//        for(SearchResult.Hit<Object,Void> hit : hits){
//            Object source = hit.source;
//            System.out.println(source);
//        }

        Search build = new Search.Builder(dsl).addIndex("gmall0725").addType("SkuLsInfo").build();

        SearchResult execute = jestClient.execute(build);

        List<SearchResult.Hit<SkuLsInfo,Void>> hits = execute.getHits(SkuLsInfo.class);
        List<SkuLsInfo> skuLsInfos = new ArrayList<>();
        for(SearchResult.Hit<SkuLsInfo,Void> hit : hits){
            SkuLsInfo source = hit.source;
            skuLsInfos.add(source);
//            System.out.println(source);
        }
//        List<SkuInfo> skuInfos = skuService.getMySkuInfo("");
        System.out.println(skuLsInfos.size());

    }

    public static String getMyDsl() throws IOException {

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //bool
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        //must
        MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("skuName","硅谷");
        boolQueryBuilder.must(matchQueryBuilder);

        //filter
        TermQueryBuilder termQueryBuilder = new TermQueryBuilder("skuAttrValueList.valueId", "39");
        boolQueryBuilder.filter(termQueryBuilder);

        searchSourceBuilder.query(boolQueryBuilder);
        searchSourceBuilder.size(20);
        searchSourceBuilder.from(0);

        return searchSourceBuilder.toString();
//        Search build = new Search.Builder(
//                "{\n" +
//                        "  \"query\": {\n" +
//                        "    \"match\": {\n" +
//                        "      \"skuName\": \"硅谷\"\n" +
//                        "    }\n" +
//                        "  }\n" +
//                        "}").addIndex("gmall0725").addType("SkuLsInfo").build();
//
//        SearchResult execute = jestClient.execute(build);
//
//        List<SearchResult.Hit<SkuLsInfo,Void>> hits = execute.getHits(SkuLsInfo.class);
//        List<SkuLsInfo> skuLsInfos = new ArrayList<>();
//        for(SearchResult.Hit<SkuLsInfo,Void> hit : hits){
//            SkuLsInfo source = hit.source;
//            skuLsInfos.add(source);
//            System.out.println(source);
//        }
////        List<SkuInfo> skuInfos = skuService.getMySkuInfo("");
//        System.out.println(skuLsInfos.size());
    }

    public void search() throws IOException {
        Search build = new Search.Builder(
                "{\n" +
                        "  \"query\": {\n" +
                        "   \"bool\": {\n" +
                        "     \"must\": [\n" +
                        "       {\"match\": {\n" +
                        "         \"name\": \"行动\"\n" +
                        "       }}\n" +
                        "     ],\n" +
                        "     \"filter\": {\n" +
                        "       \"term\": {\n" +
                        "         \"actorList.id\": \"3\"\n" +
                        "       }\n" +
                        "     }\n" +
                        "   }\n" +
                        "  }\n" +
                        "}").addIndex("movie_chn").addType("movie").build();

        SearchResult execute = jestClient.execute(build);

        List<SearchResult.Hit<Object,Void>> hits = execute.getHits(Object.class);
        for(SearchResult.Hit<Object,Void> hit : hits){
            Object source = hit.source;
            System.out.println(source);
        }
    }

    public void test() throws IOException {
        List<SkuInfo> skuInfos = skuService.getMySkuInfo("61");
        System.out.println(skuInfos);

        List<SkuLsInfo> skuLsInfos = new ArrayList<>();
        for(SkuInfo skuInfo : skuInfos){
            SkuLsInfo skuLsInfo = new SkuLsInfo();
            BeanUtils.copyProperties(skuInfo,skuLsInfo);

            skuLsInfos.add(skuLsInfo);
        }
        for(SkuLsInfo skuLsInfo : skuLsInfos){
            Index build = new Index.Builder(skuLsInfo).index("gmall0725").type("SkuLsInfo").id(skuLsInfo.getId()).build();
            jestClient.execute(build);
        }

        System.out.println(skuLsInfos.size());
    }

}

