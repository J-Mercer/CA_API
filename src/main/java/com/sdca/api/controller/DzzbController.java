package com.sdca.api.controller;

import com.sdca.api.service.DzzbAPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;

@Controller
@RequestMapping("/dzzb")
public class DzzbController {

   // private String path = "http://60.216.5.244:59202/CA/";
    private String path = "https://60.216.5.244:59206/CA/";
    @Autowired
    DzzbAPIService dzzbAPIService;

    @RequestMapping(value = "/apply")
    public String apply(Model model, @RequestParam LinkedHashMap map, HttpServletRequest request){
        String string = dzzbAPIService.getDZzzbResponse(path+"applycert", map, request);
        model.addAttribute("result",string);
        return "result";
    }

    @RequestMapping(value = "/update")
    public String update(Model model, @RequestParam LinkedHashMap map, HttpServletRequest request){
        String string = dzzbAPIService.getDZzzbResponse(path+"updatecert", map, request);
        model.addAttribute("result",string);
        return "result";
    }
    @RequestMapping(value = "/revoke")
    public String revoke(Model model, @RequestParam LinkedHashMap map, HttpServletRequest request){
        String string = dzzbAPIService.getDZzzbResponse(path+"revokecert", map, request);
        model.addAttribute("result",string);
        return "result";
    }
}
