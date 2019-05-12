package com.suo.projectManagement.dao.impl;

import com.suo.projectManagement.dao.IOperationRecordRepo;
import com.suo.projectManagement.po.OperationRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Suo Tian on 2018-05-22.
 */
@Repository
public class OperationRecordRepoImpl implements IOperationRecordRepo{
    @Autowired private JdbcTemplate jdbcTemplate;

    public void insertOperationRecord(OperationRecord operationRecord){
        String sql = "insert into operationrecord(projectId, username, name, role, module, operation) values(?,?,?,?,?,?)";
        jdbcTemplate.update(sql, operationRecord.getProjectId(), operationRecord.getUsername(), operationRecord.getName(), operationRecord.getRole(), operationRecord.getModule(), operationRecord.getOperation());
    }

    public List<OperationRecord> getOperationRecordsByProjectId(int projectId){
        String sql = "select * from operationrecord where projectId = ? order by createtime desc limit 20";
        RowMapper<OperationRecord> rm = BeanPropertyRowMapper.newInstance(OperationRecord.class);
        List<OperationRecord> operationRecordList = jdbcTemplate.query(sql, rm, projectId);
        return operationRecordList;
    }

}
