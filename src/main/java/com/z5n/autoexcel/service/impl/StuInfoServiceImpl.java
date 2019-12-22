package com.z5n.autoexcel.service.impl;

import com.z5n.autoexcel.model.entity.StuInfo;
import com.z5n.autoexcel.repository.StuInfoRepository;
import com.z5n.autoexcel.service.StuInfoService;
import com.z5n.autoexcel.service.base.AbstractCurdService;
import org.springframework.stereotype.Service;

/**
 * @program: autoexcel
 * @ClassName: StuInfoServiceImpl
 * @Description: StuInfoServiceImpl实现类
 * @Author: chen qi
 * @Date: 2019/12/22 10:40
 * @Version: 1.0
 **/
@Service
public class StuInfoServiceImpl extends AbstractCurdService<StuInfo, Integer> implements StuInfoService {

    public StuInfoServiceImpl(StuInfoRepository stuInfoRepository) {
        super(stuInfoRepository);
    }

}
