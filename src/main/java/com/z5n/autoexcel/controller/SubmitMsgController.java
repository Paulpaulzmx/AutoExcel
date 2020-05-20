package com.z5n.autoexcel.controller;

import com.alibaba.fastjson.JSONObject;
import com.z5n.autoexcel.config.security.IAuthenticationFacade;
import com.z5n.autoexcel.exception.BusinessException;
import com.z5n.autoexcel.model.ResultBody;
import com.z5n.autoexcel.model.entity.Excel;
import com.z5n.autoexcel.model.entity.SubmitMsg;
import com.z5n.autoexcel.model.entity.User;
import com.z5n.autoexcel.model.entity.UserInfo;
import com.z5n.autoexcel.model.vo.MyHistoryVo;
import com.z5n.autoexcel.service.ExcelService;
import com.z5n.autoexcel.service.SubmitMsgService;
import com.z5n.autoexcel.service.UserInfoService;
import com.z5n.autoexcel.service.UserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@ApiOperation("操作提交信息的接口")
@RestController
public class SubmitMsgController {

    private final ExcelService excelService;
    private final SubmitMsgService submitMsgService;
    private final UserService userService;
    private final UserInfoService userInfoService;
    private final IAuthenticationFacade authenticationFacade;


    public SubmitMsgController(ExcelService excelService, SubmitMsgService submitMsgService, UserService userService, UserInfoService userInfoService, IAuthenticationFacade authenticationFacade) {
        this.excelService = excelService;
        this.submitMsgService = submitMsgService;
        this.userService = userService;
        this.userInfoService = userInfoService;
        this.authenticationFacade = authenticationFacade;
    }


    /**
     * basis页面请求表头生成填表页面
     *
     * @param id 请求的表格id
     * @return 返回表格id为id的表头
     */
    @ApiOperation("页面自动获取表格模板的接口")
    @ApiImplicitParam(name = "id", required = true, value = "模板id")
    @RequestMapping(value = "/user/excel/{id}", method = RequestMethod.GET)
    public ResultBody getExcelById(@PathVariable("id") String id) {
        try {
            Excel excel = excelService.getById(id);
            return ResultBody.success(excel.getHeadContent());
        } catch (Exception e) {
            return ResultBody.error(e.getMessage());
        }
    }

    /** 获取用户附加的个人信息，用于自动填表
     * @return 当前用户的个人信息
     */
    @ApiOperation("获取用户附加的个人信息，自动填表")
    @RequestMapping(value = "/user/personalInfo", method = RequestMethod.GET)
    public ResultBody getPersonalInfo(){
        Authentication authentication = authenticationFacade.getAuthentication();
        User currentUser = userService.findUserByUsername(authentication.getName());
        try {
            UserInfo userInfo = userInfoService.getUserInfoByUserId(currentUser.getUuid());
            return ResultBody.success(userInfo);
        }catch (BusinessException e){
            return ResultBody.error(e.getMessage());
        }
    }


    /**
     * (填表人)提交一条信息
     *
     * @param submitMsg json形式的信息
     * @return 是否成功
     */
    @ApiOperation("填表人提交一次信息的接口")
    @ApiImplicitParam(name = "submitMsg", required = true, value = "json字符串形式，包括模板id和表格具体内容")
    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    public ResultBody submit(@RequestBody String submitMsg) {
        Authentication authentication = authenticationFacade.getAuthentication();
        User currentUser = userService.findUserByUsername(authentication.getName());
        try {
            JSONObject jsonObject = JSONObject.parseObject(submitMsg);
            String excelId = jsonObject.getString("excelId");
            //重复提交查询
            if (submitMsgService.checkIsSubmitted(currentUser.getUuid(), excelId)) {
                return ResultBody.error("您已提交过此表单，请不要重复提交！");
            }
            //存储提交内容
            SubmitMsg stuMsg = submitMsgService.submitMsg(currentUser.getUuid(), jsonObject);
            return ResultBody.success(stuMsg);
        } catch (BusinessException e) {
            return ResultBody.error(e.getMessage());
        }
    }


    /**
     * 删除一条提交过的信息
     * @param msgId 要删除的msgId
     * @return
     */
    @ApiOperation("删除用户提交过的SubmitMsg")
    @RequestMapping(value = "/user/deleteSubmitMsg", method = RequestMethod.POST)
    public ResultBody deleteSubmitMsg(@RequestParam("submitMsgId")String msgId) {
        try {
            SubmitMsg submitMsg = submitMsgService.removeById(msgId);
            return ResultBody.success(submitMsg);
        } catch (Exception e) {
            return ResultBody.error(e.getMessage());
        }
    }


    /**
     * 查看填表历史————获取某用户提交过的所有信息
     */
    @ApiOperation("获取用户提交过的SubmitMsg")
    @RequestMapping(value = "/user/getMySubmit", method = RequestMethod.GET)
    public ResultBody getMySubmit() {
        Authentication authentication = authenticationFacade.getAuthentication();
        User currentUser = userService.findUserByUsername(authentication.getName());
        try {
            List<SubmitMsg> submitMsgs =
                    submitMsgService.getValidSubmitMsgByFillerIdSortByUpdateTimeDesc(currentUser.getUuid());
            List<MyHistoryVo> historyVos = new ArrayList<>();
            String temp;
            for (SubmitMsg submitMsg : submitMsgs) {
                Excel excel = excelService.getById(submitMsg.getExcelId());
                MyHistoryVo myHistoryVo = new MyHistoryVo();
                myHistoryVo.setFileName(excel.getFileName());
                myHistoryVo.setTitle(excel.getTitle());
                myHistoryVo.setHead(excel.getHeadContent());
                myHistoryVo.setUuid(submitMsg.getUuid());
                myHistoryVo.setContent(submitMsg.getContent());
                temp = submitMsg.getUpdateTime().toString();
                myHistoryVo.setUpdateTimeStr(temp.substring(0, temp.lastIndexOf(".")));
                historyVos.add(myHistoryVo);
            }
            return ResultBody.success(historyVos);
        } catch (BusinessException e) {
            return ResultBody.error(e.getMessage());
        }
    }


}
