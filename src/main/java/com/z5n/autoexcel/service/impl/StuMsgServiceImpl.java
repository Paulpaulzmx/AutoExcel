package com.z5n.autoexcel.service.impl;

import com.z5n.autoexcel.model.entity.StuMsg;
import com.z5n.autoexcel.repository.StuMsgRepository;
import com.z5n.autoexcel.service.StuMsgService;
import com.z5n.autoexcel.service.base.AbstractCurdService;
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
}
