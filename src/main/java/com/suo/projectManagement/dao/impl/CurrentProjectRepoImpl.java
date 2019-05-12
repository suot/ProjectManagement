package com.suo.projectManagement.dao.impl;

import com.suo.projectManagement.dao.ICurrentProjectRepo;
import com.suo.projectManagement.po.CurrentProject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Suo Tian on 2018-05-28.
 */

@Repository
public class CurrentProjectRepoImpl implements ICurrentProjectRepo {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 从首页选择一个项目进入后，更新数据库中的currentproject表，下次再登录时取这个项目中的权限作为登录时的权限。
     * @param currentProject
     */
    @Override
    public void updateCurrentProject(CurrentProject currentProject){
        String sql = "select count(*) from currentproject where username=?";
        int count = jdbcTemplate.queryForObject(sql, int.class, currentProject.getUsername());
        if(count==1){
            //有记录，更新
            sql = "update currentproject set projectId=? where username=?";
            jdbcTemplate.update(sql, currentProject.getProjectId(), currentProject.getUsername());
        }else if(count==0){
            //没有记录，插入
            sql="insert into currentproject(username, projectId) values(?,?)";
            jdbcTemplate.update(sql, currentProject.getUsername(), currentProject.getProjectId());
        }else{
            //多余一条，是错误的记录。先删除再插入
            sql="delete from currentproject where username=?";
            jdbcTemplate.update(sql, currentProject.getUsername());
            sql="insert into currentproject(username, projectId) values(?,?)";
            jdbcTemplate.update(sql, currentProject.getUsername(), currentProject.getProjectId());
        }
    }

    @Override
    public CurrentProject getCurrentProjectByUsername(String username){
        String sql="select * from currentproject where username=?";
        RowMapper<CurrentProject> rm = BeanPropertyRowMapper.newInstance(CurrentProject.class);
        List<CurrentProject> list = jdbcTemplate.query(sql, rm, username);
        return list.size()>0?list.get(0):null;
    }

    @Override
    public int add(CurrentProject currentProject){
        String sql="insert into currentproject(username, projectId) values(?,?)";
        return jdbcTemplate.update(sql, currentProject.getUsername(), currentProject.getProjectId());
    }

    @Override
    public List<String> getUsernameListByProjectId(int projectId){
        String sql = "select username from currentproject where projectId=?";
        return jdbcTemplate.queryForList(sql, String.class, projectId);
    }

    @Override
    public List<String> getAllUsernames(){
        String sql = "select username from currentproject";
        return jdbcTemplate.queryForList(sql, String.class);
    }


    @Override
    public int delete(CurrentProject currentProject){
        String sql = "delete from currentproject where username=? and projectId=?";
        return jdbcTemplate.update(sql, currentProject.getUsername(), currentProject.getProjectId());
    }
}
