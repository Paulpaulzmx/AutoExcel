package com.z5n.autoexcel.service.impl;

import com.z5n.autoexcel.exception.BusinessException;
import com.z5n.autoexcel.model.entity.Role;
import com.z5n.autoexcel.model.entity.User;
import com.z5n.autoexcel.model.entity.UserInfo;
import com.z5n.autoexcel.repository.BaseRepository;
import com.z5n.autoexcel.repository.RoleRepository;
import com.z5n.autoexcel.repository.UserInfoRepository;
import com.z5n.autoexcel.repository.UserRepository;
import com.z5n.autoexcel.service.UserService;
import com.z5n.autoexcel.service.base.AbstractCurdService;
import com.z5n.autoexcel.utils.UuidUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
public class UserServiceImpl extends AbstractCurdService<User, String> implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;

    public UserServiceImpl(BaseRepository<User, String> repository, UserRepository userRepository, RoleRepository roleRepository, UserInfoRepository userInfoRepository) {
        super(repository);
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userInfoRepository = userInfoRepository;
    }

    public UserServiceImpl() {
        super(new BaseRepository<User, String>() {
            @Override
            public long deleteByUuidIn(Iterable<String> strings) {
                return 0;
            }

            @Override
            public List<User> findAll() {
                return null;
            }

            @Override
            public List<User> findAll(Sort sort) {
                return null;
            }

            @Override
            public List<User> findAllById(Iterable<String> strings) {
                return null;
            }

            @Override
            public <S extends User> List<S> saveAll(Iterable<S> entities) {
                return null;
            }

            @Override
            public void flush() {

            }

            @Override
            public <S extends User> S saveAndFlush(S entity) {
                return null;
            }

            @Override
            public void deleteInBatch(Iterable<User> entities) {

            }

            @Override
            public void deleteAllInBatch() {

            }

            @Override
            public User getOne(String s) {
                return null;
            }

            @Override
            public <S extends User> List<S> findAll(Example<S> example) {
                return null;
            }

            @Override
            public <S extends User> List<S> findAll(Example<S> example, Sort sort) {
                return null;
            }

            @Override
            public Page<User> findAll(Pageable pageable) {
                return null;
            }

            @Override
            public <S extends User> S save(S entity) {
                return null;
            }

            @Override
            public Optional<User> findById(String s) {
                return Optional.empty();
            }

            @Override
            public boolean existsById(String s) {
                return false;
            }

            @Override
            public long count() {
                return 0;
            }

            @Override
            public void deleteById(String s) {

            }

            @Override
            public void delete(User entity) {

            }

            @Override
            public void deleteAll(Iterable<? extends User> entities) {

            }

            @Override
            public void deleteAll() {

            }

            @Override
            public <S extends User> Optional<S> findOne(Example<S> example) {
                return Optional.empty();
            }

            @Override
            public <S extends User> Page<S> findAll(Example<S> example, Pageable pageable) {
                return null;
            }

            @Override
            public <S extends User> long count(Example<S> example) {
                return 0;
            }

            @Override
            public <S extends User> boolean exists(Example<S> example) {
                return false;
            }
        });
    }

    /**
     * 登陆验证
     */
    @Override
    public User findUserByUsername(String username) {
        User user;
        user = userRepository.findByName(username);
        if (user == null) {
            throw new BusinessException("用户名或密码错误");
//            throw new BusinessException("用户不存在!");
        } else {
            return user;
        }
    }

    /**
     * 注册新用户,存储用户的基本信息
     */
    @Override
    public User addUser(User user) {
        if (checkUserNameExist(user)) {
            throw new BusinessException("用户名已被使用");
        }
        if (checkUserEmailExist(user)) {
            throw new BusinessException("邮箱已被使用");
        }

        user.setUuid(UuidUtils.randomUUIDWithoutDash());
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        //添加角色信息
        List<Role> roles = new ArrayList<>();
        Role role = roleRepository.findByName("ROLE_USER");
        roles.add(role);
        user.setRoles(roles);
        //存入数据库
        User savedUser = userRepository.save(user);
        return savedUser;
    }

    /**
     * 注册新用户，存储附加信息（UserInfo）
     */
    @Override
    public UserInfo addUserInfo(UserInfo userInfo, String userId) {
        userInfo.setUuid(UuidUtils.randomUUIDWithoutDash());
        userInfo.setUserId(userId);
        UserInfo saveUserInfo = userInfoRepository.save(userInfo);
        return saveUserInfo;
    }


    /**
     * 根据用户名检查用户是否已存在
     */
    private boolean checkUserNameExist(User user) {
        return userRepository.findByName(user.getName()) != null;
    }

    /**
     * 根据邮箱检查用户是否已存在
     */
    private boolean checkUserEmailExist(User user) {
        return userRepository.findByEmail(user.getEmail()) != null;
    }


    /**
     * 重写UserDetailsService的登陆验证方法
     *
     * @param username 用户在网页填写的用户名
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        try {
            User user = findUserByUsername(username);
            if (user == null) {
                return null;
            }
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();

            //获取用户所有角色
            List<Role> roles = user.getRoles();
            for (Role role : roles) {
                authorities.add(new SimpleGrantedAuthority(role.getName()));
            }

            log.info("当前用户："+username+"，当前用户拥有的权限"+authorities.toString());


            UserDetails userDetails = new org.springframework.security.core.userdetails.User
                    (username, user.getPassword(), authorities);
            return userDetails;

        }catch (BusinessException businessException){
            log.error(businessException.getErrorMsg());
            return null;
        }
        catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public User updatePassword(User user, String newPassword) {
        User newUser = user;
        newUser.setPassword(new BCryptPasswordEncoder().encode(newPassword));
        try{
            return userRepository.save(newUser);
        }catch (Exception e){
            throw new BusinessException("存储新用户时出错");
        }
    }
}
