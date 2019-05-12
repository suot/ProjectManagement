package com.suo.projectManagement.vo;

import java.util.Date;

/**
 * Created by wuxu on 2018/6/1.
 */
public class UnfoldBlindnessResultDownload {
    private int id;
    private String subjectId;
    private String investigatorId;
    private String groupType; //分组类型
    private String groupName; //分组类型名称
    private int randomBlockNum; //区块号
    private int orderInBlock;
    private String rnid;
    private Date offerDate;

    public UnfoldBlindnessResultDownload(int id, String subjectId, String investigatorId, String groupType, String groupName, int randomBlockNum, int orderInBlock, String rnid, Date offerDate) {
        this.id = id;
        this.subjectId = subjectId;
        this.investigatorId = investigatorId;
        this.groupType = groupType;
        this.groupName = groupName;
        this.randomBlockNum = randomBlockNum;
        this.orderInBlock = orderInBlock;
        this.rnid = rnid;
        this.offerDate = offerDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getInvestigatorId() {
        return investigatorId;
    }

    public void setInvestigatorId(String investigatorId) {
        this.investigatorId = investigatorId;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getRandomBlockNum() {
        return randomBlockNum;
    }

    public void setRandomBlockNum(int randomBlockNum) {
        this.randomBlockNum = randomBlockNum;
    }

    public int getOrderInBlock() {
        return orderInBlock;
    }

    public void setOrderInBlock(int orderInBlock) {
        this.orderInBlock = orderInBlock;
    }

    public String getRnid() {
        return rnid;
    }

    public void setRnid(String rnid) {
        this.rnid = rnid;
    }

    public Date getOfferDate() {
        return offerDate;
    }

    public void setOfferDate(Date offerDate) {
        this.offerDate = offerDate;
    }

    @Override
    public String toString() {
        return "UnfoldBlindnessResultDownload{" +
                "id=" + id +
                ", subjectId='" + subjectId + '\'' +
                ", investigatorId='" + investigatorId + '\'' +
                ", groupType='" + groupType + '\'' +
                ", groupName='" + groupName + '\'' +
                ", randomBlockNum=" + randomBlockNum +
                ", orderInBlock=" + orderInBlock +
                ", rnid=" + rnid +
                ", offerDate=" + offerDate +
                '}';
    }
}
