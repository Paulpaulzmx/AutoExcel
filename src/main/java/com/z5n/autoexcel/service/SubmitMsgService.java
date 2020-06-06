package com.z5n.autoexcel.service;

import com.alibaba.fastjson.JSONObject;
import com.z5n.autoexcel.model.entity.SubmitMsg;
import com.z5n.autoexcel.service.base.CurdService;

import java.util.List;

/**
 * @program: autoexcel
 * @Interface: SubmitMsgService
 * @Description: 学生填写消息service
 * @Author: chen qi
 * @Date: 2019/12/22 9:57
 * @Version: 1.0
 **/
public interface SubmitMsgService extends CurdService<SubmitMsg, String> {
    public SubmitMsg submitMsg(String fillerId, JSONObject jsonObject);

    public SubmitMsg updateSubmitMsg(String submitMsgId, JSONObject jsonObject);

    public List<SubmitMsg> getValidSubmitMsgByExcelIdSortByUpdateTimeDesc(String excelId);

    public List<SubmitMsg> getValidSubmitMsgByFillerIdSortByUpdateTimeDesc(String userID);

    public List<SubmitMsg> getAllValidSubmitMsgSortByUpdatetimeDesc();

    public int countMsgByExcelId(String excelId);

    public boolean checkIsSubmitted(String userId, String excelId);

}
