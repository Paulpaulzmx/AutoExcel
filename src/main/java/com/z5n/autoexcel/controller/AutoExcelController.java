package com.z5n.autoexcel.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSONObject;
import com.z5n.autoexcel.exception.BusinessException;
import com.z5n.autoexcel.model.ResultBody;
import com.z5n.autoexcel.model.entity.SubmitMsg;
import com.z5n.autoexcel.model.entity.Excel;
import com.z5n.autoexcel.model.vo.ExcelVo;
import com.z5n.autoexcel.service.ExcelService;
import com.z5n.autoexcel.service.SubmitMsgService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


/**
 * 涉及操作的Controller
 */
@Slf4j
@Api(tags = "涉及到操作的接口")
@RestController
public class AutoExcelController {

    private final ExcelService excelService;
    private final SubmitMsgService submitMsgService;


    public AutoExcelController(ExcelService excelService, SubmitMsgService submitMsgService) throws FileNotFoundException {
        this.excelService = excelService;
        this.submitMsgService = submitMsgService;
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
}
