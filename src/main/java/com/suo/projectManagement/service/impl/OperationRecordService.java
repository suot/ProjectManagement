package com.suo.projectManagement.service.impl;

import com.suo.projectManagement.dao.IOperationRecordRepo;
import com.suo.projectManagement.po.OperationRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Suo Tian on 2018-05-22.
 */

@Service
public class OperationRecordService {
    @Autowired
    IOperationRecordRepo operationRecordRepoImpl;

    public void insertOperationRecord(OperationRecord operationRecord){
        operationRecordRepoImpl.insertOperationRecord(operationRecord);
    }

    public List<OperationRecord> getOperationRecordsByProjectId(int projectId){
        return operationRecordRepoImpl.getOperationRecordsByProjectId(projectId);
    }
}

