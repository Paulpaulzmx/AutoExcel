package com.z5n.autoexcel.service;

import com.alibaba.fastjson.JSONObject;
import com.z5n.autoexcel.model.entity.StuMsg;
import com.z5n.autoexcel.service.base.CurdService;

import java.util.ArrayList;

/**
 * @program: autoexcel
 * @Interface: StuMsgService
 * @Description: 学生填写消息service
 * @Author: chen qi
 * @Date: 2019/12/22 9:57
 * @Version: 1.0
 **/
public interface StuMsgService extends CurdService<StuMsg, String> {
    public StuMsg submitMsg(JSONObject jsonObject);

    public ArrayList<StuMsg> getMsgByTemplateId(String id);
}
