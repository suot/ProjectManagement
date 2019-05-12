package com.suo.projectManagement.po;

import java.util.Date;

/**
 * Created by Suo Tian on 2018-05-22.
 */

public class OperationRecord {
    private int id;
    private int projectId;
    private String username;
    private String name;
    private String role;
    private String module;
    private String operation;
    private Date createTime;

    public OperationRecord(){

    }

    public OperationRecord(int projectId, String username, String name, String role, String module, String operation) {
        this.projectId = projectId;
        this.username = username;
        this.name = name;
        this.role = role;
        this.module = module;
        this.operation = operation;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "OperationRecord{" +
                "id=" + id +
                ", projectId='" + projectId + '\'' +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", role='" + role + '\'' +
                ", module='" + module + '\'' +
                ", operation='" + operation + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
