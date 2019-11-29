package com.sdca.api.model.dzyz;

import com.alibaba.fastjson.annotation.JSONType;
import lombok.Data;

@Data
@JSONType(orders = {"requestID","serialNumber","subjectPublicKeyInfo"})
public class DelayInfo {
    private String requestID;
    private String serialNumber;
    private String subjectPublicKeyInfo;


}
