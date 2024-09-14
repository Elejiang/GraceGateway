package com.grace.gateway.config.test;

import com.alibaba.fastjson.JSON;
import org.junit.Test;

public class TestJson {

    @Test
    public void testJson() {
        String json =
                "{\"routes\": [\n" +
                "        {\n" +
                "            \"id\": \"user-service-route\",\n" +
                "            \"serviceName\": \"user-service-1\",\n" +
                "            \"uri\": \"/api/user/**\"\n" +
                "        }\n" +
                "    ]" +
                "}";
        System.out.println(JSON.parseObject(json).getJSONArray("routes"));
    }

}
