package com.z5n.autoexcel.controller;

import com.z5n.autoexcel.service.StuMsgService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


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
    public String index(){
        return "basis";
    }
    //zmxtodo 重命名函数名

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
    @RequestMapping(value = "/uploadTemplate", method=RequestMethod.GET)
    public String upload(){
        return "uploadTemplate";
    }

    @ApiOperation("项目首页")
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String indexPage(){
        return "index";
    }



    @ApiOperation("注册账号")
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String register(){
        return "register";
    }

    @ApiOperation("个人主页")
    @RequestMapping(value = "/user/index", method = RequestMethod.GET)
    public String userIndex(){
        return "user/index";
    }

    @ApiOperation("填表历史")
    @RequestMapping(value = "/user/history", method = RequestMethod.GET)
    public String myHistory(){
        return "user/history";
    }

    @ApiOperation("我的模板")
    @RequestMapping(value = "/user/myExcels", method = RequestMethod.GET)
    public String myExcels(){
        return "user/myExcels";
    }

    @ApiOperation("我的模板")
    @RequestMapping(value = "/user/editTemplate/{id}", method = RequestMethod.GET)
    public String editTemplate5(){
        return "user/editTemplate";
    }

    @ApiOperation("测试用")
    @RequestMapping(value = "/submit", method = RequestMethod.GET)
    public String test(){
        return "submit";
    }


}
