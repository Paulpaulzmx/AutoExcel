package com.z5n.autoexcel.repository;

import com.z5n.autoexcel.model.entity.Template;
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
public interface TemplateRepository extends BaseRepository<Template, String> {
    List<Template> findAllByUploaderId(String uploaderId);
    Template findByUuid(String templateId);
}
