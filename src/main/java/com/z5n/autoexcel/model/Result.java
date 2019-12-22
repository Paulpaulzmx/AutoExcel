package com.z5n.autoexcel.model;

import lombok.Data;
import org.apache.poi.ss.formula.functions.T;

@Data
public class Result {
    private Integer resultCode;
    private String status;
    private T data;

    public Result() {
    }



}
