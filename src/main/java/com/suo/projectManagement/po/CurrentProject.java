package com.suo.projectManagement.po;

/**
 * Created by TianSuo on 2018-05-28.
 */
public class CurrentProject {
    private int id;
    private String username;
    private int projectId;

    public CurrentProject(){

    }
    public CurrentProject(String username, int projectId) {
        this.username = username;
        this.projectId = projectId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "CurrentProject{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", projectId='" + projectId + '\'' +
                '}';
    }
}
