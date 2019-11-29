package com.sdca.api.controller.qfxt;

import com.alibaba.fastjson.JSON;
import com.sdca.api.service.QfxtAPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/qfxt")
public class QfxtController {
    @Autowired
    private QfxtAPIService qfxtAPIService;

    @RequestMapping("/register")
    public String register(@RequestParam Map map, Model model, HttpServletRequest request){
        String result =  qfxtAPIService.register(map,request);
        model.addAttribute("result",result);
        return "result";
    }

    @RequestMapping("/change")
    public String change(@RequestParam Map map, Model model, HttpServletRequest request){
        String result =  qfxtAPIService.change(map,request);
        model.addAttribute("result",result);
        return "result";
    }

    @RequestMapping("/update")
    public String update(@RequestParam Map map, Model model, HttpServletRequest request){
        String result =  qfxtAPIService.update(map,request);
        model.addAttribute("result",result);
        return "result";
    }

    @RequestMapping("/reissue")
    public String reissue(@RequestParam Map map, Model model, HttpServletRequest request){
        String result =  qfxtAPIService.reissue(map,request);
        model.addAttribute("result",result);
        return "result";
    }

    @RequestMapping("/info")
    @ResponseBody
    public Map<String,Object> info(@RequestParam Map map, HttpServletRequest request){
        String act = (String) map.get("act");
        String result =  qfxtAPIService.info(act,map,request);
        Map result1Map = new HashMap<>();
        if(result == null){
            result1Map.put("result",null);
        }else{
            result1Map = JSON.parseObject(result,Map.class);
        }

        return result1Map;
    }
}
