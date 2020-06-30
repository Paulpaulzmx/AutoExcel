package com.z5n.autoexcel.controller;

import com.z5n.autoexcel.config.security.IAuthenticationFacade;
import com.z5n.autoexcel.exception.BusinessException;
import com.z5n.autoexcel.model.ResultBody;
import com.z5n.autoexcel.model.entity.User;
import com.z5n.autoexcel.model.entity.UserInfo;
import com.z5n.autoexcel.model.vo.MailVo;
import com.z5n.autoexcel.model.vo.ResetPasswordVo;
import com.z5n.autoexcel.model.vo.UpdateUserInfoVo;
import com.z5n.autoexcel.model.vo.UserRegisterVo;
import com.z5n.autoexcel.service.MailService;
import com.z5n.autoexcel.service.UserInfoService;
import com.z5n.autoexcel.service.UserService;
import com.z5n.autoexcel.utils.VerifyCodeUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@Slf4j
@Api(tags = "用户操作的接口")
@RestController
public class UserController {

    private final UserService userService;
    private final MailService mailService;
    private final UserInfoService userInfoService;

    private final IAuthenticationFacade authenticationFacade;


    public UserController(UserService userService, MailService mailService, UserInfoService userInfoService, IAuthenticationFacade authenticationFacade) {
        this.userService = userService;
        this.mailService = mailService;
        this.userInfoService = userInfoService;
        this.authenticationFacade = authenticationFacade;
    }

    /**
     * 用户登陆接口
     *
     * @param username 用户名
     * @param password 密码
     */
    @ApiOperation("用户登陆接口")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResultBody login(String username, String password) {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return ResultBody.error("参数为空");
        }
        try {
            User user = userService.findUserByUsername(username);
            if (new BCryptPasswordEncoder().matches(password, user.getPassword())) {
                return ResultBody.success();
            } else {
                return ResultBody.error("用户名或密码错误");
            }
        } catch (BusinessException businessException) {
            return ResultBody.error(businessException.getMessage());
        }
    }

    /**
     * 返回用户当前还未修改的附加信息
     */
    @ApiOperation("返回用户当前还未修改的附加信息")
    @RequestMapping(value = "/user/getOldPersonInfo", method = RequestMethod.GET)
    public ResultBody getOldPersonInfo() {
        try {
            Authentication authentication = authenticationFacade.getAuthentication();
            User currentUser = userService.findUserByUsername(authentication.getName());
            UserInfo userInfo = userInfoService.getUserInfoByUserId(currentUser.getUuid());
            return ResultBody.success(userInfo);
        } catch (BusinessException e) {
            return ResultBody.error(e.getMessage());
        }
    }

    /**
     * 用户提交修改后的新附加信息
     */
    @ApiOperation("用户提交修改后的新附加信息")
    @RequestMapping(value = "/user/submitNewUserInfo", method = RequestMethod.POST)
    public ResultBody uploadNewUserInfo(UpdateUserInfoVo newUserInfo) {
        try {
            Authentication authentication = authenticationFacade.getAuthentication();
            User currentUser = userService.findUserByUsername(authentication.getName());
            userInfoService.update(updateUserInfo(newUserInfo, currentUser));
            return ResultBody.success();
        } catch (BusinessException e) {
            return ResultBody.error(e.getMessage());
        }
    }

    private UserInfo updateUserInfo(UpdateUserInfoVo newUserInfo, User currentUser) {
        UserInfo userInfo = userInfoService.getUserInfoByUserId(currentUser.getUuid());
        userInfo.setAddress(newUserInfo.getAddress());
        userInfo.setClassNum(newUserInfo.getClassNum());
        userInfo.setQq(newUserInfo.getQq());
        userInfo.setTel(newUserInfo.getTel());
        userInfo.setIdNum(newUserInfo.getIdCardNum());
        userInfo.setStuNum(newUserInfo.getStuNum());
        if (newUserInfo.getGender() != null && newUserInfo.getGender().equals("male")) {
            userInfo.setGender(1);
        } else {
            userInfo.setGender(0);
        }
        userInfo.setName(newUserInfo.getTrueName());
        return userInfo;
    }

    /**
     * 新用户注册接口
     *
     * @param userRegisterVo 新用户注册信息
     */
    @ApiOperation("新用户注册接口")
    @RequestMapping(value = "/register/newUser", method = RequestMethod.POST)
    public ResultBody userRegister(UserRegisterVo userRegisterVo) {

        //验证码check
        if (!mailService.verifying(userRegisterVo.getEmail(), userRegisterVo.getVerifyCode())) {
            return ResultBody.error("验证码错误，请重试！");
        }

        //必填项不能为空
        if (userInfoIsNull(userRegisterVo)) { return ResultBody.error("必填项不能为空!");}

        User user = generateUser(userRegisterVo);
        UserInfo userInfo = new UserInfo();

        //存储基本信息
        try {
            userService.addUser(user);
        } catch (BusinessException businessException) {
            return ResultBody.error(businessException.getMessage());
        } catch (IncorrectResultSizeDataAccessException e) {
            return ResultBody.error("邮箱或用户名数据不唯一");
        }
        //存储用户其他信息 userInfo
        if (userDetailsNotNull(userRegisterVo)) {
            generateUserDetail(userRegisterVo, userInfo);
        }
        userService.addUserInfo(userInfo, user.getUuid());
        return ResultBody.success();
    }

    private void generateUserDetail(UserRegisterVo userRegisterVo, UserInfo userInfo) {
        userInfo.setName(userRegisterVo.getTrueName());
        if (userRegisterVo.getGender() != null && userRegisterVo.getGender().equals("male")) {
            userInfo.setGender(1);
        } else {
            userInfo.setGender(0);
        }
        userInfo.setStuNum(userRegisterVo.getStuNum());
        userInfo.setIdNum(userRegisterVo.getIdCardNum());
        userInfo.setTel(userRegisterVo.getTel());
        userInfo.setQq(userRegisterVo.getQq());
        userInfo.setAddress(userRegisterVo.getAddress());
        userInfo.setClassNum(userRegisterVo.getClassNum());
    }

    private boolean userDetailsNotNull(UserRegisterVo userRegisterVo) {
        return StringUtils.isNotEmpty(userRegisterVo.getTrueName()) ||
                StringUtils.isNotEmpty(userRegisterVo.getGender()) ||
                StringUtils.isNotEmpty(userRegisterVo.getStuNum()) ||
                StringUtils.isNotEmpty(userRegisterVo.getIdCardNum()) ||
                StringUtils.isNotEmpty(userRegisterVo.getTel()) ||
                StringUtils.isNotEmpty(userRegisterVo.getQq()) ||
                StringUtils.isNotEmpty(userRegisterVo.getQq()) ||
                StringUtils.isNotEmpty(userRegisterVo.getClassNum()) ||
                StringUtils.isNotEmpty(userRegisterVo.getAddress());
    }

    private User generateUser(UserRegisterVo userRegisterVo) {
        User user = new User();
        user.setName(userRegisterVo.getUsername());
        user.setEmail(userRegisterVo.getEmail());
        user.setPassword(userRegisterVo.getPassword());
        return user;
    }

    private boolean userInfoIsNull(UserRegisterVo userRegisterVo) {
        return StringUtils.isEmpty(userRegisterVo.getEmail()) ||
                StringUtils.isEmpty(userRegisterVo.getPassword()) ||
                StringUtils.isEmpty(userRegisterVo.getPasswordVerify()) ||
                StringUtils.isEmpty(userRegisterVo.getUsername());
    }

    /**
     * 邮箱验证码发送
     *
     * @param emailAddress 邮箱地址
     */
    @ApiOperation("邮箱验证码发送")
    @RequestMapping(value = "/register/verifyCode", method = RequestMethod.POST)
    public ResultBody emailVerify(@RequestParam("emailAddress") String emailAddress) {
        //生成验证码
        try {
            sendVerifyCode(emailAddress);
            return ResultBody.success();
        } catch (BusinessException e) {
            return ResultBody.error(e.getMessage());
        }
    }

    /**
     * 发送验证码
     *
     * @param emailAddress
     */
    @ApiOperation("发送注册验证码")
    private void sendVerifyCode(@RequestParam("emailAddress") String emailAddress) {
        String verifyCode = VerifyCodeUtils.generateVerifyCode();
        MailVo mailVo = new MailVo();
        mailVo.setFrom("980455605@qq.com");
        mailVo.setTo(emailAddress);
        mailVo.setSubject("自动收集表验证码");
        mailVo.setSentDate(new Date());
        mailVo.setVerifyCode(verifyCode);
        mailVo.setText("验证码为：" + mailVo.getVerifyCode());
        mailService.sendMail(mailVo);
    }


    /**
     * 获取用户名
     *
     * @return 当前登陆用户的用户名
     */
    @ApiOperation("获取当前用户用户名，显示在dropDown")
    @RequestMapping(value = "/user/getName", method = RequestMethod.GET)
    public ResultBody showUsername() {
        Authentication authentication = authenticationFacade.getAuthentication();
        return ResultBody.success(authentication.getName());
    }

    /**
     * 忘记密码发送验证码
     *
     * @param username 需要重置密码的用户名
     */
    @ApiOperation("忘记密码请求发送验证码")
    @RequestMapping(value = "/forgetPassword/getVerifyCode", method = RequestMethod.POST)
    public ResultBody forgetPasswordVerifyCode(@RequestParam("username") String username) {
        try {
            User user = userService.findUserByUsername(username);
            if (user == null) {
                return ResultBody.error("用户：" + username + "不存在！");
            }
            sendVerifyCode(user.getEmail());
            return ResultBody.success();
        } catch (BusinessException e) {
            return ResultBody.error(e.getMessage());
        }
    }

    /**
     * 忘记密码提交新密码
     *
     * @param resetPasswordVo 新密码等信息
     */
    @ApiOperation("忘记密码，重置密码请求")
    @RequestMapping(value = "forgetPassword/resetPasswordSubmit", method = RequestMethod.POST)
    public ResultBody resetPassword(ResetPasswordVo resetPasswordVo) {
        if (
                resetPasswordVoIsNull(resetPasswordVo)
        ) {
            return ResultBody.error("参数错误");
        }
        if (!resetPasswordVo.getNewPasswordVerify().equals(resetPasswordVo.getNewPassword())) {
            return ResultBody.error("两次密码不同");
        }

        User user = userService.findUserByUsername(resetPasswordVo.getUsername());
        if (!mailService.verifying(user.getEmail(), resetPasswordVo.getVerifyCode())) {
            return ResultBody.error("验证码错误，请重试！");
        }
        try {
            userService.updatePassword(user, resetPasswordVo.getNewPassword());
            return ResultBody.success();
        } catch (BusinessException e) {
            return ResultBody.error(e.getMessage());
        }
    }

    private boolean resetPasswordVoIsNull(ResetPasswordVo resetPasswordVo) {
        return StringUtils.isEmpty(resetPasswordVo.getUsername()) ||
                StringUtils.isEmpty(resetPasswordVo.getVerifyCode()) ||
                StringUtils.isEmpty(resetPasswordVo.getNewPassword()) ||
                StringUtils.isEmpty(resetPasswordVo.getNewPasswordVerify());
    }

}
