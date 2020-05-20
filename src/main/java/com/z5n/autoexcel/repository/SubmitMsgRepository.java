package com.z5n.autoexcel.repository;

import com.z5n.autoexcel.model.entity.SubmitMsg;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author z5n
 */
@Repository
public interface SubmitMsgRepository extends BaseRepository<SubmitMsg, String> {
    SubmitMsg findByUuid(String uuid);
    List<SubmitMsg> findByExcelIdAndDeleted(String excelId, boolean deleted, Sort sort);
    List<SubmitMsg> findByFillerIdAndDeleted(String fillerId, boolean deleted, Sort sort);
    List<SubmitMsg> findAllByDeleted(boolean deleted, Sort sort);
    int countAllByExcelIdAndDeleted(String excelId, boolean deleted);
    List<SubmitMsg> findByFillerIdAndExcelIdAndDeleted(String fillerId, String excelId, boolean deleted);
}
