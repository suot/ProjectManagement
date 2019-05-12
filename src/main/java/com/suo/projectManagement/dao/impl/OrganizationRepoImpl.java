package com.suo.projectManagement.dao.impl;

import com.suo.projectManagement.dao.IOrganizationRepo;
import com.suo.projectManagement.po.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Suo Tian on 2018-05-16.
 */
@Repository
public class OrganizationRepoImpl implements IOrganizationRepo{
    @Autowired private JdbcTemplate jdbcTemplate;

    public int insertOrganization(Organization organization){
        String sql = "insert into organization(organizationNumber, name, address, contactName, contactPhone, contactDepartment, property) values(?,?,?,?,?,?,?)";
        jdbcTemplate.update(sql, organization.getOrganizationNumber(), organization.getName(), organization.getAddress(), organization.getContactName(), organization.getContactPhone(), organization.getContactDepartment(), organization.getProperty());
        sql = "select LAST_INSERT_ID()";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    public void updateOrganization(Organization organization){
        String sql = "update organization set organizationNumber=?, name=?, address=?, contactName=?, contactPhone=?, contactDepartment=?, property=? where id=?";
        jdbcTemplate.update(sql, organization.getOrganizationNumber(), organization.getName(), organization.getAddress(), organization.getContactName(), organization.getContactPhone(), organization.getContactDepartment(), organization.getProperty(), organization.getId());
    }

    public List<Organization> getAllOrganizations(){
        String sql = "select * from organization";
        RowMapper<Organization> rm = BeanPropertyRowMapper.newInstance(Organization.class);
        List<Organization> organizationList = jdbcTemplate.query(sql,rm);
        return organizationList;
    }

    public Organization getOrganizationById(int id) {
        String sql = "select * from organization where id = ?";
        RowMapper<Organization> rm = BeanPropertyRowMapper.newInstance(Organization.class);
        List<Organization> list = jdbcTemplate.query(sql,rm, id);
        return list.size()>0?list.get(0):null;
    }
}
