package com.suo.projectManagement.dao.impl;

import com.suo.projectManagement.config.SecurityConfig;
import com.suo.projectManagement.dao.IInvestiOrganizationRepo;
import com.suo.projectManagement.dao.IUserRepo;
import com.suo.projectManagement.po.User;
import com.suo.projectManagement.service.impl.CurrentProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Suo Tian on 2018-04-26.
 */
@Repository
public class UserRepoImpl implements IUserRepo{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired private OrganizationRepoImpl organizationRepoImpl;

    @Autowired private CurrentProjectService currentProjectService;

    @Autowired
    private IInvestiOrganizationRepo investiOrganizationRepo;


    public User getUserByUsername(String username) {
        String sql = "select * from user where username = ?";
        RowMapper<User> rm = BeanPropertyRowMapper.newInstance(User.class);
        List<User> userList = jdbcTemplate.query(sql, rm, username);
        if(userList.size()>0){
            return userList.get(0);
        }else {
            return null;
        }
    }

    public List<User> getUserByOrganizationId(int organizationId) {
        String sql = "select * from user where organizationId = ?";
        RowMapper<User> rm = BeanPropertyRowMapper.newInstance(User.class);
        List<User> userList = jdbcTemplate.query(sql, rm, organizationId);
        return userList;
    }

    public List<User> getAllUsers() {
        String sql = "select * from user";
        RowMapper<User> rm = BeanPropertyRowMapper.newInstance(User.class);
        List<User> userList = jdbcTemplate.query(sql,rm);
        return userList;
    }

    public List<List<User>> getUserByOrganizationGroup(){
        List<List<User>> groupedUsers = new ArrayList<>();

        String sql = "select distinct organizationId from user";
        List<Integer> organizationIdList = jdbcTemplate.queryForList(sql, Integer.class);

        if(organizationIdList != null) {
            for (int organizationId : organizationIdList) {
                List<User> usersWithSameOrganizationId = this.getUserByOrganizationId(organizationId);
//                for(User user : usersWithSameOrganizationId){
//                    String organizationName = organizationRepoImpl.getOrganizationById(user.getOrganizationId()).getName();
//                    user.setOrganizationId(organizationName);
//                }
                groupedUsers.add(usersWithSameOrganizationId);
            }
        }
        return groupedUsers;
    }

    /**
     * 取projectId里的所有参试机构里的users
     * @param projectId
     * @return
     */
    @Override
    public List<List<User>> getUserByOrganizationGroup(int projectId){
        //参试机构id
        List<Integer> researchOrganizationIdList = investiOrganizationRepo.getOrgIds(projectId);

        List<List<User>> groupedUsers = new ArrayList<>();
        if(researchOrganizationIdList != null && researchOrganizationIdList.size()>0) {
            for (int organizationId : researchOrganizationIdList) {
                List<User> usersWithSameOrganizationId = this.getUserByOrganizationId(organizationId);
//                for(User user : usersWithSameOrganizationId){
//                    String organizationName = organizationRepoImpl.getOrganizationById(user.getOrganizationId()).getName();
//                    user.setOrganizationId(organizationName);
//                }
                groupedUsers.add(usersWithSameOrganizationId);
            }

        }
        return groupedUsers;
    }


    public int getIdByUsername(String username){
        String sql = "select id from user where username = ?";
        return jdbcTemplate.queryForObject(sql, int.class, username);
    }

    /**
     * 将userinfo根据username组织成map，便于在前端Thymeleaf中引用
     * @return    map, key: username, value: userInfo
     */
    public Map<String, User> getUserMapByUsername(){
        List<User> userList = this.getAllUsers();
        if(userList.size()>0) {
            Map<String, User> userMap = new HashMap<>();
            for (User user : userList) {
                userMap.put(user.getUsername(), user);
            }
            return userMap;
        }else{
            return null;
        }
    }

    public void add(User user){
        String sql = "insert into user(username, password, name, phone, cellphone, email, organizationId, role, valid, activated, createTime) values(?,?,?,?,?,?,?,?,?,?,sysdate())";
        jdbcTemplate.update(sql, user.getUsername(), SecurityConfig.encodePassword(user.getPassword()), user.getName(), user.getPhone(), user.getCellphone(), user.getEmail(), user.getOrganizationId(), user.getRole(), user.getValid(), user.getActivated());
    }

    public void update(User user){
        String sql = "update user set name=?, phone=?, cellphone=?, email=? where username=?";
        jdbcTemplate.update(sql, user.getName(), user.getPhone(), user.getCellphone(), user.getEmail(), user.getUsername());
    }

    public void updateByAdmin(User user){
        String sql = "update user set name=?, phone=?, cellphone=?, email=?, organizationId=?, role=?, valid=?, activated=? where username=?";
        jdbcTemplate.update(sql, user.getName(), user.getPhone(), user.getCellphone(), user.getEmail(), user.getOrganizationId(), user.getRole(), user.getValid(), user.getActivated(), user.getUsername());
    }

    public void updatePassword(User user){
        String sql = "update user set password=? where username=?";
        jdbcTemplate.update(sql, SecurityConfig.encodePassword(user.getPassword()), user.getUsername());
    }

}
