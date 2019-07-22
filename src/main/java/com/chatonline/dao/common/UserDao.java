package com.chatonline.dao.common;

import com.chatonline.model.po.User;
import com.chatonline.model.po.UserExample;

/**
 * UserDao继承基类
 */
public interface UserDao extends MyBatisBaseDao<User, Long, UserExample> {
}