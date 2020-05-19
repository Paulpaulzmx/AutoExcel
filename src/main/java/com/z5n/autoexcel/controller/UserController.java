package com.z5n.autoexcel.controller;

import com.z5n.autoexcel.config.security.IAuthenticationFacade;
import com.z5n.autoexcel.exception.BusinessException;
import com.z5n.autoexcel.model.ResultBody;
import com.z5n.autoexcel.model.entity.User;
import com.z5n.autoexcel.model.entity.UserInfo;
import com.z5n.autoexcel.model.vo.MailVo;
import com.z5n.autoexcel.model.vo.ResetPasswordVO;
import com.z5n.autoexcel.model.vo.UserRegisterVo;
import com.z5n.autoexcel.service.MailService;
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

    private final IAuthenticationFacade authenticationFacade;


    public UserController(UserService userService, MailService mailService, IAuthenticationFacade authenticationFacade) {
        this.userService = userService;
        this.mailService = mailService;
        this.authenticationFacade = authenticationFacade;
    }

    /**用户登陆接口
     * @param username 用户名
     * @param password 密码
     */
    @ApiOperation("用户登陆接口")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResultBody login(String username, String password){
        if(StringUtils.isEmpty(username) || StringUtils.isEmpty(password)){
            return ResultBody.error("参数为空");
        }
        try{
            User user = userService.findUserByUsername(username);
            if(new BCryptPasswordEncoder().matches(password, user.getPassword())){
                return ResultBody.success();
            }else{
                return ResultBody.error("用户名或密码错误");
            }
        }catch (BusinessException businessException){
            return ResultBody.error(businessException.getMessage());
        }
    }

    /**新用户注册接口
     * @param userRegisterVo 新用户注册信息
     */
    @ApiOperation("新用户注册接口")
    @RequestMapping(value = "/register/newUser", method = RequestMethod.POST)
    public ResultBody userRegister(UserRegisterVo userRegisterVo) {

        //验证码check
        if(!mailService.verifying(userRegisterVo.getEmail(), userRegisterVo.getVerifyCode())){
            return ResultBody.error("验证码错误，请重试！");
        }

        if (StringUtils.isEmpty(userRegisterVo.getEmail()) ||
                StringUtils.isEmpty(userRegisterVo.getPassword()) ||
                StringUtils.isEmpty(userRegisterVo.getPasswordVerify()) ||
                StringUtils.isEmpty(userRegisterVo.getUsername())
        ){
            return ResultBody.error("必填项不能为空!");
        }
        //存储基本信息
        User user = new User();
        user.setName(userRegisterVo.getUsername());
        user.setEmail(userRegisterVo.getEmail());
        user.setPassword(userRegisterVo.getPassword());
        try{
            userService.addUser(user);
        }catch (BusinessException businessException){
            return ResultBody.error(businessException.getMessage());
        }catch (IncorrectResultSizeDataAccessException e){
            return ResultBody.error("邮箱或用户名数据不唯一");
        }
        //存储用户其他信息 userInfo
        if(
                StringUtils.isNotEmpty(userRegisterVo.getTrueName()) ||
                StringUtils.isNotEmpty(userRegisterVo.getGender()) ||
                StringUtils.isNotEmpty(userRegisterVo.getStuNum()) ||
                StringUtils.isNotEmpty(userRegisterVo.getIdCardNum()) ||
                StringUtils.isNotEmpty(userRegisterVo.getTel()) ||
                StringUtils.isNotEmpty(userRegisterVo.getQq()) ||
                StringUtils.isNotEmpty(userRegisterVo.getAddress())
        ){
            UserInfo userInfo = new UserInfo();
            userInfo.setName(userRegisterVo.getTrueName());
            if(userRegisterVo.getGender() != null && userRegisterVo.getGender().equals("male")){
                userInfo.setGender(1);
            }else{
                userInfo.setGender(0);
            }
            userInfo.setStuNum(userRegisterVo.getStuNum());
            userInfo.setIdNum(userRegisterVo.getIdCardNum());
            userInfo.setTel(userRegisterVo.getTel());
            userInfo.setQq(userRegisterVo.getQq());
            userInfo.setAddress(userRegisterVo.getAddress());
            userService.addUserInfo(userInfo, user.getUuid());
        }else {
            userService.addUserInfo(new UserInfo(), user.getUuid());
        }
        return ResultBody.success();
    }

    /**邮箱验证码发送
     * @param emailAddress 邮箱地址
     */
    @ApiOperation("邮箱验证码发送")
    @RequestMapping(value = "/register/verifyCode", method = RequestMethod.POST)
    public ResultBody emailVerify(@RequestParam("emailAddress") String emailAddress) {
        //生成验证码
        try {
            sendVerifyCode(emailAddress);
            return ResultBody.success();
        }catch (BusinessException e){
            return ResultBody.error(e.getMessage());
        }
    }

    /**获取用户名
     * @return 当前登陆用户的用户名
     */
    @RequestMapping(value = "/user/getName", method = RequestMethod.GET)
    public ResultBody showUsername(){
        Authentication authentication = authenticationFacade.getAuthentication();
        return ResultBody.success(authentication.getName());
    }

    /**忘记密码发送验证码
     * @param username 需要重置密码的用户名
     */
    @RequestMapping(value = "/forgetPassword/getVerifyCode", method = RequestMethod.POST)
    public ResultBody forgetPasswordVerifyCode(@RequestParam("username") String username){
        try{
            User user = userService.findUserByUsername(username);
            if(user == null){
                return ResultBody.error("用户："+username+"不存在！");
            }
            sendVerifyCode(user.getEmail());
            return ResultBody.success();
        }catch (BusinessException e){
            return ResultBody.error(e.getMessage());
        }
    }

    @RequestMapping(value = "forgetPassword/resetPasswordSubmit", method = RequestMethod.POST)
    public ResultBody resetPassword(ResetPasswordVO resetPasswordVO){
        if(
                StringUtils.isEmpty(resetPasswordVO.getUsername()) ||
                StringUtils.isEmpty(resetPasswordVO.getVerifyCode()) ||
                StringUtils.isEmpty(resetPasswordVO.getNewPassword()) ||
                StringUtils.isEmpty(resetPasswordVO.getNewPasswordVerify())
        ){
            return ResultBody.error("参数错误");
        }
        if(!resetPasswordVO.getNewPasswordVerify().equals(resetPasswordVO.getNewPassword())){
            return ResultBody.error("两次密码不同");
        }

        User user = userService.findUserByUsername(resetPasswordVO.getUsername());
        if(!mailService.verifying(user.getEmail(), resetPasswordVO.getVerifyCode())){
            return ResultBody.error("验证码错误，请重试！");
        }
        try{
            userService.updatePassword(user, resetPasswordVO.getNewPassword());
            return ResultBody.success();
        }catch(BusinessException e){
            return ResultBody.error(e.getMessage());
        }
    }

    /**发送验证码
     * @param emailAddress
     */
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
}
