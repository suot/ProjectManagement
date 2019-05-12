package com.suo.projectManagement.dao;

import com.suo.projectManagement.po.OfferNumberResult;
import com.suo.projectManagement.vo.echarts.StatisticalLine;

import java.util.List;

public interface IOfferNumberResultRepo {
    public int add(OfferNumberResult offerNumberResult);
    public List<OfferNumberResult> getAll(int projectID);
    public int countAll(int projectID);
    int countByOrg(int projectId,int organizationId);
    List<StatisticalLine> countForEcharts(int projectId, int organizationId);
    List<Integer> getOrgIdList(int projectId);
    List<String> getDateList(int projectId);
}
