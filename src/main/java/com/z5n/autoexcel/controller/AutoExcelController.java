package com.z5n.autoexcel.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.z5n.autoexcel.excel.TemplateDataListener;
import com.z5n.autoexcel.model.Result;
import com.z5n.autoexcel.model.entity.StuMsg;
import com.z5n.autoexcel.service.StuMsgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
public class AutoExcelController {

    private final StuMsgService stuMsgService;

    public AutoExcelController(StuMsgService stuMsgService) {
        this.stuMsgService = stuMsgService;
    }

    @PostMapping("/submit")
    public Result submit(StuMsg stuMsg) {
        //todo 使用参数实体配合spring校验
        System.out.println(stuMsg);
        //存入数据库、excel设置为定时任务（每隔一段时间将新的请求写入excel表中，数量达标后自动生成excel）
        //提交记录存入数据库
        stuMsgService.create(stuMsg);

        return Result.success(stuMsg);
    }

    @PostMapping("uploadTemplate")
    public Result uploadTemplate(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return Result.error("文件为空");
        }

        String origName = file.getOriginalFilename();
        if (origName == null) {
            return Result.error();
        }
        // 判断文件类型
        String type = origName.contains(".") ? origName.substring(origName.lastIndexOf(".") + 1) : null;

        if (type == null) {
            return Result.error();
        }

        type = type.toLowerCase().trim();
        if (!ExcelTypeEnum.XLSX.getValue().equals(type) && !ExcelTypeEnum.XLS.getValue().equals(type)) {
            return Result.error();
        }

        try {
            EasyExcel.read(file.getInputStream(), new TemplateDataListener()).sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Result.success();
    }

}
