package com.suo.projectManagement.dao.impl;

import com.suo.projectManagement.dao.IRoleInProjectRepo;
import com.suo.projectManagement.po.RoleInProject;
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
public class RoleInProjectRepoImpl implements IRoleInProjectRepo{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public int add(RoleInProject roleInProject){
        String sql = "insert into roleinproject(projectId, username, role) values(?, ?, ?)";
        return jdbcTemplate.update(sql, roleInProject.getProjectId(), roleInProject.getUsername(), roleInProject.getRole());
    }

    @Override
    public int delete(RoleInProject roleInProject){
        String sql = "delete from roleinproject where projectId=? and username=? and role=?";
        return jdbcTemplate.update(sql, roleInProject.getProjectId(), roleInProject.getUsername(), roleInProject.getRole());
    }

    @Override
    public List<String> getRolesByUsername(String username) {
        String sql = "select role from roleinproject where username=?";
        return jdbcTemplate.queryForList(sql, String.class, username);
    }

    @Override
    public List<Integer> getProjectIdListByUsername(String username){
        String sql = "select DISTINCT projectId from roleinproject where username = ?";
        return jdbcTemplate.queryForList(sql, Integer.class, username);
    }

    @Override
    public List<RoleInProject> getRoleInProjectListByProjectIdAndRole(int projectId, String role){
        String sql = "select * from roleinproject where projectId=? and role=?";
        RowMapper<RoleInProject> rm = BeanPropertyRowMapper.newInstance(RoleInProject.class);
        List<RoleInProject> roleInProjectList = jdbcTemplate.query(sql, rm, projectId, role);
        return roleInProjectList;
    }

    @Override
    public List<RoleInProject> getRoleInProjectListByProjectId(int projectId){
        String sql = "select * from roleinproject where projectId=? order by role";
        RowMapper<RoleInProject> rm = BeanPropertyRowMapper.newInstance(RoleInProject.class);
        List<RoleInProject> roleInProjectList = jdbcTemplate.query(sql, rm, projectId);
        return roleInProjectList;
    }

    @Override
    public List<String> getRolesInProject(int projectId, String username){
        String sql = "select DISTINCT role from roleinproject where projectId=? and username=?";
        return jdbcTemplate.queryForList(sql, String.class, projectId, username);
    }

    @Override
    public List<String> getUsernameList(int projectId, String role) {
        String sql = "select DISTINCT username from roleinproject where projectId=? and role=?";
        return jdbcTemplate.queryForList(sql, String.class, projectId, role);
    }

    @Override
    public List<String> getUsernameList(int projectId) {
        String sql = "select DISTINCT username from roleinproject where projectId=?";
        return jdbcTemplate.queryForList(sql, String.class, projectId);
    }
}
