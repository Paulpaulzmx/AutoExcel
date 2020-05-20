package com.z5n.autoexcel.model.vo;

import com.z5n.autoexcel.model.entity.SubmitMsg;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class MyHistoryVo extends SubmitMsg {

    private String fillerName;
    private String fileName;
    private String title;
    private String head;
    private String updateTimeStr;
}
