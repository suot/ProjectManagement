package com.suo.projectManagement.dao;

import com.suo.projectManagement.po.InvestiOrganization;

import java.util.List;

/**
 * Created by wuxu on 2018/5/30.
 */
public interface IInvestiOrganizationRepo {
    int add(InvestiOrganization investiOrganization);
    int updateEnrollNumber(InvestiOrganization investiOrganization);
    int updateStatus(InvestiOrganization investiOrganization);
    List<InvestiOrganization> get(int projectId);

    /**
     * 获得项目中所有参试单位id
     * @param projectId
     * @return
     */
    List<Integer> getOrgIds(int projectId);
    int getEnrollNumber(int projectId,int orgId);
    String getStatus(int projectId,int orgId);
    int delete(InvestiOrganization investiOrganization);
}
