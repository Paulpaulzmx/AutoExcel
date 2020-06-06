package com.z5n.autoexcel.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.z5n.autoexcel.exception.BusinessException;
import com.z5n.autoexcel.model.entity.Excel;
import com.z5n.autoexcel.model.entity.SubmitMsg;
import com.z5n.autoexcel.repository.SubmitMsgRepository;
import com.z5n.autoexcel.service.SubmitMsgService;
import com.z5n.autoexcel.service.base.AbstractCurdService;
import com.z5n.autoexcel.utils.UuidUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: autoexcel
 * @ClassName: SubmitMsgServiceImpl
 * @Description: SubmitMsgServiceImpl 实现类
 * @Author: chen qi
 * @Date: 2019/12/22 9:58
 * @Version: 1.0
 **/
@Slf4j
@Service
public class SubmitMsgServiceImpl extends AbstractCurdService<SubmitMsg, String> implements SubmitMsgService {

    private final SubmitMsgRepository submitMsgRepository;

    public SubmitMsgServiceImpl(SubmitMsgRepository submitMsgRepository) {
        super(submitMsgRepository);
        this.submitMsgRepository = submitMsgRepository;
    }

    /**
     * 处理提交的信息，根据excelId存入数据库
     * @param fillerId      填表人id
     * @param jsonObject    提交的填写内容
     * @return 返回新增的SubmitMsg实体
     * @author zhou mingxin
     */
    public SubmitMsg submitMsg(String fillerId, JSONObject jsonObject) {
        try{
            String excelId = jsonObject.getString("excelId");
            jsonObject.remove("excelId");
            SubmitMsg submitMsg = new SubmitMsg();
            submitMsg.setUuid(UuidUtils.randomUUIDWithoutDash());
            submitMsg.setExcelId(excelId);
            submitMsg.setFillerId(fillerId);
            submitMsg.setContent(jsonObject.toJSONString());
            this.create(submitMsg);
            return submitMsg;
        }catch (Exception e){
            throw new BusinessException(e.getMessage());
        }

    }

    @Override
    public SubmitMsg updateSubmitMsg(String submitMsgId, JSONObject jsonObject) {
        try {
            jsonObject.remove("excelId");
            SubmitMsg submitMsg = submitMsgRepository.findByUuid(submitMsgId);
            submitMsg.setContent(jsonObject.toJSONString());
            this.update(submitMsg);
            return submitMsg;
        }catch(Exception e){
            throw new BusinessException(e.getMessage());
        }
    }

    /**
     * 根据模板id查询所有已提交的SubmitMsg
     *
     * @param excelId url中的模板id
     * @return 所有已提交的SubmitMsg
     */
    public List<SubmitMsg> getValidSubmitMsgByExcelIdSortByUpdateTimeDesc(String excelId) {
        Sort sortByUpdateTimeDesc = Sort.by(Sort.Direction.DESC, "updateTime");
        return submitMsgRepository.findByExcelIdAndDeleted(excelId, false, sortByUpdateTimeDesc);
    }

    /**根据填表人id查询未删除的提交历史数据
     * @param fillerId 填表人/提交人id
     * @return 某人提交的所有有效数据
     */
    @Override
    public List<SubmitMsg> getValidSubmitMsgByFillerIdSortByUpdateTimeDesc(String fillerId) {
        try{
            Sort sortByUpdateTimeDesc = Sort.by(Sort.Direction.DESC, "updateTime");
            List<SubmitMsg> submitMsgs = submitMsgRepository.findByFillerIdAndDeleted(fillerId, false, sortByUpdateTimeDesc);
            return submitMsgs;
        }catch (Exception e){
            log.error(e.getMessage());
            throw new BusinessException("查询数据库出错！");
        }
    }

    @Override
    public List<SubmitMsg> getAllValidSubmitMsgSortByUpdatetimeDesc() {
        try {
            Sort sortByUpdateTimeDesc = Sort.by(Sort.Direction.DESC, "updateTime");
            List<SubmitMsg> submitMsgs = submitMsgRepository.findAllByDeleted(false, sortByUpdateTimeDesc);
            return submitMsgs;
        }catch (Exception e){
            log.error(e.getMessage());
            throw new BusinessException("查询数据库出错！");
        }
    }

    @Override
    public int countMsgByExcelId(String excelId) {
        try {
            return submitMsgRepository.countAllByExcelIdAndDeleted(excelId, false);
        }catch (Exception e){
            throw new BusinessException("请求单表已提交的数据出错！");
        }
    }

    @Override
    public SubmitMsg removeById(String submitMsgId) {
        try {
            SubmitMsg submitMsg = submitMsgRepository.findByUuid(submitMsgId);
            submitMsg.setDeleted(true);
            return submitMsgRepository.save(submitMsg);
        }catch (Exception e){
            throw new BusinessException("删除出错");
        }
    }

    /**为限制每个用户在每个表只能提交一次数据，查询是否已提交过
     * @param userId    用户id
     * @param excelId   表格id
     */
    public boolean checkIsSubmitted(String userId, String excelId){
        List<SubmitMsg> submittedUsers = submitMsgRepository
                .findByFillerIdAndExcelIdAndDeleted(userId, excelId, false);
        return (submittedUsers.size() != 0);
    }

}
