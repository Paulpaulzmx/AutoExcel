package com.z5n.autoexcel.service.impl;

import com.z5n.autoexcel.exception.BusinessException;
import com.z5n.autoexcel.model.entity.UserInfo;
import com.z5n.autoexcel.repository.UserInfoRepository;
import com.z5n.autoexcel.service.UserInfoService;
import com.z5n.autoexcel.service.base.AbstractCurdService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class UserInfoServiceImpl extends AbstractCurdService<UserInfo, String> implements UserInfoService {

    private final UserInfoRepository userInfoRepository;

    public UserInfoServiceImpl(UserInfoRepository userInfoRepository) {
        super(userInfoRepository);
        this.userInfoRepository = userInfoRepository;
    }

    @Override
    public UserInfo getUserInfoByUserId(String userId) {
        try {
            UserInfo userInfo = userInfoRepository.findByUserId(userId);
            if(userInfo != null) {
                return userInfo;
            }else {
                throw new BusinessException("查找个人信息为空!");
            }
        }catch (Exception e){
            throw new BusinessException("查找个人信息错误!");
        }
    }
}
