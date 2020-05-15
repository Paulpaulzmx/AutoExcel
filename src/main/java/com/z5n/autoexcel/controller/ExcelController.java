package com.z5n.autoexcel.controller;

import com.alibaba.fastjson.JSONObject;
import com.z5n.autoexcel.config.security.IAuthenticationFacade;
import com.z5n.autoexcel.exception.BusinessException;
import com.z5n.autoexcel.model.ResultBody;
import com.z5n.autoexcel.model.entity.Template;
import com.z5n.autoexcel.model.entity.User;
import com.z5n.autoexcel.service.TemplateService;
import com.z5n.autoexcel.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class ExcelController {

    private final TemplateService templateService;
    private final UserService userService;
    private final IAuthenticationFacade authenticationFacade;


    public ExcelController(TemplateService templateService, UserService userService, IAuthenticationFacade authenticationFacade) {
        this.templateService = templateService;
        this.userService = userService;
        this.authenticationFacade = authenticationFacade;
    }


    /**
     * 上传表格作为模板
     * @param file
     * @return 是否成功
     */
    @ApiOperation("执行上传表格的接口")
    @RequestMapping(value = "/uploadTemplate", method = RequestMethod.POST)
    public String uploadTemplate(MultipartFile file) {

        JSONObject result = new JSONObject();

        Authentication authentication = authenticationFacade.getAuthentication();
        User uploader = userService.findUserByUsername(authentication.getName());

        if (file.isEmpty()) {
            throw new BusinessException("参数不能为空");
        }

        try{
            Template template = templateService.readExcelHeadTemplate(file, uploader.getUuid());
            result.put("error", null);
            result.put("status", "OK");
            result.put("templateId", template.getUuid());

            return result.toJSONString();
        }catch (Exception e){
            result.put("status", "error");
            result.put("error", e.getMessage());
            return result.toJSONString();
        }

    }

    @ApiOperation("获取用户上传过的Excel")
    @RequestMapping(value = "/user/getMyExcel", method = RequestMethod.GET)
    public ResultBody getMyExcel(){
        Authentication authentication = authenticationFacade.getAuthentication();
        User uploader = userService.findUserByUsername(authentication.getName());
        try{
            List<Template> excelList = templateService.getExcelList(uploader.getUuid());
            return ResultBody.success(excelList);
        }catch (Exception e){
            return ResultBody.error(e.getMessage());
        }
    }

    @ApiOperation("在editTemplate页面更新template表头字段")
    @RequestMapping(value = "/updateTemplate", method = RequestMethod.POST)
    public ResultBody updateTemplate(@RequestBody String newTemplate){
        JSONObject msg = JSONObject.parseObject(newTemplate);
        String templateId = msg.getString("templateId");
        msg.remove("templateId");
        Template updatedTemplate = templateService.updateTemplate(templateId, msg.toJSONString());
        return ResultBody.success(updatedTemplate);
    }
}
