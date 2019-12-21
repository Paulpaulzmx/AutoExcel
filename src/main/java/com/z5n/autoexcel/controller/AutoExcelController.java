package com.z5n.autoexcel.controller;

import com.z5n.autoexcel.bean.Result;
import com.z5n.autoexcel.bean.StuMsg;
import com.z5n.autoexcel.service.StuMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
public class AutoExcelController {

    @Resource
    private StuMsgService stuMsgService;

//    @GetMapping("/hello")
//    public String sayHello(@RequestParam(name = "name") String name, Model model) {
//        model.addAttribute("name", name);
//        return "hello";
//    }

    @PostMapping("/submit")
    @ResponseBody
//    public Result submit(HttpServletRequest httpServletRequest) {     //测试Http请求用
    public Result submit(StuMsg stuMsg){
        System.out.println(stuMsg);
//存入数据库、excel设置为定时任务（每隔一段时间将新的请求写入excel表中，数量达标后自动生成excel）
        //提交记录存入数据库
        stuMsgService.save(stuMsg);

        Result result = new Result();
        result.setResultCode(200);
        result.setStatus("ok");
        return result;
    }

    /**
     * 请求主页
     */
    @GetMapping("/")
    public String index() {
        return "register";
    }
}
