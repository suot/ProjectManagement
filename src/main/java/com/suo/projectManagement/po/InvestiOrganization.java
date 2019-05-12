package com.suo.projectManagement.po;

/**
 * Created by wuxu on 2018/5/30.
 */
public class InvestiOrganization {
    private int id;
    private int projectId;
    private int orgId; //参试机构id
    private int enrollNumber;  //控制受试者数量
    private String valid;

    public InvestiOrganization() {
    }

    public InvestiOrganization(int projectId, int orgId) {
        this.projectId = projectId;
        this.orgId = orgId;
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

    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    public int getEnrollNumber() {
        return enrollNumber;
    }

    public void setEnrollNumber(int enrollNumber) {
        this.enrollNumber = enrollNumber;
    }

    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid;
    }

    @Override
    public String toString() {
        return "InvestiOrganization{" +
                "id=" + id +
                ", projectId=" + projectId +
                ", orgId=" + orgId +
                ", enrollNumber=" + enrollNumber +
                ", valid='" + valid + '\'' +
                '}';
    }
}
