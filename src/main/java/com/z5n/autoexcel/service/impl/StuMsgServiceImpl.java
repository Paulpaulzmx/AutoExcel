package com.z5n.autoexcel.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.z5n.autoexcel.model.entity.StuMsg;
import com.z5n.autoexcel.repository.StuMsgRepository;
import com.z5n.autoexcel.service.StuMsgService;
import com.z5n.autoexcel.service.base.AbstractCurdService;
import com.z5n.autoexcel.utils.UuidUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * @program: autoexcel
 * @ClassName: StuMsgServiceImpl
 * @Description: StuMsgServiceImpl 实现类
 * @Author: chen qi
 * @Date: 2019/12/22 9:58
 * @Version: 1.0
 **/
@Service
public class StuMsgServiceImpl extends AbstractCurdService<StuMsg, String> implements StuMsgService {

    private final StuMsgRepository stuMsgRepository;

    public StuMsgServiceImpl(StuMsgRepository stuMsgRepository) {
        super(stuMsgRepository);
        this.stuMsgRepository = stuMsgRepository;
    }

    /**
     * 处理提交的信息，根据templateId存入数据库
     * @param jsonObject
     * @author zhou mingxin
     * @return 返回新增的StuMsg实体
     */
    public StuMsg submitMsg(JSONObject jsonObject){
        String templateId = jsonObject.getString("templateId");
        jsonObject.remove("templateId");
        StuMsg stuMsg = new StuMsg();
        stuMsg.setTemplateId(templateId);
        stuMsg.setContent(jsonObject.toJSONString());
        stuMsg.setUuid(UuidUtils.randomUUIDWithoutDash());
        this.create(stuMsg);
        return stuMsg;
    }

    /**
     * 根据模板id查询所有已提交的StuMsg
     * @param id url中的模板id
     * @return 所有已提交的StuMsg
     */
    public ArrayList<StuMsg> getMsgByTemplateId(String id){
        return (ArrayList<StuMsg>) stuMsgRepository.findByTemplateId(id);
    }
}
