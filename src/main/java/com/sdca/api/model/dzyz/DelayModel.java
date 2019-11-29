package com.sdca.api.model.dzyz;

import com.alibaba.fastjson.annotation.JSONType;
import lombok.Data;

import java.util.List;

@Data
@JSONType(orders={"taskCode","version","tokenInfo","data","signInfo"})
public class DelayModel extends  BaseModel{
    private List<DelayInfo> data;

}
