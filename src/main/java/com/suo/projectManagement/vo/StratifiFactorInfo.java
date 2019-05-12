package com.suo.projectManagement.vo;

/**
 * Created by wuxu on 2018/5/30.
 */
public class StratifiFactorInfo {
    private String stratifiFactorName;
    private String stratifiFactorId;

    public String getStratifiFactorName() {
        return stratifiFactorName;
    }

    public void setStratifiFactorName(String stratifiFactorName) {
        this.stratifiFactorName = stratifiFactorName;
    }

    public String getStratifiFactorId() {
        return stratifiFactorId;
    }

    public void setStratifiFactorId(String stratifiFactorId) {
        this.stratifiFactorId = stratifiFactorId;
    }

    @Override
    public String toString() {
        return "StratifiFactorInfo{" +
                "stratifiFactorName='" + stratifiFactorName + '\'' +
                ", stratifiFactorId='" + stratifiFactorId + '\'' +
                '}';
    }
}
