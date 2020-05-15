package com.z5n.autoexcel.service;

import com.z5n.autoexcel.model.entity.User;
import com.z5n.autoexcel.model.entity.UserInfo;
import com.z5n.autoexcel.service.base.CurdService;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @program: autoexcel
 * @Interface: StuInfoService
 * @Description: 学生信息Service
 * @Author: chen qi
 * @Date: 2019/12/22 9:37
 * @Version: 1.0
 **/
public interface UserService extends CurdService<User, String>, UserDetailsService {
    User addUser(User user);
    UserInfo addUserInfo(UserInfo userInfo, String userId);
    User findUserByUsername(String username);
}
