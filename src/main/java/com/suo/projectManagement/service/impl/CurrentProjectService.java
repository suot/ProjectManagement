package com.suo.projectManagement.service.impl;

import com.suo.projectManagement.aop.PrintServiceName;
import com.suo.projectManagement.dao.ICurrentProjectRepo;
import com.suo.projectManagement.po.CurrentProject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Suo Tian on 2018-05-28.
 */
@Service
public class CurrentProjectService {
    @Autowired
    ICurrentProjectRepo currentProjectRepoImpl;

    @PrintServiceName(description = "当前项目&&切换了项目")
    public void updateCurrentProject(CurrentProject currentProject){
        currentProjectRepoImpl.updateCurrentProject(currentProject);
    }

    //@PrintServiceName(description = "当前项目&&查询单行")
    public CurrentProject getCurrentProjectByUsername(String username){
        return currentProjectRepoImpl.getCurrentProjectByUsername(username);
    }

    public void addCurrentProject(CurrentProject currentProject){
        currentProjectRepoImpl.add(currentProject);
    }

}
