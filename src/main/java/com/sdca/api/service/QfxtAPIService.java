package com.sdca.api.service;

import com.alibaba.fastjson.JSON;
import com.sdca.api.util.HttpClientUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Service
public class QfxtAPIService {
    //private String path = "http://172.20.188.241:8002/59202/";

    //private  String req = "MIIBFDCBuQIBADBXMRAwDgYDVQQDDAcwNTE0MDAyMRwwGgYDVQQLDBNJMzcwNjg1MTk5MzA5MDE1MDAxMQswCQYDVQQHDAJqbjELMAkGA1UECAwCc2QxCzAJBgNVBAYMAkNOMFkwEwYHKoZIzj0CAQYIKoEcz1UBgi0DQgEEMq0TiYEG0BfZceHiCI6jcHC9mdX1DmkGFbnJhmgANk8cc1uBdX/k/ogixfSzGS/mqoDpvxbFA4pAY5s29mWxjqAAMAwGCCqBHM9VAYN1BQADSAAwRQIhAPwXaGcW9uh6WJj5d+aD3/jPjf2k/AV8uvgTaHYnKO5IAiALyBoUfOw60dgF3IEeWWIBctm7VjmWIcrx4ZvMOtmPpw==";

    public String register(Map map, HttpServletRequest request) {
        HttpSession session = request.getSession();

        String path    = (String) session.getAttribute("serviceAPI");
        String req    = (String) session.getAttribute("req");
        //1调用https://60.216.5.244:59202/qf_biaozhun/ca_service.php?act=register
        String result1 = HttpClientUtil.PostUrlEncoded(path+"ca_service.php?act=register",getParam(map));

        Map result1Map = JSON.parseObject(result1,Map.class);
        result1Map.get("status");
        if(!"1".equals(result1Map.get("status")+"")){
            return  result1;
        }
        Map data = JSON.parseObject( result1Map.get("data")+"",Map.class);

        //2ttps://60.216.5.244:59202/qf_biaozhun/ca_service.php?act=get_register_certificate直接下证
        Map map2 = new HashMap();
        map2.put("register_id",data.get("register_id"));
        map2.put("req",req);
        map2.put("password",map.get("password"));
        String result2 = HttpClientUtil.PostUrlEncoded(path+"ca_service.php?act=get_register_certificate", getParam(map2));
        return result2;
    }

    public String change(Map map, HttpServletRequest request) {
        HttpSession session = request.getSession();

        String path    = (String) session.getAttribute("serviceAPI");
        String req    = (String) session.getAttribute("req");
        //1调用https://60.216.5.244:59202/qf_biaozhun/ca_service.php?act=change
        String result1 = HttpClientUtil.PostUrlEncoded(path+"ca_service.php?act=change",getParam(map));

        Map result1Map = JSON.parseObject(result1,Map.class);
        result1Map.get("status");
        if(!"1".equals(result1Map.get("status")+"")){
            return  result1;
        }
        Map data = JSON.parseObject( result1Map.get("data")+"",Map.class);

        //2根据返回的dn生成req调用https://60.216.5.244:59202/qf_biaozhun/ca_service.php?act=get_change_certificate直接下证
        Map map2 = new HashMap();
        map2.put("change_id",data.get("change_id"));
        map2.put("req",req);
        map2.put("password",map.get("password"));
        String result2 = HttpClientUtil.PostUrlEncoded(path+"ca_service.php?act=get_change_certificate", getParam(map2));
        return result2;
    }

    public String update(Map map, HttpServletRequest request) {
        HttpSession session = request.getSession();

        String path    = (String) session.getAttribute("serviceAPI");
        //1调用https://60.216.5.244:59202/qf_biaozhun/ca_service.php?act=update直接下证
        String result1 = HttpClientUtil.PostUrlEncoded(path+"ca_service.php?act=update",getParam(map));
        return  result1;
    }

    public String reissue(Map map, HttpServletRequest request) {
        HttpSession session = request.getSession();

        String path    = (String) session.getAttribute("serviceAPI");
        String req    = (String) session.getAttribute("req");
        //1查询https://60.216.5.244:59202/qf_biaozhun/ca_service.php?act=reissue_query
        String result1 = HttpClientUtil.PostUrlEncoded(path+"ca_service.php?act=reissue_query",getParam(map));

        Map result1Map = JSON.parseObject(result1,Map.class);
        result1Map.get("status");
        if(!"1".equals(result1Map.get("status")+"")){
            return  result1;
        }
        Map data = JSON.parseObject( result1Map.get("data")+"",Map.class);
        //2调用https://60.216.5.244:59202/qf_biaozhun/ca_service.php?act=reissue直接下证
        Map map2 = new HashMap();
        map2.put("cert_id",data.get("cert_id"));
        map2.put("req",req);
        map2.put("password",map.get("password"));
        map2.put("application_code",map.get("application_code"));
        String result2 = HttpClientUtil.PostUrlEncoded(path+"ca_service.php?act=reissue", getParam(map2));
        return result2;
    }

    public String info(String act, Map map, HttpServletRequest request) {
        HttpSession session = request.getSession();

        String path    = (String) session.getAttribute("serviceAPI");
        String result1 = HttpClientUtil.PostUrlEncoded(path+"ca_service.php?act="+act,getParam(map));
        return  result1;
    }

    private String getParam(Map map){
        String str = "";
        Iterator iterator = map.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry entry = (Map.Entry) iterator.next();
            String value = null;
            if(StringUtils.isNotBlank(entry.getValue()+"")){
                try {
                    value = URLEncoder.encode(entry.getValue()+"", "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                str += entry.getKey() + "=" + value + "&";
            }

        }
        return str.substring(0, str.length()-1);
    }



}
