package com.suo.projectManagement.vo.echarts;

/**
 * Created by TianSuo on 2018-07-27.
 */
public class StatisticalLine {
    private int orgId;
    private String sj;
    private int number;

    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getSj() {
        return sj;
    }

    public void setSj(String sj) {
        this.sj = sj;
    }

    @Override
    public String toString() {
        return "StatisticalLine{" +
                "orgId=" + orgId +
                ", sj='" + sj + '\'' +
                ", number=" + number +
                '}';
    }
}
