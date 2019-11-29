package com.sdca.api.model.dzyz;

import com.alibaba.fastjson.annotation.JSONType;
import lombok.Data;

@Data
@JSONType(orders = {"requestID","certType","serialNumber","countryName","organizationName","commonName","subjectPublicKeyInfo","algorithm"})
public class ChangeInfo {
    private  String requestID;
    private  String certType;
    private  String serialNumber;
    private  String countryName;
    private  String organizationName;
    private  String commonName;
    private  String subjectPublicKeyInfo;
    private  String algorithm;

}
