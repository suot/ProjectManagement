package com.suo.projectManagement.dao;

import com.suo.projectManagement.po.RoleInProject;

import java.util.List;

/**
 * Created by TianSuo on 2018-07-04.
 */
public interface IRoleInProjectRepo {
    int add(RoleInProject roleInProject);
    int delete(RoleInProject roleInProject);
    List<String> getRolesByUsername(String username);
    List<Integer> getProjectIdListByUsername(String username);
    List<RoleInProject> getRoleInProjectListByProjectIdAndRole(int projectId, String role);
    List<RoleInProject> getRoleInProjectListByProjectId(int projectId);
    List<String> getRolesInProject(int projectId, String username);
    List<String> getUsernameList(int projectId, String role);
    List<String> getUsernameList(int projectId);
}
