package com.z5n.autoexcel.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**打印所有到controller中方法的请求日志
 * @author zmx
 * @date 2020/1/6 17:12
 */
@Component
@Aspect
@Slf4j
public class LogAspect {
    private final String POINT_CUT = "execution(public * com.z5n.autoexcel.controller.*.*(..))";

    private long startTime;

    @Pointcut(POINT_CUT)
    public void pointCut() {
    }

    /**
     * 前置通知
     * 获取请求方的ip地址等信息，并写入日志
     */
    @Before(value = "pointCut()")
    public void before() {
        log.info("------------before通知开始------------");

        startTime = System.currentTimeMillis();

        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        log.info(" ### 请求URL: " + request.getRequestURL().toString());
        log.info(" ### IP: " + request.getRemoteAddr());
        log.info(" ### 请求方式: " + request.getMethod());

        log.info("------------before通知结束------------");
    }

    @After(value = "pointCut()")
    public void after() {
        log.info("------------after通知开始------------");

        long time = System.currentTimeMillis() - startTime;
        log.info(" ### 执行时间为:" + time + "ms");

        log.info("------------after通知结束------------");

    }
}
