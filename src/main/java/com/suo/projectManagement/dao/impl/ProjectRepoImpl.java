package com.suo.projectManagement.dao.impl;

import com.suo.projectManagement.dao.IProjectRepo;
import com.suo.projectManagement.po.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Suo Tian on 2018-05-04.
 */
@Repository
public class ProjectRepoImpl implements IProjectRepo{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int add(Project project){
        String sql = "insert into project(projectNumber, researchId, abbreviation, organizationId, status, moduleList, planNumber, batchNumber, ethicNumber, description, createTime) values(?,?,?,?,?,?,?,?,?,?,sysdate())";
        jdbcTemplate.update(sql, project.getProjectNumber(), project.getResearchId(), project.getAbbreviation(), project.getOrganizationId(), project.getStatus(), project.getModuleList(), project.getPlanNumber(), project.getBatchNumber(), project.getEthicNumber(), project.getDescription());
        sql = "select LAST_INSERT_ID()";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    public void update(Project project){
        String sql = "update project set projectNumber=?, researchId=?, abbreviation=?, organizationId=?, status=?, moduleList=?, planNumber=?, batchNumber=?, ethicNumber=?, description=? where id=?";
        jdbcTemplate.update(sql, project.getProjectNumber(), project.getResearchId(), project.getAbbreviation(), project.getOrganizationId(), project.getStatus(), project.getModuleList(), project.getPlanNumber(), project.getBatchNumber(), project.getEthicNumber(), project.getDescription(), project.getId());
    }

    public List<Project> getAllProjects(){
        String sql = "select * from project";
        RowMapper<Project> rm = BeanPropertyRowMapper.newInstance(Project.class);
        List<Project> projectList = jdbcTemplate.query(sql,rm);
        return projectList;
    }

    public Project getProjectById(int id) {
        String sql = "select * from project where id = ?";
        RowMapper<Project> rm = BeanPropertyRowMapper.newInstance(Project.class);
        List<Project> list = jdbcTemplate.query(sql, rm, id);
        return list.size()>0?list.get(0):null;
    }


//    private String getProjectId(String organization){
//        String sql="select _nextval('projectid')";
//        int sequence = jdbcTemplate.queryForObject(sql, int.class);
//        if(sequence < 10){
//            organization += "00";  //1写成001
//        }else if(sequence >= 10 && sequence <=99){
//            organization += "0";
//        }
//        return organization + sequence;
//    }

}
