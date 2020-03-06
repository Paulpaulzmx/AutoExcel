package com.z5n.autoexcel.controller;

import com.alibaba.fastjson.JSONObject;
import com.z5n.autoexcel.exception.BusinessException;
import com.z5n.autoexcel.model.ResultBody;
import com.z5n.autoexcel.model.entity.StuMsg;
import com.z5n.autoexcel.model.entity.Template;
import com.z5n.autoexcel.service.StuMsgService;
import com.z5n.autoexcel.service.TemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


/**
 * 涉及操作的Controller
 */
@Slf4j
@Api(tags = "涉及到操作的接口")
@RestController
public class AutoExcelController {

    private final TemplateService templateService;
    private final StuMsgService stuMsgService;

    public AutoExcelController(TemplateService templateService, StuMsgService stuMsgService) {
        this.templateService = templateService;
        this.stuMsgService = stuMsgService;
    }


    /**
     * 上传表格作为模板
     *
     * @param file
     * @param uploaderId 上传者id
     * @return 是否成功
     */
    @ApiOperation("执行上传表格的接口")
    @RequestMapping(value = "uploadTemplate", method = RequestMethod.POST)
    public ResultBody uploadTemplate(MultipartFile file, Integer uploaderId) {
        if (file.isEmpty() || uploaderId == null) {
            throw new BusinessException("参数不能为空");
        }

        Template template = templateService.readExcelHeadTemplate(file, uploaderId);

        return ResultBody.success(template);
    }


    /**
     * basis页面请求表头生成填表页面
     *
     * @param id 请求的表格id
     * @return 返回表格id为id的表头
     */
    @ApiOperation("页面自动获取表格模板的接口")
    @ApiImplicitParam(name = "id", required = true, value = "模板id")
    @RequestMapping(value = "/template/{id}", method = RequestMethod.GET)
    public ResultBody getTemplateById(@PathVariable("id") Integer id) {
        if (id == null) {
            throw new BusinessException("参数不能为空");
        }

        Template template = templateService.getById(id);
        return ResultBody.success(template.getHeadContent());

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
        JSONObject jsonObject = JSONObject.parseObject(submitMsg);
        StuMsg stuMsg = stuMsgService.submitMsg(jsonObject);
        return ResultBody.success(stuMsg);
    }


    /**
     * 获取所有表的统计信息，用于管理员对表执行下一步的操作
     * @return 返回所有表的统计详情
     */
    @ApiOperation("获取所有表格信息的接口")
    @RequestMapping(value = "/getAllExcel", method = RequestMethod.GET)
    public ResultBody getAllExcel() {
        List<Template> excelList = templateService.getExcelList();
        return ResultBody.success(excelList);
    }


    /**
     * 收集完成后，请求生成表格
     *
     * @param id 请求生成的表格id
     * @return 返回excel表格
     */
    @ApiOperation("请求导出生成表格的接口")
    @ApiImplicitParam(name = "id", required = true, value = "表格id")
    @RequestMapping(value = "/downloadExcel/{id}", method = RequestMethod.GET)
    public ResultBody downloadExcel(@PathVariable("id") Integer id) {
        if (id == null) {
            throw new BusinessException("id不能为空");
        }
        return ResultBody.success(stuMsgService.getMsgByTemplateId(id));
        // 导出整合好数据的excel
    }
}
