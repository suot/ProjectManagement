package com.suo.projectManagement.dao;

import com.suo.projectManagement.po.User;

import java.util.List;
import java.util.Map;

/**
 * Created by TianSuo on 2018-07-03.
 */
public interface IUserRepo {
    User getUserByUsername(String username);
    List<User> getUserByOrganizationId(int organizationId);
    List<User> getAllUsers();
    List<List<User>> getUserByOrganizationGroup();
    List<List<User>> getUserByOrganizationGroup(int projectId);
    int getIdByUsername(String username);
    Map<String, User> getUserMapByUsername();
    void add(User user);
    void update(User user);
    void updateByAdmin(User user);
    void updatePassword(User user);
}
