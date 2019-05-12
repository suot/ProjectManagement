package com.suo.projectManagement.vo;


import com.suo.projectManagement.po.Stratification;

import java.util.ArrayList;

/**
 * Created by wuxu on 2018/5/30.
 * 分层信息
 */
public class StratifiInfo {
    private  String stratifiName; //分层名称
    private String  stratifiId;    //分层编号
    private ArrayList<StratifiFactorInfo> stratifiFactors =new ArrayList<>();

    public StratifiInfo() {
    }

    public String getStratifiId() {
        return stratifiId;
    }

    public void setStratifiId(String stratifiId) {
        this.stratifiId = stratifiId;
    }

    public StratifiInfo(String stratifiName) {
        this.stratifiName = stratifiName;
    }

    public String getStratifiName() {
        return stratifiName;
    }

    public void setStratifiName(String stratifiName) {
        this.stratifiName = stratifiName;
    }
    public ArrayList<StratifiFactorInfo> getStratifiFactors() {
        return this.stratifiFactors;
    }

    //获取所有分层因素的id
    public ArrayList<String> getFactorKeys(){
        ArrayList<StratifiFactorInfo> stratifiFactors = this.stratifiFactors;
        ArrayList<String> rtn = new ArrayList<>();
        for (StratifiFactorInfo stratifiFactorInfo: stratifiFactors){
            rtn.add(stratifiFactorInfo.getStratifiFactorId());
        }
        return rtn;
    }

    //转换成 分层表
    public ArrayList<Stratification> parseToStraification(int projectId){
        ArrayList<Stratification> stratifications = new ArrayList<>();

        for (StratifiFactorInfo stratifiFactorInfo: stratifiFactors){
            Stratification s = new Stratification();
            s.setProjectId(projectId);
            s.setStratifiId(this.stratifiId);
            s.setStratifiName(this.stratifiName);
            s.setStratifiFactorId(stratifiFactorInfo.getStratifiFactorId());
            s.setStratifiFactorName(stratifiFactorInfo.getStratifiFactorName());
            stratifications.add(s);
        }
        return stratifications;
    }


    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("StratifiInfo{ stratifiName='" + stratifiName +":");
        for (StratifiFactorInfo stratifiFactorInfo : stratifiFactors) {

            stringBuilder.append(stratifiFactorInfo.toString());

        }
        stringBuilder.append( "}");
        return stringBuilder.toString();
    }
}
