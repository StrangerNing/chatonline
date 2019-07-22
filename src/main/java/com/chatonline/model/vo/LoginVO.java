package com.chatonline.model.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author zening.zhu
 * @version 1.0
 * @date 2019/7/19
 */

@Data
public class LoginVO {
    private Long id;

    private String username;

    private String nickname;

    private String avatar;

    private Integer sex;

    private Date birth;

    private String sign;

    private Date createTime;

    private Date lastLoginTime;

    private Integer status;
}
