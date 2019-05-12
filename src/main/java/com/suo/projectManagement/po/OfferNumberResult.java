package com.suo.projectManagement.po;

import java.util.Date;

/**
 * Created by wuxu on 2018/5/30.
 */
public class OfferNumberResult {
    private int id ;
    private int projectId;
    private String rnid;
    private int randomNumber;
    private String randomResult;
    private int groupNumber;
    private int orderInGroup;
    private String groupType;
    private String stratificationFactor;
    private String investigatorId;
    private String subjectId;
    private int organizationId;
    private Date offerDate;

    public OfferNumberResult() {
    }

    public OfferNumberResult(int projectId, String rnid, int randomNumber, String randomResult, int groupNumber, int orderInGroup, String groupType, String stratificationFactor, String investigatorId, String subjectId, int organizationId) {
        this.projectId = projectId;
        this.rnid = rnid;
        this.randomNumber = randomNumber;
        this.randomResult = randomResult;
        this.groupNumber = groupNumber;
        this.orderInGroup = orderInGroup;
        this.groupType = groupType;
        this.stratificationFactor = stratificationFactor;
        this.investigatorId = investigatorId;
        this.subjectId = subjectId;
        this.organizationId = organizationId;
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

    public String getRnid() {
        return rnid;
    }

    public void setRnid(String rnid) {
        this.rnid = rnid;
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

    public String getInvestigatorId() {
        return investigatorId;
    }

    public void setInvestigatorId(String investigatorId) {
        this.investigatorId = investigatorId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public Date getOfferDate() {
        return offerDate;
    }

    public void setOfferDate(Date offerDate) {
        this.offerDate = offerDate;
    }

    public int getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(int organizationId) {
        this.organizationId = organizationId;
    }

    @Override
    public String toString() {
        return "OfferNumberResult{" +
                "id=" + id +
                ", projectId=" + projectId +
                ", rnid='" + rnid + '\'' +
                ", randomNumber=" + randomNumber +
                ", randomResult='" + randomResult + '\'' +
                ", groupNumber=" + groupNumber +
                ", orderInGroup=" + orderInGroup +
                ", groupType='" + groupType + '\'' +
                ", stratificationFactor='" + stratificationFactor + '\'' +
                ", investigatorId='" + investigatorId + '\'' +
                ", subjectId='" + subjectId + '\'' +
                ", organizationId=" + organizationId +
                ", offerDate=" + offerDate +
                '}';
    }
}
