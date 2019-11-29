package com.sdca.api.model.dzyz;

import com.alibaba.fastjson.annotation.JSONType;
import lombok.Data;

import java.util.Map;

@Data
@JSONType(orders={"taskCode","version","tokenInfo","data","signInfo"})
public class TaskModel extends BaseModel {
    private Map<String, String> data;

}
