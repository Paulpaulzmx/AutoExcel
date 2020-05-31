package com.z5n.autoexcel.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSONObject;
import com.z5n.autoexcel.config.security.IAuthenticationFacade;
import com.z5n.autoexcel.exception.BusinessException;
import com.z5n.autoexcel.model.ResultBody;
import com.z5n.autoexcel.model.entity.Excel;
import com.z5n.autoexcel.model.entity.SubmitMsg;
import com.z5n.autoexcel.model.entity.User;
import com.z5n.autoexcel.model.vo.ExcelVo;
import com.z5n.autoexcel.service.ExcelService;
import com.z5n.autoexcel.service.SubmitMsgService;
import com.z5n.autoexcel.service.UserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
public class ExcelController {

    private final ExcelService excelService;
    private final UserService userService;
    private final SubmitMsgService submitMsgService;
    private final IAuthenticationFacade authenticationFacade;

    private final String excelDir = ResourceUtils.getURL("classpath:").getPath() + "../../Excels/";


    public ExcelController(ExcelService excelService,
                           UserService userService,
                           SubmitMsgService submitMsgService,
                           IAuthenticationFacade authenticationFacade) throws FileNotFoundException {
        this.excelService = excelService;
        this.userService = userService;
        this.submitMsgService = submitMsgService;
        this.authenticationFacade = authenticationFacade;
    }


    /**
     * 上传表格作为模板
     *
     * @param file
     * @return 是否成功
     */
    @ApiOperation("执行上传表格的接口")
    @RequestMapping(value = "/uploadExcel", method = RequestMethod.POST)
    public String uploadExcel(MultipartFile file) {

        JSONObject result = new JSONObject();

        Authentication authentication = authenticationFacade.getAuthentication();
        User uploader = userService.findUserByUsername(authentication.getName());

        if (file.isEmpty()) {
            throw new BusinessException("参数不能为空");
        }

        try {
            Excel excel = excelService.readExcelHeadExcel(file, uploader.getUuid());
            result.put("error", null);
            result.put("status", "OK");
            result.put("excelId", excel.getUuid());

            return result.toJSONString();
        } catch (Exception e) {
            result.put("status", "error");
            result.put("error", e.getMessage());
            return result.toJSONString();
        }

    }

    /**
     * 获取普通用户上传过的表格
     *
     * @return 返回显示在bootstrap-table中的内容
     */
    @ApiOperation("获取用户上传过的Excel")
    @RequestMapping(value = "/user/getMyExcel", method = RequestMethod.GET)
    public ResultBody getMyExcel() {
        Authentication authentication = authenticationFacade.getAuthentication();
        User uploader = userService.findUserByUsername(authentication.getName());
        try {
            List<Excel> excelList = excelService.getExcelListByUploaderIdSortByCreateTimeDesc(uploader.getUuid());
            List<ExcelVo> excelVoList = new ArrayList<>();
            int count;
            String temp;
            for (Excel excel : excelList) {
                ExcelVo excelVo = new ExcelVo();
                count = submitMsgService.countMsgByExcelId(excel.getUuid());
                excelVo.setSubmitNum(count);
                excelVo.setUuid(excel.getUuid());
                excelVo.setFileName(excel.getFileName());
                //美化表头字段
                StringBuffer headStringBuffer = new StringBuffer(excel.getHeadContent());
                String[] heads = headStringBuffer.substring(1, headStringBuffer.length() - 1).split(",");
                StringBuffer headContent = new StringBuffer();
                for (String s : heads) {
                    headContent.append("「"+s.substring(s.lastIndexOf(':') + 2, s.length() - 1)+"」");
                }
                excelVo.setHeadContent(headContent.toString());
                temp = excel.getCreateTime().toString();
                excelVo.setCreateTimeStr(temp.substring(0, temp.lastIndexOf(".")));
                temp = excel.getUpdateTime().toString();
                excelVo.setUpdateTimeStr(temp.substring(0, temp.lastIndexOf(".")));
                excelVoList.add(excelVo);
            }
            return ResultBody.success(excelVoList);
        } catch (BusinessException e) {
            return ResultBody.error(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResultBody.error("出现逻辑错误，请查看日志");
        }
    }

    /**
     * 用户更新Excel表头字段
     *
     * @param newExcel 新的表头字段（包括旧表id）
     * @return
     */
    @ApiOperation("在editExcel页面更新excel表头字段")
    @RequestMapping(value = "/updateExcel", method = RequestMethod.POST)
    public ResultBody updateExcel(@RequestBody String newExcel) {
        JSONObject msg = JSONObject.parseObject(newExcel);
        String excelId = msg.getString("excelId");
        msg.remove("excelId");
        try {
            Excel updatedExcel = excelService.updateExcel(excelId, msg.toJSONString());
            return ResultBody.success(updatedExcel);
        } catch (BusinessException e) {
            return ResultBody.error(e.getMessage());
        }
    }


    /**
     * 用户删除曾经上传的excel
     */
    @ApiOperation("用户删除上传的excel")
    @RequestMapping(value = "/user/deleteExcel", method = RequestMethod.POST)
    public ResultBody deleteExcel(@RequestParam("excelId") String excelId) {
        try {
            Excel excel = excelService.removeById(excelId);
            return ResultBody.success(excel);
        } catch (Exception e) {
            return ResultBody.error(e.getMessage());
        }
    }


    /**
     * 收集完成后，生成并下载表格
     *
     * @param id 请求生成的表格id
     * @return 返回excel表格
     */
    @ApiOperation("请求导出生成表格的接口")
    @ApiImplicitParam(name = "id", required = true, value = "表格id")
    @RequestMapping(value = "/user/downloadExcel/{id}", method = RequestMethod.GET)
    public void downloadExcel(@PathVariable("id") String id, HttpServletResponse response) {
        try {
            //获取填充数据
            List<SubmitMsg> submitMsgs = submitMsgService.getValidSubmitMsgByExcelIdSortByUpdateTimeDesc(id);
            //获取表头格式
            Excel currentExcel = excelService.getById(id);
            String title = currentExcel.getTitle();
            String headContent = currentExcel.getHeadContent();
            //新建文件
            File file = new File(excelDir + currentExcel.getFileName() + ".xls");
            file.createNewFile();
            //开始写入
            EasyExcel.write(file)
                    .sheet()
                    .head(getExcelHead(title, headContent))
                    .doWrite(getExcelData(submitMsgs));
            //返回下载内容——设置类型
            String filename = new String(file.getName().getBytes("UTF-8"), "iso-8859-1");
            response.setContentType("application/force-download");
            response.setHeader("Content-Disposition", "attachment; filename="
                    + filename);
            //返回下载内容——写流
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            ServletOutputStream os = response.getOutputStream();
            byte[] buffer = new byte[1024];
            int i = bis.read(buffer);
            while (i != -1) {
                os.write(buffer, 0, i);
                i = bis.read(buffer);
            }
            //关闭资源
            fis.close();
            bis.close();
            os.close();
            os.flush();
        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }

    //设置头
    private List<List<String>> getExcelHead(String title, String headContent) {
        JSONObject jsonHeadContent = JSONObject.parseObject(headContent);
        int size = jsonHeadContent.size();
        List<List<String>> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            List<String> head = new ArrayList<>();
            head.add(title);
            head.add(jsonHeadContent.getString(String.valueOf(i)));
            list.add(head);
        }
        return list;
    }

    //设置填充数据
    private List<List<String>> getExcelData(List<SubmitMsg> submitMsgs) {
        List<List<String>> list = new ArrayList<>();
        for (SubmitMsg submitMsg : submitMsgs) {
            JSONObject jsonSubmitMsg = JSONObject.parseObject(submitMsg.getContent());
            List<String> data = new ArrayList<>();
            int size = jsonSubmitMsg.size();
            for (int i = 0; i < size; i++) {
                data.add(jsonSubmitMsg.getString(String.valueOf(i)));
            }
            list.add(data);
        }
        return list;
    }
}
