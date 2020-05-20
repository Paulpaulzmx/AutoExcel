package com.z5n.autoexcel.model.vo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UpdateUserInfoVo{
    private String trueName;
    private String gender;
    private String stuNum;
    private String classNum;
    private String idCardNum;
    private String tel;
    private String qq;
    private String address;
}
