package com.z5n.autoexcel.model.vo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ResetPasswordVo {
    private String username;
    private String verifyCode;
    private String newPassword;
    private String newPasswordVerify;
}
