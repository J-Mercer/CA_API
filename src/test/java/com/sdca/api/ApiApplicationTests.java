package com.sdca.api;

import com.alibaba.fastjson.JSON;
import com.sdca.api.util.HttpClientUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.LinkedHashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApiApplicationTests {
    @Test
    public void test(){
        String urlpath = "http://60.216.5.244:59203/sdca/ca_service.php?act=register";
        Map<String,String> map = new LinkedHashMap<>();
        map.put("application_code","SD180028-YY01");
        map.put("cn","20191009test1");
        map.put("ou","372330199600000001");
        map.put("password","111111");
        System.out.println(JSON.toJSONString(map));
        String result = HttpClientUtil.Post(urlpath, JSON.toJSONString(map),"utf-8");
    }


}
