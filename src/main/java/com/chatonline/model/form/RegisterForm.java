package com.chatonline.model.form;

import lombok.Data;

/**
 * @author zening.zhu
 * @version 1.0
 * @date 2019/7/22
 */

@Data
public class RegisterForm {
    private String username;

    private String password;

    private String confirmPassword;
}
