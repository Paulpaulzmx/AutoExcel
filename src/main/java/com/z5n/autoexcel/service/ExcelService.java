package com.z5n.autoexcel.service;

import com.z5n.autoexcel.model.entity.Excel;
import com.z5n.autoexcel.service.base.CurdService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @program: autoexcel
 * @Interface: ExcelService
 * @Description: ExcelService
 * @Author: chen qi
 * @Date: 2019/12/22 19:41
 * @Version: 1.0
 **/
public interface ExcelService extends CurdService<Excel, String> {

    /**
     *  从excel文件中读取头形成模板
     * @param file
     * @param uploaderId
     * @return
     */
    Excel readExcelHeadExcel(MultipartFile file, String uploaderId);


    /**
     * 获取所有Excel
     */
    List<Excel> getExcelList();

    /**获取特定用户上传的所有Excel
     * @param uploaderId 上传者id
     */
    List<Excel> getExcelListByUploaderIdSortByCreateTimeDesc(String uploaderId);

    Excel updateExcel(String excelId, String newExcel);
}
