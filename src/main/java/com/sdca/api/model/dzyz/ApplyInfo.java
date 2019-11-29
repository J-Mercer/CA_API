package com.sdca.api.model.dzyz;

import com.alibaba.fastjson.annotation.JSONType;
import lombok.Data;

@Data
@JSONType(orders = {"requestID","certType","countryName","organizationName","commonName","subjectPublicKeyInfo","notBefore","notAfter","algorithm"})
public class ApplyInfo {
    private String requestID;
    private String certType;
    private String countryName;
    private String organizationName;
    private String commonName;
    private String subjectPublicKeyInfo;
    private String notBefore;
    private String notAfter;
    private String algorithm;

}
