package com.z5n.autoexcel.controller;

import com.z5n.autoexcel.model.Result;
import com.z5n.autoexcel.model.entity.StuMsg;
import com.z5n.autoexcel.service.StuMsgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
public class AutoExcelController {

    private final StuMsgService stuMsgService;

    public AutoExcelController(StuMsgService stuMsgService) {
        this.stuMsgService = stuMsgService;
    }

    @PostMapping("/submit")
    @ResponseBody
    public Result submit(StuMsg stuMsg) {
        //todo 使用参数实体配合spring校验
        System.out.println(stuMsg);
        //存入数据库、excel设置为定时任务（每隔一段时间将新的请求写入excel表中，数量达标后自动生成excel）
        //提交记录存入数据库
        stuMsgService.create(stuMsg);

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
