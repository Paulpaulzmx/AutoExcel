package com.z5n.autoexcel.repository;

import com.z5n.autoexcel.model.entity.Excel;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * @program: autoexcel
 * @Interface: TemplateRepository
 * @Description: TemplateRepository
 * @Author: chen qi
 * @Date: 2019/12/22 19:43
 * @Version: 1.0
 **/
@Repository
public interface ExcelRepository extends BaseRepository<Excel, String> {
    List<Excel> findAllByUploaderIdAndDeleted(String uploaderId, boolean deleted, Sort sortByCreateTime);
    Excel findByUuid(String templateId);
}
