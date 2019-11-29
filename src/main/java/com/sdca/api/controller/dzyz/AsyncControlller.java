package com.sdca.api.controller.dzyz;

import com.sdca.api.model.dzyz.ApplyModel;
import com.sdca.api.model.dzyz.ChangeModel;
import com.sdca.api.model.dzyz.DelayModel;
import com.sdca.api.model.dzyz.RevokeModel;
import com.sdca.api.service.DzyzAPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/async")
public class AsyncControlller {

    @Autowired
    DzyzAPIService dzyzApiService;

    @RequestMapping(value = "/apply")
    public String apply(Model model, @RequestParam String taskCode , @RequestParam String version,
                        @RequestParam String tokenInfo, @RequestParam String requestID ,
                        @RequestParam String certType, @RequestParam String countryName ,
                        @RequestParam String organizationName, @RequestParam String commonName,
                        @RequestParam String subjectPublicKeyInfo, @RequestParam String notBefore,
                        @RequestParam String notAfter, @RequestParam String algorithm,
                        @RequestParam String signAlgorithm, HttpServletRequest request){

        Map<String,Object> map = new LinkedHashMap<>();
        Map<String,String> data = new LinkedHashMap<>();
        Map<String, Object> signInfo =  new LinkedHashMap<>();
        List<Map<String,String>> list = new ArrayList<>();

        map.put("taskCode",taskCode);
        map.put("version",version);
        map.put("tokenInfo",tokenInfo);

        data.put("requestID",requestID);
        data.put("certType",certType);
        data.put("countryName",countryName);
        data.put("organizationName",organizationName);
        data.put("commonName",commonName);
        data.put("subjectPublicKeyInfo",subjectPublicKeyInfo);
        data.put("notBefore",notBefore);
        data.put("notAfter",notAfter);
        data.put("algorithm",algorithm);

        list.add(data);
        map.put("data",list);

        String result = dzyzApiService.getAsyncResponse(signAlgorithm,map,request);
        model.addAttribute("result",result);
        return  "result";
    }


    @RequestMapping(value = "/revoke")
    public String revoke(Model model, @RequestParam String taskCode ,@RequestParam String version,
                         @RequestParam String tokenInfo, @RequestParam String requestID ,
                         @RequestParam String serialNumber, @RequestParam String signAlgorithm,HttpServletRequest request){
        Map<String,Object> map = new LinkedHashMap<>();
        Map<String,String> data = new LinkedHashMap<>();
        Map<String, Object> signInfo = new LinkedHashMap<>();

        List<Map<String,String> > list = new ArrayList<>();
        map.put("taskCode",taskCode);
        map.put("version",version);
        map.put("tokenInfo",tokenInfo);

        data.put("requestID",requestID);
        data.put("serialNumber",serialNumber);
        list.add(data);
        map.put("data",list);

        String result = dzyzApiService.getAsyncResponse(signAlgorithm,map,request);
        model.addAttribute("result",result);
        return  "result";

    }

    @RequestMapping(value = "/change")
    public String change(Model model, @RequestParam String taskCode ,@RequestParam String version,
                         @RequestParam String tokenInfo, @RequestParam String requestID ,
                         @RequestParam String certType, @RequestParam String countryName ,
                         @RequestParam String organizationName, @RequestParam String commonName,
                         @RequestParam String subjectPublicKeyInfo, @RequestParam String algorithm,
                         @RequestParam String signAlgorithm, @RequestParam String serialNumber,
                         HttpServletRequest request){
        Map<String,Object> map = new LinkedHashMap<>();
        Map<String,String> data = new LinkedHashMap<>();
        Map<String, Object> signInfo =  new LinkedHashMap<>();
        List<Map<String,String>> list = new ArrayList<>();

        map.put("taskCode",taskCode);
        map.put("version",version);
        map.put("tokenInfo",tokenInfo);

        data.put("requestID",requestID);
        data.put("certType",certType);
        data.put("serialNumber",serialNumber);
        data.put("countryName",countryName);
        data.put("organizationName",organizationName);
        data.put("commonName",commonName);
        data.put("subjectPublicKeyInfo",subjectPublicKeyInfo);
        data.put("algorithm",algorithm);

        list.add(data);
        map.put("data",list);

        String result = dzyzApiService.getAsyncResponse(signAlgorithm,map,request);
        model.addAttribute("result",result);
        return  "result";
    }


    @RequestMapping(value = "/delay")
    public String delay(Model model, @RequestParam String taskCode ,@RequestParam String version,
                        @RequestParam String tokenInfo, @RequestParam String requestID ,
                        @RequestParam String serialNumber, @RequestParam String signAlgorithm,
                        @RequestParam String subjectPublicKeyInfo , HttpServletRequest request){
        Map<String,Object> map = new LinkedHashMap<>();
        Map<String,String> data = new LinkedHashMap<>();
        Map<String, Object> signInfo = new LinkedHashMap<>();

        List<Map<String,String> > list = new ArrayList<>();
        map.put("taskCode",taskCode);
        map.put("version",version);
        map.put("tokenInfo",tokenInfo);

        data.put("requestID",requestID);
        data.put("serialNumber",serialNumber);
        data.put("subjectPublicKeyInfo",subjectPublicKeyInfo);
        list.add(data);
        map.put("data",list);

        String result = dzyzApiService.getAsyncResponse(signAlgorithm,map,request);
        model.addAttribute("result",result);
        return  "result";
    }


    @RequestMapping(value = "/batchApply")
    public String batchApply(ApplyModel applyModel, HttpServletRequest request, Model model){
//        String json = JSONObject.toJSONString(applyModel);
//        System.out.println(json);
        String result = dzyzApiService.asyncBatchHandler(applyModel,request);
        model.addAttribute("result",result);
        return  "result";
    }


    @RequestMapping(value = "/batchRevoke")
    public String batchRevoke(RevokeModel revokeModel, HttpServletRequest request, Model model){
        String result = dzyzApiService.asyncBatchHandler(revokeModel,request);
        model.addAttribute("result",result);
        return  "result";
    }

    @RequestMapping(value = "/batchDelay")
    public String batchDelay(DelayModel delayModel, HttpServletRequest request, Model model){
        String result = dzyzApiService.asyncBatchHandler(delayModel,request);
        model.addAttribute("result",result);
        return  "result";
    }
    @RequestMapping(value = "/batchChange")
    public String batchChange(ChangeModel changeModel, HttpServletRequest request, Model model){
        String result = dzyzApiService.asyncBatchHandler(changeModel,request);
        model.addAttribute("result",result);
        return  "result";
    }


}
