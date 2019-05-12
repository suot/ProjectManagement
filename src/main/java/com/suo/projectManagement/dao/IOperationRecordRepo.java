package com.suo.projectManagement.dao;

import com.suo.projectManagement.po.OperationRecord;

import java.util.List;

/**
 * Created by TianSuo on 2018-07-04.
 */
public interface IOperationRecordRepo {
    void insertOperationRecord(OperationRecord operationRecord);
    List<OperationRecord> getOperationRecordsByProjectId(int projectId);
}
