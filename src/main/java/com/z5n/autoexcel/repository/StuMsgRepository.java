package com.z5n.autoexcel.repository;

import com.z5n.autoexcel.model.entity.StuMsg;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author z5n
 */
@Repository
public interface StuMsgRepository extends BaseRepository<StuMsg, Integer> {
    List<StuMsg> findByTemplateId(Integer templateId);
}
