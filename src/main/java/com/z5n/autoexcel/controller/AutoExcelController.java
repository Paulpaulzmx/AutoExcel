package com.z5n.autoexcel.controller;

import com.alibaba.excel.EasyExcel;
import com.z5n.autoexcel.exception.BusinessException;
import com.z5n.autoexcel.model.ResultBody;
import com.z5n.autoexcel.model.entity.StuMsg;
import com.z5n.autoexcel.model.entity.Template;
import com.z5n.autoexcel.service.StuMsgService;
import com.z5n.autoexcel.service.TemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
public class AutoExcelController {

    private final StuMsgService stuMsgService;
    private final TemplateService templateService;

    public AutoExcelController(StuMsgService stuMsgService,
                               TemplateService templateService) {
        this.stuMsgService = stuMsgService;
        this.templateService = templateService;
    }

    @PostMapping("/submit")
    public ResultBody submit(StuMsg stuMsg) {
        //todo 使用参数实体配合spring校验
        System.out.println(stuMsg);
        //存入数据库、excel设置为定时任务（每隔一段时间将新的请求写入excel表中，数量达标后自动生成excel）
        //提交记录存入数据库
        stuMsgService.create(stuMsg);

        return ResultBody.success(stuMsg);
    }

    @PostMapping("uploadTemplate")
    public ResultBody uploadTemplate(MultipartFile file, Integer stuInfoId) {
        if (file.isEmpty() || stuInfoId == null) {
            throw new BusinessException("参数不能为空");
        }

        Template template = templateService.readExcelHeadTemplate(file, stuInfoId);

        return ResultBody.success(template);
    }

    @RequestMapping(value = "/template/{id}", method = RequestMethod.GET)
    public ResultBody getTemplateById(@PathVariable("id") Integer id) {
        if (id == null) {
            throw new BusinessException("参数不能为空");
        }

        Template template = templateService.getById(id);
        // 返回所有的表头
        return ResultBody.success(template.getHeadContent());

    }


    @GetMapping("/getExcel")
    public void getExcel() {
        // 导出整合好数据的excel
    }
}
