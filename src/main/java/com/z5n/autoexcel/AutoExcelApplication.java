package com.z5n.autoexcel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class AutoExcelApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(AutoExcelApplication.class, args);
    }

}
