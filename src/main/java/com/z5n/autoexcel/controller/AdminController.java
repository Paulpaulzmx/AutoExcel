package com.z5n.autoexcel.controller;

import com.z5n.autoexcel.exception.BusinessException;
import com.z5n.autoexcel.model.ResultBody;
import com.z5n.autoexcel.model.entity.Excel;
import com.z5n.autoexcel.model.entity.SubmitMsg;
import com.z5n.autoexcel.model.entity.User;
import com.z5n.autoexcel.model.vo.ExcelVo;
import com.z5n.autoexcel.model.vo.MyHistoryVo;
import com.z5n.autoexcel.service.ExcelService;
import com.z5n.autoexcel.service.SubmitMsgService;
import com.z5n.autoexcel.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;


/**
 * 涉及操作的Controller
 */
@Slf4j
@Api(tags = "涉及到管理员操作的接口")
@RestController
public class AdminController {

    private final ExcelService excelService;
    private final SubmitMsgService submitMsgService;
    private final UserService userService;


    public AdminController(ExcelService excelService, SubmitMsgService submitMsgService, UserService userService) throws FileNotFoundException {
        this.excelService = excelService;
        this.submitMsgService = submitMsgService;
        this.userService = userService;
    }


    /**
     * 获取所有表的统计信息，用于管理员对表执行下一步的操作
     *
     * @return 返回所有表的统计详情
     */
    @ApiOperation("获取所有表格信息的接口")
    @RequestMapping(value = "/admin/getAllExcel", method = RequestMethod.GET)
    public ResultBody getAllExcel() {
        List<Excel> excelList = excelService.getExcelList();
        try {
            List<ExcelVo> excelVoList = new ArrayList<>();
            int count;
            String temp;
            for (Excel excel : excelList) {
                ExcelVo excelVo = new ExcelVo();
                count = submitMsgService.countMsgByExcelId(excel.getUuid());
                excelVo.setSubmitNum(count);
                excelVo.setUuid(excel.getUuid());
                excelVo.setFileName(excel.getFileName());
                excelVo.setHeadContent(excel.getHeadContent());
                temp = excel.getCreateTime().toString();
                excelVo.setCreateTimeStr(temp.substring(0, temp.lastIndexOf(".")));
                temp = excel.getUpdateTime().toString();
                excelVo.setUpdateTimeStr(temp.substring(0, temp.lastIndexOf(".")));
                excelVoList.add(excelVo);
            }
            return ResultBody.success(excelVoList);
        } catch (BusinessException e){
            return ResultBody.error(e.getMessage());
        }
    }

    /**管理员查看提交历史页面，查看全部提交过的信息
     * @return 所有提交过的信息
     */
    @ApiOperation("获取所有提交信息的接口")
    @RequestMapping(value = "/admin/getAllSubmitMsg", method = RequestMethod.GET)
    public ResultBody getAllSubmitMsg() {
        try {
            List<SubmitMsg> submitMsgs = submitMsgService.getAllValidSubmitMsgSortByUpdatetimeDesc();
            List<MyHistoryVo> historyVos = new ArrayList<>();
            String temp;
            for (SubmitMsg submitMsg : submitMsgs) {
                Excel excel = excelService.getById(submitMsg.getExcelId());
                User user = userService.findUserById(submitMsg.getFillerId());
                MyHistoryVo myHistoryVo = new MyHistoryVo();
                myHistoryVo.setFileName(excel.getFileName());
                myHistoryVo.setTitle(excel.getTitle());
                myHistoryVo.setHead(excel.getHeadContent());
                myHistoryVo.setUuid(submitMsg.getUuid());
                myHistoryVo.setFillerName(user.getName());
                myHistoryVo.setContent(submitMsg.getContent());
                temp = submitMsg.getUpdateTime().toString();
                myHistoryVo.setUpdateTimeStr(temp.substring(0, temp.lastIndexOf(".")));
                historyVos.add(myHistoryVo);
            }
            return ResultBody.success(historyVos);
        }catch (BusinessException e){
            return ResultBody.error(e.getMessage());
        }
    }
}
