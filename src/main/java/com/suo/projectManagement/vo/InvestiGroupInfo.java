package com.suo.projectManagement.vo;

/**
 * Created by wuxu on 2018/5/30.
 * 随机试验组信息
 */
public class InvestiGroupInfo implements Comparable<InvestiGroupInfo>{
    private int  projectId;
    private String groupName;  //试验组名
    private  int    subjectNumber; //受试者数
    private String groupType;   //试验组类型
    private int occupy;         //试验组比例
    private int processd_occupy; //调整后的比例

    public InvestiGroupInfo(){}

    public int getProjectId() { return projectId; }

    public void setProjectId(int projectId) { this.projectId = projectId; }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getSubjectNumber() {
        return subjectNumber;
    }

    public void setSubjectNumber(int subjectNumber) {
        this.subjectNumber = subjectNumber;
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

    public int getProcessd_occupy() {
        return processd_occupy;
    }

    public void setProcessd_occupy(int processd_occupy) {
        this.processd_occupy = processd_occupy;
    }

    @Override
    public String toString() {
        return "InvestiGroupInfo{" +
                "groupName='" + groupName + '\'' +
                ", subjectNumber=" + subjectNumber +
                ", groupType='" + groupType + '\'' +
                ", occupy=" + occupy +
                ", processd_occupy=" + processd_occupy +
                '}';
    }

    @Override
    public int compareTo(InvestiGroupInfo o) {
        int oc = o.getOccupy();
        return this.occupy-oc;
    }
}
