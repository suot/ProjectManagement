package com.suo.projectManagement.po;

/**
 * Created by wuxu on 2018/5/30.
 */
public class RandomSchedule {

    private int id ;
    private int projectId;
    private int randomNumber;
    private String randomResult;
    private int groupNumber;
    private int orderInGroup;
    private String groupType;
    private String stratificationFactor;
    private String isUsed;

    public RandomSchedule() {
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

    public int getRandomNumber() {
        return randomNumber;
    }

    public void setRandomNumber(int randomNumber) {
        this.randomNumber = randomNumber;
    }

    public String getRandomResult() {
        return randomResult;
    }

    public void setRandomResult(String randomResult) {
        this.randomResult = randomResult;
    }

    public int getGroupNumber() {
        return groupNumber;
    }

    public void setGroupNumber(int groupNumber) {
        this.groupNumber = groupNumber;
    }

    public int getOrderInGroup() {
        return orderInGroup;
    }

    public void setOrderInGroup(int orderInGroup) {
        this.orderInGroup = orderInGroup;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public String getStratificationFactor() {
        return stratificationFactor;
    }

    public void setStratificationFactor(String stratificationFactor) {
        this.stratificationFactor = stratificationFactor;
    }

    public String getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(String isUsed) {
        this.isUsed = isUsed;
    }

    @Override
    public String toString() {
        return "RandomSchedule{" +
                "id=" + id +
                ", projectId=" + projectId +
                ", randomNumber=" + randomNumber +
                ", randomResult='" + randomResult + '\'' +
                ", groupNumber=" + groupNumber +
                ", orderInGroup=" + orderInGroup +
                ", groupType='" + groupType + '\'' +
                ", stratificationFactor='" + stratificationFactor + '\'' +
                ", isUsed='" + isUsed + '\'' +
                '}';
    }
}
