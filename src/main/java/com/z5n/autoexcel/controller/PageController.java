package com.z5n.autoexcel.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Slf4j
@Controller
public class PageController {

    /**
     * @id 模板id
     * @return 主页
     */
    // 访问"/+id"获取填写模板
    @GetMapping(value = "/goBasis/{id}")
    public String index(){
        return "basis";
    }
}
