package com.sdca.api.model.dzyz;

import com.alibaba.fastjson.annotation.JSONType;
import lombok.Data;

@Data
@JSONType(orders = {"requestID","serialNumber"})
public class RevokeInfo {
    private String requestID;
    private String serialNumber;


}
