package com.z5n.autoexcel.controller;

import com.z5n.autoexcel.bean.Result;
import com.z5n.autoexcel.bean.StuMsg;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AutoExcelController {
    @GetMapping("/hello")
    public String sayHello(@RequestParam(name = "name") String name, Model model) {
        model.addAttribute("name", name);
        return "hello";
    }

    @PostMapping("/submit")
    @ResponseBody
//    public Result submit(HttpServletRequest httpServletRequest) {
    public Result submit(StuMsg stuMsg){
        System.out.println(stuMsg);
//存入数据库、excel设置为定时任务（每隔一段时间将新的请求写入excel表中，数量达标后自动生成excel）
        Result result = new Result();
        return result;
    }

    @GetMapping("/")
    public String index() {
        return "register";
    }
}
