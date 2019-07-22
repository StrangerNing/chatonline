package com.chatonline.service;

import com.chatonline.model.form.LoginForm;
import com.chatonline.model.form.RegisterForm;
import com.chatonline.model.vo.LoginVO;

/**
 * @author zening.zhu
 * @version 1.0
 * @date 2019/7/19
 */

public interface LoginService {

    /**
     * 登录
     * @param loginForm 用户名和密码
     * @return 用户信息
     */
    LoginVO login(LoginForm loginForm);

    /**
     * 注册
     * @param registerForm 注册用户名、密码以及确认密码
     */
    void register(RegisterForm registerForm);
}
