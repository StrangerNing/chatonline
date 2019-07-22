package com.chatonline.dao.common;

import com.chatonline.model.po.Record;
import com.chatonline.model.po.RecordExample;

/**
 * RecordDao继承基类
 */
public interface RecordDao extends MyBatisBaseDao<Record, Long, RecordExample> {
}