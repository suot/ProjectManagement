package com.suo.projectManagement.vo;

/**
 * Created by wuxu on 2018/5/30.
 * 随机数信息
 */
public class RandomCell implements  Comparable<RandomCell>{
    private int randomNumber;
    private int projectId;
    private double randomResult;  //随机数结果
    private int groupNumber;    //随机组号
    private int orderInGroup;   //组内序号
    private String groupType;   //分配类型
    private String stratificationFactor = "default";  //分层标记

    public  RandomCell(){}

    public RandomCell(int randomNumber, int projectId, double randomResult, int groupNumber, int orderInGroup, String groupType, String stratificationFactor) {
        this.randomNumber = randomNumber;
        this.projectId = projectId;
        this.randomResult = randomResult;
        this.groupNumber = groupNumber;
        this.orderInGroup = orderInGroup;
        this.groupType = groupType;
        this.stratificationFactor = stratificationFactor;
    }

    public int getRandomNumber() {
        return randomNumber;
    }

    public void setRandomNumber(int randomNumber) {
        this.randomNumber = randomNumber;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public double getRandomResult() {
        return randomResult;
    }

    public void setRandomResult(double randomResult) {
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

    @Override
    public String toString() {
        return "RandomCell{" +
                "randomNumber=" + randomNumber +
                ", projectId=" + projectId +
                ", randomResult=" + randomResult +
                ", groupNumber=" + groupNumber +
                ", orderInGroup=" + orderInGroup +
                ", groupType='" + groupType + '\'' +
                ", stratificationFactor='" + stratificationFactor + '\'' +
                '}';
    }

    @Override
    public int compareTo(RandomCell o) {
        double compareNumber = o.getRandomResult();
        if (this.getRandomResult()>compareNumber)
            return  1;
        else return -1;
    }
}
