package com.chatonline.controller;

import com.chatonline.base.component.Result;
import com.chatonline.model.form.LoginForm;
import com.chatonline.model.form.RegisterForm;
import com.chatonline.service.LoginService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @author zening.zhu
 * @version 1.0
 * @date 2019/7/19
 */

@Controller
public class LoginController {
    @Resource
    private LoginService loginService;

    @RequestMapping("/login")
    @ResponseBody
    public Result login(LoginForm loginForm) {
        return Result.success(loginService.login(loginForm));
    }

    @RequestMapping("/register")
    @ResponseBody
    public Result register(RegisterForm registerForm) {
        loginService.register(registerForm);
        return Result.success();
    }
}
