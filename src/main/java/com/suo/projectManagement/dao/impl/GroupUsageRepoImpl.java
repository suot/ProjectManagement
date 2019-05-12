package com.suo.projectManagement.dao.impl;

import com.suo.projectManagement.dao.IGroupUsageRepo;
import com.suo.projectManagement.po.GroupUsage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by wuxu on 2018/5/30.
 */
@Repository
public class GroupUsageRepoImpl implements IGroupUsageRepo {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public int add(GroupUsage groupUsage) {
        String sql = "insert into groupusage(projectid, stratificationfactor, groupnumber) values(?,?,?)";
        return jdbcTemplate.update(sql, groupUsage.getProjectId(), groupUsage.getStratificationFactor(), groupUsage.getGroupNumber());
    }

    @Override
    public int update(GroupUsage groupUsage) {
        String sql = "update groupusage set projectid=?, stratificationfactor=?, groupnumber=? where id=?";
        return jdbcTemplate.update(sql, groupUsage.getProjectId(), groupUsage.getStratificationFactor(), groupUsage.getGroupNumber(), groupUsage.getId());
    }

    @Override
    public GroupUsage find(int projectID, String stratificationFactor) {
        String sql = "select * from groupusage where projectid=? and stratificationfactor=?";
        List<GroupUsage> list = jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(GroupUsage.class), projectID, stratificationFactor);
        return list.size()>0?list.get(0):null;
    }
}
