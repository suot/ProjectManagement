package com.suo.projectManagement.service;

import com.suo.projectManagement.vo.ReturnEntity;
import com.suo.projectManagement.vo.echarts.EChartsMap;
import com.suo.projectManagement.vo.echarts.StatisticalChart;

import java.util.List;


public interface IOfferNumberService {
    ReturnEntity competitiveRan(int projectId, String StratificationFactor , String investigatorId, String subjectId,int organizationId);
    ReturnEntity balancedRan(int projectId,String StratificationFactor ,String investigatorId,String subjectId,int organizationId);
    ReturnEntity mixedRan(int projectId,String StratificationFactor ,String investigatorId,String subjectId,int organizationId);
    //String getRnid(int projectId, int orgId);
    int getCountOfRandomNumberUsed(int projectId);
    int getTrailDuration(int projectId);
    List<StatisticalChart> getOfferNumberChart(int projectId);
    EChartsMap getOptionsForEChart(int projectId);
}
