package com.suo.projectManagement.po;

/**
 * Created by wuxu on 2018/5/30.
 */
public class InvestiGroup {
    private int id;
    private int projectId;
    private String groupName;  //试验组名
    private String groupType;   //试验组类型
    private int occupy;         //试验组比例
    private  int    subjectNumber; //受试者数

    public InvestiGroup() {
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

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public int getOccupy() {
        return occupy;
    }

    public void setOccupy(int occupy) {
        this.occupy = occupy;
    }

    public int getSubjectNumber() {
        return subjectNumber;
    }

    public void setSubjectNumber(int subjectNumber) {
        this.subjectNumber = subjectNumber;
    }

    @Override
    public String toString() {
        return "InvestiGroup{" +
                "id=" + id +
                ", projectId=" + projectId +
                ", groupName='" + groupName + '\'' +
                ", groupType='" + groupType + '\'' +
                ", occupy=" + occupy +
                ", subjectNumber=" + subjectNumber +
                '}';
    }
}
