package com.suo.projectManagement.po;

import java.util.Date;

/**
 * Created by wuxu on 2018/5/30.
 * 随机数设置
 */

public class RandomSettings {
    private int id;
    private int projectId;
    private String blindType;  //盲态类型
    private int simpleSize;     //样本量
    private String isCtrlSubEnroll;  //是否控制项目受试者
    private String randomSeed;  //随机种子
    private int numOfBlock;     //随机区组数
    private String balanceType; //平衡类型
    private String isStratifi; //是否分层
    private int lengthOfBlock;  //区组长度
    private int numOfGroup;     //试验分组数
    private String unFoldMgr; // 揭盲管理员
    private Date createDate;


    public RandomSettings(){}

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

    public String getBalanceType() {
        return balanceType;
    }

    public void setBalanceType(String balanceType) {
        this.balanceType = balanceType;
    }

    public String getBlindType() {
        return blindType;
    }

    public void setBlindType(String blindType) {
        this.blindType = blindType;
    }

    public int getSimpleSize() {
        return simpleSize;
    }

    public void setSimpleSize(int simpleSize) {
        this.simpleSize = simpleSize;
    }

    public String getIsCtrlSubEnroll() {
        return isCtrlSubEnroll;
    }

    public void setIsCtrlSubEnroll(String isCtrlSubEnroll) {
        this.isCtrlSubEnroll = isCtrlSubEnroll;
    }

    public String getRandomSeed() {
        return randomSeed;
    }

    public void setRandomSeed(String randomSeed) {
        this.randomSeed = randomSeed;
    }

    public int getNumOfBlock() {
        return numOfBlock;
    }

    public void setNumOfBlock(int numOfBlock) {
        this.numOfBlock = numOfBlock;
    }

    public int getLengthOfBlock() {
        return lengthOfBlock;
    }

    public void setLengthOfBlock(int lengthOfBlock) {
        this.lengthOfBlock = lengthOfBlock;
    }

    public int getNumOfGroup() {
        return numOfGroup;
    }

    public void setNumOfGroup(int numOfGroup) {
        this.numOfGroup = numOfGroup;
    }

    public String getIsStratifi() {
        return isStratifi;
    }

    public void setIsStratifi(String isStratifi) {
        this.isStratifi = isStratifi;
    }

    public String getUnFoldMgr() {
        return unFoldMgr;
    }

    public void setUnFoldMgr(String unFoldMgr) {
        this.unFoldMgr = unFoldMgr;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "RandomSettings{" +
                "id=" + id +
                ", projectId=" + projectId +
                ", blindType='" + blindType + '\'' +
                ", simpleSize=" + simpleSize +
                ", isCtrlSubEnroll='" + isCtrlSubEnroll + '\'' +
                ", randomSeed='" + randomSeed + '\'' +
                ", numOfBlock=" + numOfBlock +
                ", balanceType='" + balanceType + '\'' +
                ", isStratifi='" + isStratifi + '\'' +
                ", lengthOfBlock=" + lengthOfBlock +
                ", numOfGroup=" + numOfGroup +
                ", unFoldMgr='" + unFoldMgr + '\'' +
                ", createDate=" + createDate +
                '}';
    }
}
