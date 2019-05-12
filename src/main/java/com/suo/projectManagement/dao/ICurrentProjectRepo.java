package com.suo.projectManagement.dao;

import com.suo.projectManagement.po.CurrentProject;

import java.util.List;

/**
 * Created by TianSuo on 2018-07-04.
 */
public interface ICurrentProjectRepo {
    void updateCurrentProject(CurrentProject currentProject);
    CurrentProject getCurrentProjectByUsername(String username);
    int add(CurrentProject currentProject);
    List<String> getUsernameListByProjectId(int projectId);
    int delete(CurrentProject currentProject);
    List<String> getAllUsernames();
}
