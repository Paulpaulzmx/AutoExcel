package com.z5n.autoexcel.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.z5n.autoexcel.model.entity.StuMsg;
import com.z5n.autoexcel.repository.StuMsgRepository;
import com.z5n.autoexcel.service.StuMsgService;
import com.z5n.autoexcel.service.base.AbstractCurdService;
import io.swagger.models.auth.In;
import org.springframework.stereotype.Service;

/**
 * @program: autoexcel
 * @ClassName: StuMsgServiceImpl
 * @Description: StuMsgServiceImpl 实现类
 * @Author: chen qi
 * @Date: 2019/12/22 9:58
 * @Version: 1.0
 **/
@Service
public class StuMsgServiceImpl extends AbstractCurdService<StuMsg, Integer> implements StuMsgService {

    public StuMsgServiceImpl(StuMsgRepository stuMsgRepository) {
        super(stuMsgRepository);
    }

    /**
     * 处理提交的信息，根据templateId存入数据库
     * @param jsonObject
     * @author zhou mingxin
     * @return 返回新增的StuMsg实体
     */
    public StuMsg submitMsg(JSONObject jsonObject){
        Integer templateId = jsonObject.getInteger("templateId");
        jsonObject.remove("templateId");
        StuMsg stuMsg = new StuMsg();
        stuMsg.setTemplateId(templateId);
        stuMsg.setContent(jsonObject.toJSONString());
        this.create(stuMsg);
        return stuMsg;
    }
}
