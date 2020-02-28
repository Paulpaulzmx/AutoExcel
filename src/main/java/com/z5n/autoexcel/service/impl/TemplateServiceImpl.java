package com.z5n.autoexcel.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.fastjson.JSON;
import com.z5n.autoexcel.excel.TemplateDataListener;
import com.z5n.autoexcel.exception.BusinessException;
import com.z5n.autoexcel.model.entity.Template;
import com.z5n.autoexcel.repository.TemplateRepository;
import com.z5n.autoexcel.service.TemplateService;
import com.z5n.autoexcel.service.base.AbstractCurdService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @program: autoexcel
 * @ClassName: TemplateServiceImpl
 * @Description: TemplateServiceImpl
 * @Author: chen qi
 * @Date: 2019/12/22 19:42
 * @Version: 1.0
 **/
@Slf4j
@Service
public class TemplateServiceImpl extends AbstractCurdService<Template, Integer> implements TemplateService {

    private final TemplateRepository templateRepository;

    public TemplateServiceImpl(TemplateRepository templateRepository) {
        super(templateRepository);
        this.templateRepository = templateRepository;
    }

    @Override
    public Template readExcelHeadTemplate(MultipartFile file, Integer uploaderId) {

        String origName = file.getOriginalFilename();
        if (StringUtils.isEmpty(origName)) {
            throw new BusinessException("文件原名称为空");
        }
        // 判断文件类型
        String type = origName.contains(".") ? origName.substring(origName.lastIndexOf(".")) : null;

        if (StringUtils.isEmpty(type)) {
            throw new BusinessException("文件类型为空");
        }

        type = type.toLowerCase().trim();
        if (!ExcelTypeEnum.XLSX.getValue().equals(type) && !ExcelTypeEnum.XLS.getValue().equals(type)) {
            throw new BusinessException("文件类型不正确");
        }

        TemplateDataListener dataListener = new TemplateDataListener();
        try {
            EasyExcel.read(file.getInputStream(), dataListener).headRowNumber(2).sheet().doRead();
        } catch (IOException e) {
            log.error("TemplateServiceImpl_readExcelHeadTemplate_excel读取异常 : ", e);
        }

        List<Map<Integer, String>> headList = dataListener.getHeadList();
        if (CollectionUtils.isEmpty(headList)) {
            throw new BusinessException("读取数据为空");
        }

        Map<Integer, String> map = headList.get(0);
        if (CollectionUtils.isEmpty(map)) {
            throw new BusinessException("读取表头数据为空");
        }

        Template template = new Template();
        template.setHeadContent(JSON.toJSONString(map));
        template.setUploaderId(uploaderId);
        return templateRepository.save(template);
    }

    @Override
    public List<Template> getExcelList() {
        return templateRepository.findAll();
    }


}
