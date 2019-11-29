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
//发请求
@Controller
@RequestMapping("/sync")
public class SyncController {
    //public static final String PATH = "http://221.214.5.80:59205/sealCertService";
    //public static final String PATH = "http://60.216.5.244:59205/sealCertService";
    //public static final String PATH = "http://221.214.5.66:59205/sealCertService";
    //验证签名
   // public static final String applicantCert = "MIICSjCCAfGgAwIBAgIIIBkFKQE3GJQwCgYIKoEcz1UBg3UwbzEaMBgGA1UEAwwRU2hhbkRvbmdTTTJUZXN0Q0ExDTALBgNVBAsMBFNEQ0ExDTALBgNVBAoMBFNEQ0ExEjAQBgNVBAcMCea1juWNl+W4gjESMBAGA1UECAwJ5bGx5Lic55yBMQswCQYDVQQGEwJDTjAeFw0xOTA1MjgxNTA4MzJaFw0yMDA1MjgxNTA4MzJaMHExGTAXBgNVBAMeEABDAEF7flPRbUuL1Xz7ft8xDTALBgNVBAsTBFNEQ0ExGzAZBgNVBAoTEjIyMS4yMTQuNS44MDo1OTIwNTEOMAwGA1UEBxMFSmluYW4xCzAJBgNVBAgTAlNEMQswCQYDVQQGEwJDTjBZMBMGByqGSM49AgEGCCqBHM9VAYItA0IABPullvnef07+vO++tUP12dwvfSOT78fElxtoOQWh1IPkr426yZIH1O2np9lGgYdrIWuPr7beZLLH3UOJEq+gngejdTBzMB8GA1UdIwQYMBaAFBxdrOHAKs5eSsyj9C8i/6Om8wTOMB0GA1UdDgQWBBSyZBbtm/W23LnM54n9/PiyxhqnzDAJBgNVHRMEAjAAMA4GA1UdDwEB/wQEAwIGwDAWBgNVHSUBAf8EDDAKBggrBgEFBQcDCDAKBggqgRzPVQGDdQNHADBEAiA2pPOv/vucNteu0erYSAL4xpe/AOt/CUHs0Ie1l0FAbAIgJkPzgx/svPELKHi6hh5CED6w21prkruG0vg2qbmlbNY=";
    //public  static  final String applicantCert = "MIIDQjCCAuigAwIBAgIIIBkFKAE2hpIwCgYIKoEcz1UBg3UwZjELMAkGA1UEBhMCQ04xETAPBgNVBAgMCFNIQU5ET05HMQ4wDAYDVQQHDAVKSU5BTjENMAsGA1UECgwEU0RDQTENMAsGA1UECwwEU0RDQTEWMBQGA1UEAwwNU2hhbkRvbmdTTTJDQTAeFw0xOTA1MjcyMzU4MDJaFw0yNDA1MjcyMzU4MDJaMGIxGTAXBgNVBAMeEABTAEQAQwBBe35T0Xz7ft8xGzAZBgNVBAoTEjIyMS4yMTQuNS42Njo1OTIwNTEOMAwGA1UEBxMFSmluYW4xCzAJBgNVBAgTAlNEMQswCQYDVQQGEwJDTjBZMBMGByqGSM49AgEGCCqBHM9VAYItA0IABJBQrnc7EKhu4MxJWl9qXaml/IJqB/qnq9RxqMzxbCm0F6fmtM9dQIDTSg22xyu3Bifgj7+KtlkyCr5a2pGpEUmjggGCMIIBfjBCBgNVHR8EOzA5MDegNaAzhjFodHRwOi8vMjIxLjIxNC41LjY2OjgwODgvU2hhbkRvbmdTTTJDQS9TTTJDUkwuanNwMAkGA1UdEwQCMAAwVgYDVR0gBE8wTTBLBgorBgEEAYGSSAEKMD0wOwYIKwYBBQUHAgEWL2h0dHA6Ly93d3cuc2RjYS5jb20uY24vVXBsb2FkRmlsZXMvU0RDQS1DUFMucGRmMFkGCCsGAQUFBwEBBE0wSzBJBggrBgEFBQcwAYY9aHR0cDovLzIyMS4yMTQuNS42Njo4MDg4L1NoYW5Eb25nU00yQ0EvZG93bmxvYWQvU00yQ0FjZXJ0LmNlcjAdBgNVHQ4EFgQUFDKNmAUmnn6q3M0vCit0y7d9aYQwDgYDVR0PAQH/BAQDAgbAMB8GA1UdIwQYMBaAFInNkOr4f1UW6BO2XCEjLvm6tU7dMCoGA1UdJQEB/wQgMB4GCCsGAQUFBwMIBggrBgEFBQcDAQYIKwYBBQUHAwIwCgYIKoEcz1UBg3UDSAAwRQIgRCWzcHPZFp4241bEQIHwDUixf3bRQLY6vnVobDhk11gCIQDnSomoqXvGrhJpDRDSg8/HVB+g4ZkmzscxZm5cg3OEjA==";
    //
    //SDCA签发系统_电子政务外网测试同步
    //public  static  final String applicantCert = "MIIDPjCCAuOgAwIBAgIIIBkGAwE5Y3cwCgYIKoEcz1UBg3UwbzEaMBgGA1UEAwwRU2hhbkRvbmdTTTJUZXN0Q0ExDTALBgNVBAsMBFNEQ0ExDTALBgNVBAoMBFNEQ0ExEjAQBgNVBAcMCea1juWNl+W4gjESMBAGA1UECAwJ5bGx5Lic55yBMQswCQYDVQQGEwJDTjAeFw0xOTA2MDIxNDM3MzFaFw0yMDA2MDIxNDM3MzFaMGIxGTAXBgNVBAMeEABTAEQAQwBBe35T0Xz7ft8xGzAZBgNVBAoTEjIyMS4yMTQuNS42Njo1OTIwNTEOMAwGA1UEBxMFSmluYW4xCzAJBgNVBAgTAlNEMQswCQYDVQQGEwJDTjBZMBMGByqGSM49AgEGCCqBHM9VAYItA0IABJBQrnc7EKhu4MxJWl9qXaml/IJqB/qnq9RxqMzxbCm0F6fmtM9dQIDTSg22xyu3Bifgj7+KtlkyCr5a2pGpEUmjggF0MIIBcDAfBgNVHSMEGDAWgBQcXazhwCrOXkrMo/QvIv+jpvMEzjAdBgNVHQ4EFgQUFDKNmAUmnn6q3M0vCit0y7d9aYQwCQYDVR0TBAIwADBbBgNVHR8EVDBSMFCgTqBMhkogbGRhcDovLzIyMS4yMTQuNS42NjozODkvY249VGVzdFNNMl9DUkwsYz1jbj9jcmxMaXN0P2Jhc2U/b2JqZWN0Q2xhc3M9Y3JsIDAOBgNVHQ8BAf8EBAMCBsAwXQYIKwYBBQUHAQEEUTBPME0GCCsGAQUFBzABhkFodHRwOi8vMjIxLjIxNC41LjY2OjgwODgvU2hhbkRvbmdTTTJUZXN0Q0EvZG93bmxvYWQvU00yQ0FjZXJ0LmNlcjBXBgNVHSAEUDBOMEwGCisGAQQBgZJIAQowPjA8BggrBgEFBQcCARYwaHR0cDovL3d3dy5zZGNhLmNvbS5jbi9VcGxvYWRGaWxlcy9TRENBLUNQUy5wZGYgMAoGCCqBHM9VAYN1A0kAMEYCIQDb4MbUqxhFtkLWWsovI3RiDxKnEPtvO6VoeqTgGE8MXwIhANriDhsvMbJRLxFZRPfwWwEGcjD3EwXJoBzae3QchYor";


    @Autowired
    DzyzAPIService dzyzApiService;

    @RequestMapping(value = "/apply",produces = "application/json; charset=utf-8")
    public String apply(Model model, @RequestParam String taskCode , @RequestParam String version,
                        @RequestParam String tokenInfo, @RequestParam String requestID ,
                        @RequestParam String certType, @RequestParam String countryName ,
                        @RequestParam String organizationName, @RequestParam String commonName,
                        @RequestParam String subjectPublicKeyInfo, @RequestParam String notBefore,
                        @RequestParam String notAfter, @RequestParam String algorithm,
                        @RequestParam String signAlgorithm, HttpServletRequest request){
        Map<String,Object> map = new LinkedHashMap<>();
        Map<String,String> data = new LinkedHashMap<>();

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

        String result = dzyzApiService.getSyncResponse(signAlgorithm, map, request);
        model.addAttribute("result",result);
        return "result";
    }


    @RequestMapping(value = "/revoke",produces = "application/json; charset=utf-8")
    public String revoke(Model model, @RequestParam String taskCode ,@RequestParam String version,
                         @RequestParam String tokenInfo, @RequestParam String requestID ,
                         @RequestParam String serialNumber, @RequestParam String signAlgorithm,
                         HttpServletRequest request){
        Map<String,Object> map = new LinkedHashMap<>();
        Map<String,String> data = new LinkedHashMap<>();
        List<Map<String,String> > list = new ArrayList<>();
        map.put("taskCode",taskCode);
        map.put("version",version);
        map.put("tokenInfo",tokenInfo);

        data.put("requestID",requestID);
        data.put("serialNumber",serialNumber);
        list.add(data);
        map.put("data",list);

        String result = dzyzApiService.getSyncResponse(signAlgorithm, map, request);
        model.addAttribute("result",result);
        return "result";
    }

    @RequestMapping(value = "/change",produces = "application/json; charset=utf-8")
    public String change(Model model, @RequestParam String taskCode ,@RequestParam String version,
                             @RequestParam String tokenInfo, @RequestParam String requestID ,
                             @RequestParam String certType, @RequestParam String countryName ,
                             @RequestParam String organizationName, @RequestParam String commonName,
                             @RequestParam String subjectPublicKeyInfo, @RequestParam String algorithm,
                             @RequestParam String signAlgorithm, @RequestParam String serialNumber,HttpServletRequest request){
        Map<String,Object> map = new LinkedHashMap<>();
        Map<String,String> data = new LinkedHashMap<>();
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

        String result = dzyzApiService.getSyncResponse(signAlgorithm, map, request);
        model.addAttribute("result",result);
        return "result";
    }


    @RequestMapping(value = "/delay")
    public String delay(Model model, @RequestParam String taskCode ,@RequestParam String version,
                        @RequestParam String tokenInfo, @RequestParam String requestID ,
                        @RequestParam String serialNumber, @RequestParam String signAlgorithm,
                        @RequestParam String subjectPublicKeyInfo, HttpServletRequest request ){
        Map<String,Object> map = new LinkedHashMap<>();
        Map<String,String> data = new LinkedHashMap<>();
        List<Map<String,String> > list = new ArrayList<>();
        map.put("taskCode",taskCode);
        map.put("version",version);
        map.put("tokenInfo",tokenInfo);

        data.put("requestID",requestID);
        data.put("serialNumber",serialNumber);
        data.put("subjectPublicKeyInfo",subjectPublicKeyInfo);
        list.add(data);
        map.put("data",list);

        String result = dzyzApiService.getSyncResponse(signAlgorithm, map, request);
        model.addAttribute("result",result);
        return "result";
    }

    @RequestMapping(value = "/batchApply")
    public String batchApply(ApplyModel applyModel, HttpServletRequest request, Model model){
//        String json = JSONObject.toJSONString(applyModel);
//        System.out.println(json);
        String result = dzyzApiService.batchHandler(applyModel,request);
        model.addAttribute("result",result);
        return  "result";
    }


    @RequestMapping(value = "/batchRevoke")
    public String batchRevoke(RevokeModel revokeModel, HttpServletRequest request, Model model){
        String result = dzyzApiService.batchHandler(revokeModel,request);
        model.addAttribute("result",result);
        return  "result";
    }

    @RequestMapping(value = "/batchDelay")
    public String batchDelay(DelayModel delayModel, HttpServletRequest request, Model model){
        String result = dzyzApiService.batchHandler(delayModel,request);
        model.addAttribute("result",result);
        return  "result";
    }
    @RequestMapping(value = "/batchChange")
    public String batchChange(ChangeModel changeModel, HttpServletRequest request, Model model){
        String result = dzyzApiService.batchHandler(changeModel,request);
        model.addAttribute("result",result);
        return  "result";
    }
}


