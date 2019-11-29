package com.sdca.api.service;

import com.sdca.api.util.HttpsClientUtil;
import com.sdca.api.util.SignUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
@Service
public class DzzbAPIService {
    public String getDZzzbResponse(String s, LinkedHashMap map, HttpServletRequest request) {
        HttpSession session = request.getSession();

        String pcshost = (String) session.getAttribute("pcsIp");
        String pcsport = (String) session.getAttribute("pcsPort");
        String certID  = (String) session.getAttribute("certID");
        String certPSW = (String) session.getAttribute("certPSW");


        String signValue = "";
        try {
            String string = getSignValue(map);
            System.out.println("被签名字符串");
            System.out.println(string);
            signValue = SignUtil.sign(string, pcshost, pcsport, certID, certPSW);
        } catch (Exception exception) {
            exception.printStackTrace();
            return  "签名服务出错";
        }
        System.out.println("生成签名字符串");
        System.out.println(signValue);
        map.put("sign",signValue);
        String param = getParam(map);
        //String result = HttpClientUtil.PostUrlEncoded(s,param);
        String result = HttpsClientUtil.Post(s,param);
        return result;
    }

    private String getSignValue(Map map){
        String str = "";
        Iterator iterator = map.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry entry = (Map.Entry) iterator.next();
            String value = null;
            if(StringUtils.isNotBlank(entry.getValue()+"")&&!("signAlg".equals(entry.getKey()))){
                /*try {
                    value = URLEncoder.encode(entry.getValue()+"", "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }*/
                str += entry.getKey() + "=" + entry.getValue() + "&";
            }

        }
        return str.substring(0, str.length()-1);
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
