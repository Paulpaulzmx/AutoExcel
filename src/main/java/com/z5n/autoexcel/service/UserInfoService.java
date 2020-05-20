package com.z5n.autoexcel.service;

import com.z5n.autoexcel.model.entity.UserInfo;
import com.z5n.autoexcel.service.base.CurdService;

public interface UserInfoService extends CurdService<UserInfo, String> {
    UserInfo getUserInfoByUserId(String userId);
}
