package com.z5n.autoexcel.controller;

import com.alibaba.fastjson.JSONObject;
import com.z5n.autoexcel.model.ResultBody;
import com.z5n.autoexcel.model.entity.StuMsg;
import com.z5n.autoexcel.service.StuMsgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * 用于控制页面切换的Controller，未使用RestController
 */
@Slf4j
@Controller
public class PageController {

    private final StuMsgService stuMsgService;

    public PageController(StuMsgService stuMsgService) {
        this.stuMsgService = stuMsgService;
    }


    /**
     * @id 模板id
     * @return 主页
     */
    // 访问"/+id"获取填写模板
    @RequestMapping(value = "/goBasis/{id}", method = RequestMethod.GET)
    public String index(){
        return "basis";
    }

    @RequestMapping(value = "/excels", method = RequestMethod.GET)
    public String excels(){
        return "excels";
    }

}
