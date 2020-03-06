package com.z5n.autoexcel.controller;

import com.alibaba.fastjson.JSONObject;
import com.z5n.autoexcel.model.ResultBody;
import com.z5n.autoexcel.model.entity.StuMsg;
import com.z5n.autoexcel.service.StuMsgService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 用于控制页面切换的Controller，未使用RestController
 */
@Slf4j
@Api(tags = "访问页面的接口")
@Controller
public class PageController {

    private final StuMsgService stuMsgService;

    public PageController(StuMsgService stuMsgService) {
        this.stuMsgService = stuMsgService;
    }


    /**
     *  访问"/+id"获取填写模板
     * @id 模板id
     * @return 主页
     */
    @ApiOperation("填表人获取填写表单的接口")
    @RequestMapping(value = "/goBasis/{id}", method = RequestMethod.GET)
    @ResponseBody
    public String index(){
        return "basis";
    }

    /**
     * 请求excel纵览页面
     */
    @ApiOperation("管理员查看所有表格纵览的接口")
    @RequestMapping(value = "/excels", method = RequestMethod.GET)
    public String excels(){
        return "excels";
    }

    /**
     * 请求上传页面
     */
    @ApiOperation("管理员获取上传表单页面接口")
    @RequestMapping(value = "/upload", method=RequestMethod.GET)
    public String upload(){
        return "upload";
    }

}
