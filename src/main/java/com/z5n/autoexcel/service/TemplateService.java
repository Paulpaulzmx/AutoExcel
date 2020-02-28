package com.z5n.autoexcel.service;

import com.z5n.autoexcel.model.entity.Template;
import com.z5n.autoexcel.service.base.CurdService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @program: autoexcel
 * @Interface: TemplateService
 * @Description: TemplateService
 * @Author: chen qi
 * @Date: 2019/12/22 19:41
 * @Version: 1.0
 **/
public interface TemplateService extends CurdService<Template, Integer> {

    /**
     *  从excel文件中读取头形成模板
     * @param file
     * @param uploaderId
     * @return
     */
    Template readExcelHeadTemplate(MultipartFile file, Integer uploaderId);


    List<Template> getExcelList();
}
