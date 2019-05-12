package com.suo.projectManagement.dao;

import com.suo.projectManagement.po.Project;

import java.util.List;

/**
 * Created by TianSuo on 2018-07-03.
 */
public interface IProjectRepo {
    int add(Project project);
    void update(Project project);
    List<Project> getAllProjects();
    Project getProjectById(int id);
}
