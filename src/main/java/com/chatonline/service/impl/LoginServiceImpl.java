package com.chatonline.service.impl;

import com.chatonline.base.exception.BusinessException;
import com.chatonline.dao.common.UserDao;
import com.chatonline.model.form.LoginForm;
import com.chatonline.model.form.RegisterForm;
import com.chatonline.model.po.User;
import com.chatonline.model.po.UserExample;
import com.chatonline.model.vo.LoginVO;
import com.chatonline.service.LoginService;
import com.chatonline.utils.MD5Util;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zening.zhu
 * @version 1.0
 * @date 2019/7/19
 */

@Service
public class LoginServiceImpl implements LoginService {

    @Resource
    private UserDao userDao;

    @Override
    public LoginVO login(LoginForm loginForm) {
        String username = loginForm.getUsername();
        UserExample userExample = new UserExample();
        userExample.createCriteria().andUsernameEqualTo(username);
        List<User> userList = userDao.selectByExample(userExample);
        if (!CollectionUtils.isEmpty(userList)) {
            User user = userList.get(0);
            String password = user.getPassword();
            if (MD5Util.verify(loginForm.getPassword(),password)) {
                LoginVO login = new LoginVO();
                BeanUtils.copyProperties(user,login);
                return login;
            } else {
                throw new BusinessException("用户名或密码错误");
            }
        }
        throw new BusinessException("没有此用户");
    }

    @Override
    public void register(RegisterForm registerForm) {
        String username = registerForm.getUsername();
        String password = registerForm.getPassword();
        UserExample userExample = new UserExample();
        userExample.createCriteria().andUsernameEqualTo(username);
        List<User> userList = userDao.selectByExample(userExample);
        if (CollectionUtils.isEmpty(userList)) {
            if (password.equals(registerForm.getConfirmPassword())) {
                User user = new User();
                user.setUsername(username);
                user.setPassword(MD5Util.generate(password));
                userDao.insert(user);
            } else {
                throw new BusinessException("两次密码不一致");
            }
        } else {
            throw new BusinessException("该用户名已存在");
        }
    }


}
