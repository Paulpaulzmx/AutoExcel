package com.z5n.autoexcel.model.vo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ResetPasswordVO {
    private String username;
    private String verifyCode;
    private String newPassword;
    private String newPasswordVerify;
}
