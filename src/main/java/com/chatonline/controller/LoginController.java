package com.chatonline.controller;

import com.chatonline.base.component.Result;
import com.chatonline.base.constant.LocalConstant;
import com.chatonline.model.form.LoginForm;
import com.chatonline.model.form.RegisterForm;
import com.chatonline.model.vo.LoginVO;
import com.chatonline.service.LoginService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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
    public ModelAndView login(HttpServletRequest request, LoginForm loginForm) {
        LoginVO login = loginService.login(loginForm);
        request.getSession().setAttribute(LocalConstant.WEBSOCKET_USERNAME_KEY,login.getUsername());
        ModelAndView modelAndView = new ModelAndView("chat.html");
        modelAndView.addObject("user",login);
        return modelAndView;
    }

    @RequestMapping("/register")
    @ResponseBody
    public Result register(RegisterForm registerForm) {
        loginService.register(registerForm);
        return Result.success();
    }
}
