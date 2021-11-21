package com.zero.treasure.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.parser.ParserConfig;
import lombok.Data;

public class SnakeProperties2Bean {

    public static void main(String[] args) {
        String json = "{\n" +
                "    \"en_name\" : \"zero\",\n" +
                "    \"ch_name\" : \"123\"\n" +
                "}";
        ParserConfig config = new ParserConfig();
        config.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
        User user = JSON.parseObject(json, User.class, config);
        System.out.println(user);
    }


    @Data
    static class User {
        private String enName;
        private String chName;
    }

}
