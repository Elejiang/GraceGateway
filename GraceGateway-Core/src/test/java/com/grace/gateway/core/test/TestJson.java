package com.grace.gateway.core.test;

import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.grace.gateway.config.config.Config;
import org.junit.Test;

public class TestJson {

    @Test
    public void testJson() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        Config config = new Config();
        objectNode.putPOJO("data", config);
        System.out.println(mapper.writeValueAsString(config));

        System.out.println(JSONUtil.toJsonStr(config));
    }

}
