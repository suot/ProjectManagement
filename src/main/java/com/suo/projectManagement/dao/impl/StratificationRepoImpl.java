package com.suo.projectManagement.dao.impl;

import com.suo.projectManagement.dao.IStratificationRepo;
import com.suo.projectManagement.po.Stratification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Suo Tian on 2018/5/30.
 */
@Repository
public class StratificationRepoImpl implements IStratificationRepo {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public int add(Stratification stratification) {
        String sql = "insert into stratification(projectid, stratifiid, stratifiname, stratififactorid, stratififactorname) values(?,?,?,?,?)";
        return jdbcTemplate.update(sql, stratification.getProjectId(), stratification.getStratifiId(), stratification.getStratifiName(), stratification.getStratifiFactorId(), stratification.getStratifiFactorName());
    }

    @Override
    public List<Stratification> getAll(int projectID) {
        String sql = "select * from stratification where projectid=? order by stratififactorid";
        RowMapper<Stratification> rm = BeanPropertyRowMapper.newInstance(Stratification.class);
        List<Stratification> stratificationList = jdbcTemplate.query(sql, rm, projectID);
        return stratificationList;
    }

    @Override
    public int delete(int projectID) {
        String sql = "delete from stratification where projectid = ?";
        return jdbcTemplate.update(sql, projectID);
    }
}
