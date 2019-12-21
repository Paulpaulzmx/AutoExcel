package com.z5n.autoexcel.service;

import com.z5n.autoexcel.bean.StuMsg;
import com.z5n.autoexcel.repository.StuMsgRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.Table;
import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class StuMsgService {
    @Resource
    private StuMsgRepository stuMsgRepository;

    /**
     * 增
     * @param stuMsg
     * @return
     */
    @Transactional
    public StuMsg save(StuMsg stuMsg){
        return stuMsgRepository.save(stuMsg);
    }

    /**
     * 根据id删除
     * @param id
     */
    @Transactional
    public void delete(int id){
        stuMsgRepository.deleteById(id);
    }

    /**
     * 查询所有
     * @return 所有结果的迭代器
     */
    public Iterable<StuMsg> getAll(){
        return stuMsgRepository.findAll();
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    public StuMsg getById(int id){
        Optional<StuMsg> op = stuMsgRepository.findById(id);
//        return op.orElseGet(op::get);
        return op.get();
    }

}
