package com.z5n.autoexcel.model.vo;

import com.z5n.autoexcel.model.entity.Excel;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ExcelVo extends Excel {
    private int submitNum = 0;
    private String createTimeStr;
    private String updateTimeStr;
}
