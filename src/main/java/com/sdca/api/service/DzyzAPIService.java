package com.sdca.api.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sdca.api.model.dzyz.BaseModel;
import com.sdca.api.model.dzyz.SignInfo;
import com.sdca.api.model.dzyz.TaskModel;
import com.sdca.api.util.HttpClientUtil;
import com.sdca.api.util.SignUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class DzyzAPIService {

    public String getSyncResponse(String signAlgorithm, Map<String, Object> map, HttpServletRequest request) {
        HttpSession session = request.getSession();

        String path    = (String) session.getAttribute("serviceAPI");
        String pcshost = (String) session.getAttribute("pcsIp");
        String pcsport = (String) session.getAttribute("pcsPort");
        String certID  = (String) session.getAttribute("certID");
        String certPSW = (String) session.getAttribute("certPSW");
        String signCert = (String) session.getAttribute("signCert");
        String svsIp    = (String) session.getAttribute("svsIp");
        String svsPort  = (String) session.getAttribute("svsPort");

        Map<String, Object> signInfo =  new LinkedHashMap<>();
        String signValue = "";

        String sourceData = JSONObject.toJSONString(map);
        try {
            signValue = SignUtil.sign(sourceData, pcshost, pcsport, certID, certPSW);
        } catch (Exception exception) {
            exception.printStackTrace();
            return  map.get("taskCode")+"签名服务出错";
        }

        signInfo.put("signAlgorithm",signAlgorithm);
        signInfo.put("signValue",signValue);
        map.put("signInfo",signInfo);
        String result = "";

        String data = JSONObject.toJSONString(map);
        try {
            result = HttpClientUtil.Post(path,data,"utf-8");
        }catch (Exception e){
            e.printStackTrace();
            return "请求出错";
        }
        Map<String,Object> resultMap  = JSON.parseObject(result);
        if(StringUtils.isBlank((String) resultMap.get("resultCode")) || !resultMap.get("resultCode").equals("0")){
            return result;
        }

        try {
            boolean flag = check(result,signCert,svsIp,svsPort);
            if(!flag) return map.get("taskCode")+"签名验证失败";
        }catch (Exception e){
            e.printStackTrace();
            return map.get("taskCode")+"签名验证服务出错";
        }

        return  result;
    }

    public String getAsyncResponse(String signAlgorithm,Map<String, Object> map,HttpServletRequest request) {
        HttpSession session = request.getSession();

        String path    = (String) session.getAttribute("serviceAPI");
        //签名
        String pcshost = (String) session.getAttribute("pcsIp");
        String pcsport = (String) session.getAttribute("pcsPort");
        String certID  = (String) session.getAttribute("certID");
        String certPSW = (String) session.getAttribute("certPSW");

        //验签
        String signCert = (String) session.getAttribute("signCert");
        String svsIp    = (String) session.getAttribute("svsIp");
        String svsPort  = (String) session.getAttribute("svsPort");

        System.out.println("--------------------第一步--------------------");
        Map<String, Object> signInfo1 =  new LinkedHashMap<>();
        String signValue1 = "";

        String sourceData = JSONObject.toJSONString(map);
        try {
            signValue1 = SignUtil.sign(sourceData, pcshost, pcsport, certID, certPSW);
        } catch (Exception exception) {
            exception.printStackTrace();
            return   map.get("taskCode")+"签名失败";
        }
        signInfo1.put("signAlgorithm",signAlgorithm);
        signInfo1.put("signValue",signValue1);
        map.put("signInfo",signInfo1);
        String result1 = "";

        String data = JSONObject.toJSONString(map);
        try {
            result1 = HttpClientUtil.Post(path,data,"utf-8");
        }catch (Exception e){
            e.printStackTrace();
            return map.get("taskCode")+"签名服务出错";
        }
        Map<String,Object> resultMap1 = JSON.parseObject(result1);
        if(StringUtils.isBlank((String) resultMap1.get("resultCode")) || !resultMap1.get("resultCode").equals("0")){
            return result1;
        }
        try {
            boolean flag = check(result1,signCert,svsIp,svsPort);
            if(!flag) return map.get("taskCode")+"签名验证失败";
        }catch (Exception e){
            e.printStackTrace();
            return map.get("taskCode")+"签名验证服务出错";
        }

        System.out.println("----------------------------第二步-----------------------------------------");

        Map<String, String> dataMap= (Map<String, String>) resultMap1.get("data");
        String taskId = dataMap.get("taskId");

        Map<String,String> idMap = new LinkedHashMap<>();
        idMap.put("taskId",taskId);

        Map<String,Object> taskMap = new LinkedHashMap<>();
        taskMap.put("taskCode","checkResult");
        taskMap.put("version",map.get("version"));
        taskMap.put("data",idMap);

        String taskData = JSONObject.toJSONString(taskMap);
        String signValue = "";
        try {
            signValue = SignUtil.sign(taskData, pcshost, pcsport, certID, certPSW);
        } catch (Exception exception) {
            exception.printStackTrace();
            return "checkResult签名服务出错";
        }

        Map<String,String> signInfo = new LinkedHashMap<>();
        signInfo.put("signAlgorithm",signAlgorithm);
        signInfo.put("signValue",signValue);

        taskMap.put("signInfo",signInfo);

        String data2 = JSONObject.toJSONString(taskMap);
        String result = HttpClientUtil.Post(path,data2,"utf-8");
        Map<String,Object> resultMap = JSON.parseObject(result);
        if(StringUtils.isBlank((String) resultMap.get("resultCode")) || !resultMap.get("resultCode").equals("0")){
            return result1;
        }
        try {
            boolean flag = check(result,signCert,svsIp,svsPort);
            if(!flag) return "checkResult签名验证失败";
        }catch (Exception e){
            e.printStackTrace();
            return "checkResult签名验证服务出错";
        }
        return  result;
    }

    private boolean check(String result, String signCert, String svsIp, String svsPort){
        Map<String,Object> map = JSON.parseObject(result, Feature.OrderedField);
        LinkedHashMap<String,Object> data = new LinkedHashMap<>();
        data.put("taskCode",map.get("taskCode"));
        data.put("version",map.get("version"));
        data.put("resultCode",map.get("resultCode"));
        data.put("resultCodeMsg",map.get("resultCodeMsg"));
        data.put("data",map.get("data"));
        String dataString = JSON.toJSONString(data, SerializerFeature.WriteMapNullValue);
        Map<String,String> signInfo = (Map<String, String>) map.get("signInfo");
        String signValue = signInfo.get("signValue");
        boolean flag = SignUtil.verifySignatureWithSVSToIsignature(dataString,signValue,signCert,null,svsIp,svsPort);
        return flag;
    }



    public String batchHandler(BaseModel dataModel, HttpServletRequest request) {
        HttpSession session = request.getSession();

        String path    = (String) session.getAttribute("serviceAPI");
        String pcshost = (String) session.getAttribute("pcsIp");
        String pcsport = (String) session.getAttribute("pcsPort");
        String certID  = (String) session.getAttribute("certID");
        String certPSW = (String) session.getAttribute("certPSW");
        String signCert = (String) session.getAttribute("signCert");
        String svsIp    = (String) session.getAttribute("svsIp");
        String svsPort  = (String) session.getAttribute("svsPort");

        String signValue = "";
        String signAlgorithm = dataModel.getSignInfo().getSignAlgorithm();
        dataModel.setSignInfo(null);
        String sourceData = JSONObject.toJSONString(dataModel);
        try {
            signValue = SignUtil.sign(sourceData, pcshost, pcsport, certID, certPSW);
        } catch (Exception exception) {
            exception.printStackTrace();
            return  dataModel.getTaskCode()+"签名服务出错";
        }

        SignInfo signInfo= new SignInfo();
        signInfo.setSignValue(signValue);
        signInfo.setSignAlgorithm(signAlgorithm);
        dataModel.setSignInfo(signInfo);

        String data = JSONObject.toJSONString(dataModel);
        String result = "";
        try {
            result = HttpClientUtil.Post(path,data,"utf-8");
        }catch (Exception e){
            e.printStackTrace();
            return "请求出错";
        }
        Map<String,Object> resultMap  = JSON.parseObject(result);
        if(StringUtils.isBlank((String) resultMap.get("resultCode")) || !resultMap.get("resultCode").equals("0")){
            return result;
        }

        try {
            boolean flag = check(result,signCert,svsIp,svsPort);
            if(!flag) return dataModel.getTaskCode()+"签名验证失败";
        }catch (Exception e){
            e.printStackTrace();
            return dataModel.getTaskCode()+"签名验证服务出错";
        }

        return  result;
    }

    public String asyncBatchHandler(BaseModel dataModel, HttpServletRequest request) {
        HttpSession session = request.getSession();

        String path    = (String) session.getAttribute("serviceAPI");
        //签名
        String pcshost = (String) session.getAttribute("pcsIp");
        String pcsport = (String) session.getAttribute("pcsPort");
        String certID  = (String) session.getAttribute("certID");
        String certPSW = (String) session.getAttribute("certPSW");

        //验签
        String signCert = (String) session.getAttribute("signCert");
        String svsIp    = (String) session.getAttribute("svsIp");
        String svsPort  = (String) session.getAttribute("svsPort");

        String signAlgorithm = dataModel.getSignInfo().getSignAlgorithm();

        System.out.println("--------------------第一步--------------------");
        String signValue1 = "";

        dataModel.setSignInfo(null);
        String sourceData = JSONObject.toJSONString(dataModel);
        try {
            signValue1 = SignUtil.sign(sourceData, pcshost, pcsport, certID, certPSW);
        } catch (Exception exception) {
            exception.printStackTrace();
            return   dataModel.getTaskCode()+"签名失败";
        }
        SignInfo signInfo1 = new SignInfo();
        signInfo1.setSignAlgorithm(signAlgorithm);
        signInfo1.setSignValue(signValue1);
        dataModel.setSignInfo(signInfo1);

        String result1 = "";
        String data = JSONObject.toJSONString(dataModel);
        try {
            result1 = HttpClientUtil.Post(path,data,"utf-8");
        }catch (Exception e){
            e.printStackTrace();
            return dataModel.getTaskCode()+"签名服务出错";
        }
        Map<String,Object> resultMap1 = JSON.parseObject(result1);
        if(StringUtils.isBlank((String) resultMap1.get("resultCode")) || !resultMap1.get("resultCode").equals("0")){
            return result1;
        }
        try {
            boolean flag = check(result1,signCert,svsIp,svsPort);
            if(!flag) return dataModel.getTaskCode()+"签名验证失败";
        }catch (Exception e){
            e.printStackTrace();
            return dataModel.getTaskCode()+"签名验证服务出错";
        }

        System.out.println("----------------------------第二步-----------------------------------------");
        Map<String, String> dataMap= (Map<String, String>) resultMap1.get("data");

        TaskModel taskModel = new TaskModel();
        taskModel.setTaskCode("checkResult");
        taskModel.setVersion(dataModel.getVersion());
        taskModel.setData(dataMap);


        String taskSignData = JSONObject.toJSONString(taskModel);
        String signValue2 = "";
        try {
            signValue2 = SignUtil.sign(taskSignData, pcshost, pcsport, certID, certPSW);
        } catch (Exception exception) {
            exception.printStackTrace();
            return "checkResult签名服务出错";
        }
        SignInfo signInfo2 = new SignInfo();
        signInfo2.setSignValue(signValue2);
        signInfo2.setSignAlgorithm(signAlgorithm);
        taskModel.setSignInfo(signInfo2);

        String taskData = JSONObject.toJSONString(taskModel);
        String result2 = HttpClientUtil.Post(path,taskData,"utf-8");
        Map<String,Object> resultMap = JSON.parseObject(result2);
        if(StringUtils.isBlank((String) resultMap.get("resultCode")) || !resultMap.get("resultCode").equals("0")){
            return result2;
        }
        try {
            boolean flag = check(result2,signCert,svsIp,svsPort);
            if(!flag) return "checkResult签名验证失败";
        }catch (Exception e){
            e.printStackTrace();
            return "checkResult签名验证服务出错";
        }
        return  result2;
    }
}
