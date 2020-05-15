package com.z5n.autoexcel.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Data
@ToString
public class MailVo {
        private String id;
        private String from;
        private String to;
        private String subject;
        private String text;
        private Date sentDate;
        private String verifyCode;
//        private String cc;
//        private String bcc;
        private String status;
        private String error;
}
