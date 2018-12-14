package com.java.gmall.list;

import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GmallListServiceApplicationTests {

    @Autowired
    JestClient jestClient;

    @Test
    public void contextLoads() throws IOException {
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

}

