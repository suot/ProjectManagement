package com.suo.projectManagement.po;

/**
 * Created by Suo Tian on 2018-04-26.
 */
public class RoleInProject {
    private int id;
    private int projectId;
    private String username;
    private String role;

    public RoleInProject(){}

    public RoleInProject(int projectId, String username, String role) {
        this.projectId = projectId;
        this.username = username;
        this.role = role;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "RoleInProject{" +
                "id=" + id +
                ", projectId=" + projectId +
                ", username='" + username + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
