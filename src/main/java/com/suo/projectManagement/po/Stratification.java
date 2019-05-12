package com.suo.projectManagement.po;

/**
 * Created by wuxu on 2018/5/30.
 */
public class Stratification {
    private int id;
    private int projectId;
    private String stratifiId;
    private String stratifiName;
    private String stratifiFactorId;
    private String stratifiFactorName;

    public Stratification(){}

    public Stratification(int projectId, String stratifiId, String stratifiName, String stratifiFactorId, String stratifiFactorName) {
        this.projectId = projectId;
        this.stratifiId = stratifiId;
        this.stratifiName = stratifiName;
        this.stratifiFactorId = stratifiFactorId;
        this.stratifiFactorName = stratifiFactorName;
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

    public String getStratifiId() {
        return stratifiId;
    }

    public void setStratifiId(String stratifiId) {
        this.stratifiId = stratifiId;
    }

    public String getStratifiName() {
        return stratifiName;
    }

    public void setStratifiName(String stratifiName) {
        this.stratifiName = stratifiName;
    }

    public String getStratifiFactorId() {
        return stratifiFactorId;
    }

    public void setStratifiFactorId(String stratifiFactorId) {
        this.stratifiFactorId = stratifiFactorId;
    }

    public String getStratifiFactorName() {
        return stratifiFactorName;
    }

    public void setStratifiFactorName(String stratifiFactorName) {
        this.stratifiFactorName = stratifiFactorName;
    }

    @Override
    public String toString() {
        return "Stratification{" +
                "id=" + id +
                ", projectId=" + projectId +
                ", stratifiId='" + stratifiId + '\'' +
                ", stratifiName='" + stratifiName + '\'' +
                ", stratifiFactorId='" + stratifiFactorId + '\'' +
                ", stratifiFactorName='" + stratifiFactorName + '\'' +
                '}';
    }
}
