package com.z5n.autoexcel.config.security;

import com.alibaba.fastjson.JSON;
import com.z5n.autoexcel.model.ResultBody;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class MyAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        log.info("登陆成功");
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(ResultBody.success().toString());
        // 会帮我们跳转到上一次请求的页面上
//        super.onAuthenticationSuccess(request, response, authentication);

    }
}
