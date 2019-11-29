package com.sdca.api.controller;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sdca.api.util.HttpClientUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;


@Controller
public class IndexController {
    //获取随机数
    //public static final String applicantCert = "MIICSjCCAfGgAwIBAgIIIBkFKQE3GJQwCgYIKoEcz1UBg3UwbzEaMBgGA1UEAwwRU2hhbkRvbmdTTTJUZXN0Q0ExDTALBgNVBAsMBFNEQ0ExDTALBgNVBAoMBFNEQ0ExEjAQBgNVBAcMCea1juWNl+W4gjESMBAGA1UECAwJ5bGx5Lic55yBMQswCQYDVQQGEwJDTjAeFw0xOTA1MjgxNTA4MzJaFw0yMDA1MjgxNTA4MzJaMHExGTAXBgNVBAMeEABDAEF7flPRbUuL1Xz7ft8xDTALBgNVBAsTBFNEQ0ExGzAZBgNVBAoTEjIyMS4yMTQuNS44MDo1OTIwNTEOMAwGA1UEBxMFSmluYW4xCzAJBgNVBAgTAlNEMQswCQYDVQQGEwJDTjBZMBMGByqGSM49AgEGCCqBHM9VAYItA0IABPullvnef07+vO++tUP12dwvfSOT78fElxtoOQWh1IPkr426yZIH1O2np9lGgYdrIWuPr7beZLLH3UOJEq+gngejdTBzMB8GA1UdIwQYMBaAFBxdrOHAKs5eSsyj9C8i/6Om8wTOMB0GA1UdDgQWBBSyZBbtm/W23LnM54n9/PiyxhqnzDAJBgNVHRMEAjAAMA4GA1UdDwEB/wQEAwIGwDAWBgNVHSUBAf8EDDAKBggrBgEFBQcDCDAKBggqgRzPVQGDdQNHADBEAiA2pPOv/vucNteu0erYSAL4xpe/AOt/CUHs0Ie1l0FAbAIgJkPzgx/svPELKHi6hh5CED6w21prkruG0vg2qbmlbNY=";

    //public static final String PATH = "http://60.216.5.244:59202/sealCertService";

    //电子印章
    @RequestMapping("{path}/page/{page}")
    public String page (@PathVariable String page, Model model,HttpServletRequest request){
        String random = getRandomStr(request);
        String uid = UUID.randomUUID().toString().replace("-","");
        model.addAttribute("random",random);
        return "dzyz/"+page;
    }
    private String getRandomStr(HttpServletRequest request){
        HttpSession session = request.getSession();
        String randomCert = (String) session.getAttribute("randomCert");
        String path = (String) session.getAttribute("serviceAPI");

        Map<String,Object> map = new LinkedHashMap<>();

        String result1 = new SimpleDateFormat("yyyyMMdd").format(System.currentTimeMillis());
        String uuid = UUID.randomUUID().toString().replace("-","").substring(3,7);
        String randomA = "test"+result1+uuid;

        map.put("taskCode", "applyServiceRandom");
        map.put("version","1.0");
        map.put("applicantCert",randomCert);
        map.put("randomA",randomA);

        String data = JSONObject.toJSONString(map);
        String result = HttpClientUtil.Post(path,data,"utf-8");
        JSONObject object = (JSONObject) JSONObject.parse(result);
        String randomB = object.getString("randomB");
        return  randomA+randomB;
    }

    //电子招标
    @RequestMapping("/dzzb/page/{page}")
    public String dzzbPage (@PathVariable String page, Model model,HttpServletRequest request){

        return "dzzb/"+page;
    }

    //签发系统
    @RequestMapping("/qfxt/page/{page}")
    public String qfxtPage (@PathVariable String page, Model model,HttpServletRequest request){
        Date date = new Date();
        String cn = "TEST"+(date.getYear()+1900)+(date.getMonth()+1)+date.getDay()+date.getHours()+date.getMinutes()+date.getSeconds();
        model.addAttribute("cn",cn);
        return "qfxt/"+page;
    }



    //系统参数
    @RequestMapping("/setting")
    public String settingPage (){
        return "setting";
    }

    @RequestMapping("/")
    public String indexPage (){
        return "setting";
    }

    @RequestMapping(value = "/save")
    public String saveSetting(Model model,@RequestParam Map<String,String> map,HttpServletRequest request,HttpServletResponse response){
        Iterator iterator = map.entrySet().iterator();
        HttpSession session = request.getSession();
        Cookie cookie = new Cookie("JSESSIONID", session.getId());
        cookie.setMaxAge(3600*24*7);
        while (iterator.hasNext()){
            Map.Entry entry = (Map.Entry) iterator.next();
            response.addCookie(cookie);
            session.setAttribute((String) entry.getKey(),entry.getValue());
        }
        session.setMaxInactiveInterval(-1);
        model.addAttribute("result",JSONObject.toJSONString(map, SerializerFeature.WriteMapNullValue));
        System.out.println(JSONObject.toJSON(map));
        return "result";
    }
}
