package com.sdca.api.model.dzyz;

import lombok.Data;

@Data
public class BaseModel {
    private String taskCode;
    private String version;
    private String tokenInfo;
    private SignInfo signInfo;

}
