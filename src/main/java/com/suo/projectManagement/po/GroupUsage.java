package com.suo.projectManagement.po;

/**
 * Created by wuxu on 2018/5/30.
 */
public class GroupUsage {
    private int id;
    private  int projectId;
    private String stratificationFactor; //分层信息
    private int groupNumber; //组号

    public GroupUsage() {
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

    public String getStratificationFactor() {
        return stratificationFactor;
    }

    public void setStratificationFactor(String stratificationFactor) {
        this.stratificationFactor = stratificationFactor;
    }

    public int getGroupNumber() {
        return groupNumber;
    }

    public void setGroupNumber(int groupNumber) {
        this.groupNumber = groupNumber;
    }

    @Override
    public String toString() {
        return "GroupUsage{" +
                "id=" + id +
                ", projectId=" + projectId +
                ", stratificationFactor='" + stratificationFactor + '\'' +
                ", groupNumber=" + groupNumber +
                '}';
    }
}
