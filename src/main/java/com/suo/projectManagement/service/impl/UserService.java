package com.suo.projectManagement.service.impl;

import com.suo.projectManagement.aop.PrintServiceName;
import com.suo.projectManagement.dao.IRoleInProjectRepo;
import com.suo.projectManagement.dao.IUserRepo;
import com.suo.projectManagement.po.Organization;
import com.suo.projectManagement.po.User;
import com.suo.projectManagement.vo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by tiansuo on 2018/5/4.
 */

@Service
public class UserService {
    @Autowired
    IUserRepo userRepoImpl;

    @Autowired
    IRoleInProjectRepo roleInProjectRepo;

    @Autowired
    OrganizationService organizationService;

    public static final String USER_ADMIN = "admin";
    public static final String PASSWORD_ADMIN = "admin";

    //@PrintServiceName(description = "人员管理&&查询单行")
    public User getUserByUsername(String username){
        return userRepoImpl.getUserByUsername(username);
    }

    //@PrintServiceName(description = "人员管理&&查询所有行")
    public List<User> getAllUsers(){
        return userRepoImpl.getAllUsers();
    }

    /**
     * @return all userInfo including organization.name
     */
    public List<UserInfo> getAllUserInfo(){
        List<User> userList = this.getAllUsers();
        if(userList.size()>0){
            List<UserInfo> userInfoList = new ArrayList<>();
            Organization organization;
            UserInfo userInfo;
            for (User user : userList){
                organization = organizationService.getOrganizationById(user.getOrganizationId());
                userInfo = new UserInfo(user, organization);
                userInfoList.add(userInfo);
            }
            return userInfoList;
        }else {
            return null;
        }
    }


    /**
     * @return all userInfo in one project including organization.name
     */
    public List<UserInfo> getAllUserInfoByProjectId(int projectId){
        List<UserInfo> userInfoList;
        User user;
        Organization organization;
        UserInfo userInfo;

        List<String> usernameListInProject = roleInProjectRepo.getUsernameList(projectId);
        if(usernameListInProject != null && usernameListInProject.size()>0){
            userInfoList = new ArrayList<>();
            for(String username : usernameListInProject){
                user = userRepoImpl.getUserByUsername(username);
                organization = organizationService.getOrganizationById(user.getOrganizationId());
                userInfo = new UserInfo(user, organization);
                userInfoList.add(userInfo);
            }
            return userInfoList;
        }
        else {
            return null;
        }
    }


    /**
     * get userInfo including organization.name
     * @param user
     * @return UserInfo
     */
    public UserInfo getUserInfoFromUser(User user){
        Organization organization = organizationService.getOrganizationById(user.getOrganizationId());
        return new UserInfo(user, organization);
    }


    //@PrintServiceName(description = "人员管理&&根据机构名称查询多行")
    public List<List<User>> getUserByOrganizationGroup(){
        return userRepoImpl.getUserByOrganizationGroup();
    }
    public List<List<User>> getUserByOrganizationGroup(int projectId){
        return userRepoImpl.getUserByOrganizationGroup(projectId);
    }

    @PrintServiceName(description = "人员管理&&添加了人员信息")
    public void createUser(User user){
        userRepoImpl.add(user);
    }

    @PrintServiceName(description = "人员管理&&修改了个人信息")
    public void modifyUser(User user){
        userRepoImpl.update(user);
    }

    @PrintServiceName(description = "人员管理&&修改了人员信息")
    public void modifyUserByAdmin(User user){
        userRepoImpl.updateByAdmin(user);
    }

    @PrintServiceName(description = "人员管理&&修改了个人密码")
    public void modifyPassword(User user){
        userRepoImpl.updatePassword(user);
    }

    public Map<String, User> getUserMapByUsername(){
        return userRepoImpl.getUserMapByUsername();
    }

    public List<String> getRoleEnumValues(){
        List<String> roleEnum = new ArrayList<>();
        roleEnum.add("项目经理");
        roleEnum.add("医生");
        roleEnum.add("主要研究员");
        roleEnum.add("独立统计师");
        roleEnum.add("统计员");
        roleEnum.add("临床协调员");
        return roleEnum;
    }

    public List<String> getValidEnumValues(){
        List<String> validEnum = new ArrayList<>();
        validEnum.add("有效");
        validEnum.add("无效");
        return validEnum;
    }

    public List<String> getActivatedEnumValues(){
        List<String> activatedEnum = new ArrayList<>();
        activatedEnum.add("已激活");
        activatedEnum.add("未激活");
        return activatedEnum;
    }
}
