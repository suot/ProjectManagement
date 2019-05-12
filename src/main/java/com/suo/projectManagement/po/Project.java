package com.suo.projectManagement.po;

import java.util.Date;

/**
 * Created by Suo Tian on 2018-04-26.
 */
public class Project {
    private int id;
    private String projectNumber;
    private String researchId;
    private String abbreviation;
    private int organizationId;
    private String status;
    private String moduleList;
    private String planNumber;
    private String batchNumber;
    private String ethicNumber;
    private String description;
    private Date createTime;

    public Project(){

    }

    public Project(int id, String projectNumber, String researchId, String abbreviation, int organizationId, String status, String moduleList, String planNumber, String batchNumber, String ethicNumber, String description) {
        this.id = id;
        this.projectNumber = projectNumber;
        this.researchId = researchId;
        this.abbreviation = abbreviation;
        this.organizationId = organizationId;
        this.status = status;
        this.moduleList = moduleList;
        this.planNumber = planNumber;
        this.batchNumber = batchNumber;
        this.ethicNumber = ethicNumber;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProjectNumber() {
        return projectNumber;
    }

    public void setProjectNumber(String projectNumber) {
        this.projectNumber = projectNumber;
    }

    public String getResearchId() {
        return researchId;
    }

    public void setResearchId(String researchId) {
        this.researchId = researchId;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public int getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(int organizationId) {
        this.organizationId = organizationId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getModuleList() {
        return moduleList;
    }

    public void setModuleList(String moduleList) {
        this.moduleList = moduleList;
    }

    public String getPlanNumber() {
        return planNumber;
    }

    public void setPlanNumber(String planNumber) {
        this.planNumber = planNumber;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public String getEthicNumber() {
        return ethicNumber;
    }

    public void setEthicNumber(String ethicNumber) {
        this.ethicNumber = ethicNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", projectNumber='" + projectNumber + '\'' +
                ", researchId='" + researchId + '\'' +
                ", abbreviation='" + abbreviation + '\'' +
                ", organizationId='" + organizationId + '\'' +
                ", status='" + status + '\'' +
                ", moduleList='" + moduleList + '\'' +
                ", planNumber='" + planNumber + '\'' +
                ", batchNumber='" + batchNumber + '\'' +
                ", ethicNumber='" + ethicNumber + '\'' +
                ", description='" + description + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
