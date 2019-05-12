package com.suo.projectManagement.dao.impl;

import com.suo.projectManagement.dao.IInvestiOrganizationRepo;
import com.suo.projectManagement.po.InvestiOrganization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by wuxu on 2018/5/30.
 */
@Repository
public class InvestiOrganizationRepoImpl implements IInvestiOrganizationRepo {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public int add(InvestiOrganization investiOrganization) {
        String sql = "insert into investiorganization(projectId, orgId) values(?,?)";
        return jdbcTemplate.update(sql, investiOrganization.getProjectId(), investiOrganization.getOrgId());
    }

    @Override
    public int updateEnrollNumber(InvestiOrganization investiOrganization) {
        String sql = "update investiorganization set enrollnumber=? where projectId=? and orgId=?";
        return jdbcTemplate.update(sql, investiOrganization.getEnrollNumber(), investiOrganization.getProjectId(), investiOrganization.getOrgId());
    }

    @Override
    public int updateStatus(InvestiOrganization investiOrganization) {
        String sql = "update investiorganization set valid=? where projectId=? and orgId=?";
        return jdbcTemplate.update(sql, investiOrganization.getValid(), investiOrganization.getProjectId(), investiOrganization.getOrgId());
    }

    @Override
    public List<InvestiOrganization> get(int projectId) {
        String sql = "select * from investiorganization where projectId=?";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(InvestiOrganization.class), projectId);
    }

    @Override
    public List<Integer> getOrgIds(int projectId) {
        String sql = "select orgId from investiorganization where projectId=?";
        return jdbcTemplate.queryForList(sql, Integer.class, projectId);
    }

    @Override
    public int getEnrollNumber(int projectId, int orgId) {
        String sql = "select enrollNumber from investiorganization where projectId=? and orgId=?";
        return jdbcTemplate.queryForObject(sql, Integer.class, projectId, orgId);
    }

    @Override
    public String getStatus(int projectId, int orgId) {
        String sql = "select valid from investiorganization where projectId=? and orgId=?";
        return jdbcTemplate.queryForObject(sql, String.class, projectId, orgId);
    }

    @Override
    public int delete(InvestiOrganization investiOrganization){
        String sql = "delete from investiorganization where projectId=? and orgId=?";
        return jdbcTemplate.update(sql, investiOrganization.getProjectId(), investiOrganization.getOrgId());
    }

}
