package com.z5n.autoexcel.controller;

import com.z5n.autoexcel.service.SubmitMsgService;
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

    private final SubmitMsgService submitMsgService;

    public PageController(SubmitMsgService submitMsgService) {
        this.submitMsgService = submitMsgService;
    }


    /**
     *  访问"/+id"获取填写模板
     * @id 模板id
     * @return 主页
     */
    @ApiOperation("填表人获取填写表单的接口")
    @RequestMapping(value = "/user/inputMsg/{id}", method = RequestMethod.GET)
    public String index(){
        return "input";
    }

    /**
     * 请求excel纵览页面
     */
    @ApiOperation("管理员查看所有表格纵览的接口")
    @RequestMapping(value = "/admin/excels", method = RequestMethod.GET)
    public String excels(){
        return "admin/excels";
    }


    /**
     * 管理员请求全部提交历史
     */
    @ApiOperation("管理员查看所有表格纵览的接口")
    @RequestMapping(value = "/admin/history", method = RequestMethod.GET)
    public String history(){
        return "admin/history";
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

    @ApiOperation("编辑模板")
    @RequestMapping(value = "/user/editExcel/{id}", method = RequestMethod.GET)
    public String editTemplate(){
        return "user/editExcel";
    }

    @ApiOperation("个人信息修改")
    @RequestMapping(value = "/user/editPersonInfo", method = RequestMethod.GET)
    public String editPersonInfo(){
        return "user/editPersonInfo";
    }


}
