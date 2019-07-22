package com.chatonline.dao.common;

import com.chatonline.model.po.Log;
import com.chatonline.model.po.LogExample;

/**
 * LogDao继承基类
 */
public interface LogDao extends MyBatisBaseDao<Log, Long, LogExample> {
}