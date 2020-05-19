package com.z5n.autoexcel.service;

import com.z5n.autoexcel.exception.BusinessException;
import com.z5n.autoexcel.model.vo.MailVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
@EnableAsync
@Service
public class MailService {

    @Autowired
    private JavaMailSenderImpl mailSender;

    @Autowired
    private RedisTemplate redisTemplate;


    public void sendMail(MailVo mailVo) {
        try {
            checkMail(mailVo);
            sendMimeMail(mailVo);
            saveMail(mailVo);
        } catch (Exception e) {
            log.error("发送邮件失败:", e);
            mailVo.setStatus("fail");
            mailVo.setError(e.getMessage());
            throw new BusinessException("邮件发送失败");
        }

    }


    private void checkMail(MailVo mailVo) {
        if (StringUtils.isEmpty(mailVo.getTo())) {
            throw new RuntimeException("邮件收信人不能为空");
        }
        if (StringUtils.isEmpty(mailVo.getSubject())) {
            throw new RuntimeException("邮件主题不能为空");
        }
        if (StringUtils.isEmpty(mailVo.getText())) {
            throw new RuntimeException("邮件内容不能为空");
        }
    }


    private void sendMimeMail(MailVo mailVo) {
        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mailSender.createMimeMessage(), true);
            mailVo.setFrom(getMailSendFrom());
            messageHelper.setFrom(mailVo.getFrom());
            messageHelper.setTo(mailVo.getTo().split(","));
            messageHelper.setSubject(mailVo.getSubject());
            messageHelper.setText(mailVo.getText());

            if (StringUtils.isEmpty(mailVo.getSentDate())) {
                mailVo.setSentDate(new Date());
                messageHelper.setSentDate(mailVo.getSentDate());
            }

            //将验证码存入redis
            //获取redis操作类
            ValueOperations valueOperations = redisTemplate.opsForValue();
            //设置缓存
            valueOperations.set(mailVo.getTo(),mailVo.getVerifyCode());
            //设置过期时间5min
            redisTemplate.expire(mailVo.getTo(),60*5, TimeUnit.SECONDS);
            mailSender.send(messageHelper.getMimeMessage());
            mailVo.setStatus("ok");
            log.info("发送邮件成功：{}->{}", mailVo.getFrom(), mailVo.getTo());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 验证用户提交的验证码是否正确
     * @param emailAddress 注册用户提供的邮箱
     * @param verifyCode 用户提交的验证码
     */
    public boolean verifying(String emailAddress, String verifyCode){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String vertificationCode = (String) valueOperations.get(emailAddress);
        if(verifyCode.equals(vertificationCode)){
            redisTemplate.delete(emailAddress);
            return true;
        }else {
            return false;
        }
    }


    private MailVo saveMail(MailVo mailVo) {
        return mailVo;
    }


    public String getMailSendFrom() {
        return mailSender.getJavaMailProperties().getProperty("from");
    }
}
