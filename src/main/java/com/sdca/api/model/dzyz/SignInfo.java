package com.sdca.api.model.dzyz;

import com.alibaba.fastjson.annotation.JSONType;
import lombok.Data;

@JSONType(orders={"signAlgorithm","signValue"})
@Data
public class SignInfo {
    private String signAlgorithm;
    private String signValue;

}
