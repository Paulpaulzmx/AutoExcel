package com.z5n.autoexcel.service.impl;

import com.z5n.autoexcel.exception.BusinessException;
import com.z5n.autoexcel.model.entity.Role;
import com.z5n.autoexcel.model.entity.User;
import com.z5n.autoexcel.repository.RoleRepository;
import com.z5n.autoexcel.repository.UserRepository;
import com.z5n.autoexcel.service.RoleService;
import com.z5n.autoexcel.service.UserService;
import com.z5n.autoexcel.service.base.AbstractCurdService;
import com.z5n.autoexcel.utils.UuidUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: autoexcel
 * @ClassName: ExcelServiceImpl
 * @Description: ExcelServiceImpl
 * @Author: chen qi
 * @Date: 2019/12/22 19:42
 * @Version: 1.0
 **/
@Slf4j
@Service
public class RoleServiceImpl extends AbstractCurdService<Role, String> implements RoleService {
    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository){
        super(roleRepository);
        this.roleRepository = roleRepository;
    }


}
