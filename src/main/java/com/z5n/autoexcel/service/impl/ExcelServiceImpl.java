package com.z5n.autoexcel.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.fastjson.JSON;
import com.z5n.autoexcel.excel.ExcelDataListener;
import com.z5n.autoexcel.exception.BusinessException;
import com.z5n.autoexcel.model.entity.Excel;
import com.z5n.autoexcel.repository.ExcelRepository;
import com.z5n.autoexcel.service.ExcelService;
import com.z5n.autoexcel.service.base.AbstractCurdService;
import com.z5n.autoexcel.utils.UuidUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @program: autoexcel
 * @ClassName: ExcelServiceImpl
 * @Description: ExcelServiceImpl
 * @Author: chen qi
 * @Date: 2019/12/22 19:42
 * @Version: 1.0
 **/
@Slf4j
@Service
public class ExcelServiceImpl extends AbstractCurdService<Excel, String> implements ExcelService {

    private final ExcelRepository excelRepository;

    public ExcelServiceImpl(ExcelRepository excelRepository) {
        super(excelRepository);
        this.excelRepository = excelRepository;
    }

    @Override
    public Excel readExcelHeadExcel(MultipartFile file, String uploaderId) {

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

        ExcelDataListener dataListener = new ExcelDataListener();
        try {
            EasyExcel.read(file.getInputStream(), dataListener).headRowNumber(3).sheet().doRead();
        } catch (IOException e) {
            log.error("ExcelServiceImpl_readExcelHeadExcel_excel读取异常 : ", e);
        }

        List<Map<Integer, String>> headList = dataListener.getHeadList();
        if (CollectionUtils.isEmpty(headList)) {
            throw new BusinessException("读取数据为空");
        }

        Map<Integer, String> map = headList.get(1);
        if (CollectionUtils.isEmpty(map)) {
            throw new BusinessException("读取表头数据为空");
        }
        String title = headList.get(0).get(0);
        Excel excel = new Excel();
        excel.setUuid(UuidUtils.randomUUIDWithoutDash());
        excel.setTitle(title);
        excel.setHeadContent(JSON.toJSONString(map));
        excel.setUploaderId(uploaderId);
        excel.setFileName(origName.substring(0,origName.lastIndexOf(".")));
        return excelRepository.save(excel);
    }

    @Override
    public List<Excel> getExcelList() {
        try {
            Sort sortByCreateTimeDesc = Sort.by(Sort.Direction.DESC, "createTime");
            return excelRepository.findAll(sortByCreateTimeDesc);
        }catch (Exception e){
            log.error(e.getMessage());
            throw new BusinessException("降序查询全部excel出错");
        }
    }

    @Override
    public List<Excel> getExcelListByUploaderIdSortByCreateTimeDesc(String uploaderId) {
        try{
            Sort sortByCreateTimeDesc = Sort.by(Sort.Direction.DESC, "createTime");
            return excelRepository.findAllByUploaderIdAndDeleted(uploaderId, false, sortByCreateTimeDesc);
        }catch (Exception e){
            log.error(e.getMessage());
            throw new BusinessException("根据上传者id降序查询excel出错");
        }
    }

    @Override
    public Excel updateExcel(String excelId, String newExcel) {
        Excel excel = excelRepository.findByUuid(excelId);
        if(excel == null){
            throw new BusinessException("excelId错误");
        }else{
            excel.setHeadContent(newExcel);
            Excel save = excelRepository.save(excel);
            return save;
        }
    }

    @Override
    public Excel removeById(String excelId) {
        try {
            Excel excel = excelRepository.findByUuid(excelId);
            excel.setDeleted(true);
            return excelRepository.save(excel);
        }catch (Exception e){
            throw new BusinessException("删除出错");
        }
    }
}
