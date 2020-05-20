package com.z5n.autoexcel.repository;

import com.z5n.autoexcel.model.entity.UserInfo;

public interface UserInfoRepository extends BaseRepository<UserInfo, String> {
    UserInfo findByUserId(String userId);
}
