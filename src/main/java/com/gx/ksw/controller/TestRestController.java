package com.gx.ksw.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/rest")
public class TestRestController {

    @GetMapping("/su")
    public JSONObject su(@RequestParam Integer id) {
        if (Objects.equals(1, id)) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", "zaoyin loushang");
            return jsonObject;
        }

//        if (Objects.equals(2, id)) {
//            Map<String, Object> map = new HashMap<>(16);
//            map.put("hhe", 24);
//            return map;
//        }

        return null;

    }
}
