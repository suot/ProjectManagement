package com.suo.projectManagement.po;

/**
 * Created by wuxu on 2018/5/30.
 */
public class OfferNumberStatus {
    private int id;
    private int projectId;
    private String stratificationFactor;
    private int groupNumber;
    private int randomNumber;
    private int orderInGroup;
    private int currentNumber;
    private String investigatorId;
    private int randomNumberId;
    private int organizationId;

    public OfferNumberStatus() {
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

    public int getRandomNumber() {
        return randomNumber;
    }

    public void setRandomNumber(int randomNumber) {
        this.randomNumber = randomNumber;
    }

    public int getOrderInGroup() {
        return orderInGroup;
    }

    public void setOrderInGroup(int orderInGroup) {
        this.orderInGroup = orderInGroup;
    }

    public String getInvestigatorId() {
        return investigatorId;
    }

    public void setInvestigatorId(String investigatorId) {
        this.investigatorId = investigatorId;
    }

    public int getRandomNumberId() {
        return randomNumberId;
    }

    public void setRandomNumberId(int randomNumberId) {
        this.randomNumberId = randomNumberId;
    }

    public int getCurrentNumber() {
        return currentNumber;
    }

    public void setCurrentNumber(int currentNumber) {
        this.currentNumber = currentNumber;
    }

    public int getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(int organizationId) {
        this.organizationId = organizationId;
    }

    @Override
    public String toString() {
        return "OfferNumberStatus{" +
                "id=" + id +
                ", projectId=" + projectId +
                ", stratificationFactor='" + stratificationFactor + '\'' +
                ", groupNumber=" + groupNumber +
                ", orderInGroup=" + orderInGroup +
                ", currentNumber=" + currentNumber +
                ", investigatorId='" + investigatorId + '\'' +
                ", randomNumberId=" + randomNumberId +
                ", organizationId=" + organizationId +
                '}';
    }
}
