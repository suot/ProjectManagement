package com.suo.projectManagement.dao.impl;

import com.suo.projectManagement.dao.IInvestiGroupRepo;
import com.suo.projectManagement.po.InvestiGroup;
import com.suo.projectManagement.vo.InvestiGroupInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by wuxu on 2018/5/30.
 */
@Repository
public class InvestiGroupRepoImpl implements IInvestiGroupRepo {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public int add(InvestiGroupInfo invesiGroupInfo) {
        String sql = "insert into investigroup(projectid, grouptype, groupname, occupy, subjectnumber) values(?,?,?,?,?)";
        return jdbcTemplate.update(sql, invesiGroupInfo.getProjectId(), invesiGroupInfo.getGroupType(), invesiGroupInfo.getGroupName(), invesiGroupInfo.getOccupy(), invesiGroupInfo.getSubjectNumber());
    }

    @Override
    public List<InvestiGroup> get(int projectId) {
        String sql = "select * from investigroup where projectid = ?";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(InvestiGroup.class), projectId);
    }

    @Override
    public InvestiGroup get(int projectId, String groupType){
        String sql = "select * from investigroup where projectid = ? and grouptype=?";
        List<InvestiGroup> list = jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(InvestiGroup.class), projectId, groupType);
        return list.size()>0?list.get(0):null;
    }

    @Override
    public String getGroupName(int projectId, String typeName) {
        String sql = "select groupname from investigroup where projectid = ? and grouptype=?";
        return jdbcTemplate.queryForObject(sql, String.class, projectId, typeName);
    }
}
