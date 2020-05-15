package com.z5n.autoexcel.model.vo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UserRegisterVo {
    private String username;
    private String password;
    private String passwordVerify;
    private String email;
    private String trueName;
    private String gender;
    private String stuNum;
    private String idCardNum;
    private String tel;
    private String qq;
    private String address;
    private String verifyCode;
}
