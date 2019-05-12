package com.suo.projectManagement.service.impl;

import com.suo.projectManagement.aop.PrintServiceName;
import com.suo.projectManagement.dao.IInvestiOrganizationRepo;
import com.suo.projectManagement.dao.IOrganizationRepo;
import com.suo.projectManagement.dao.IProjectRepo;
import com.suo.projectManagement.po.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TianSuo on 2018-05-17.
 */
@Service
public class OrganizationService {
    @Autowired
    IOrganizationRepo organizationRepoImpl;

    @Autowired
    IProjectRepo projectRepoImpl;

    @Autowired
    IInvestiOrganizationRepo investiOrganizationRepoImpl;


    //@PrintServiceName(description = "机构管理&&查询单行")
    public Organization getOrganizationById(int organizationId) {
        return organizationRepoImpl.getOrganizationById(organizationId);
    }

    //@PrintServiceName(description = "机构管理&&查询所有行")
    public List<Organization> getAllOrganizations(){
        return organizationRepoImpl.getAllOrganizations();
    }

    public List<Organization> getOrganizationsByProjectId(int projectId){
        List<Organization> organizationList = new ArrayList<>();
        List<Integer> allOrgIds = new ArrayList<>();

        //申办机构
        int sponsorOrgId = projectRepoImpl.getProjectById(projectId).getOrganizationId();
        allOrgIds.add(sponsorOrgId);
        //参试机构
        List<Integer> researchOrgIds = investiOrganizationRepoImpl.getOrgIds(projectId);
        if(researchOrgIds != null && researchOrgIds.size()>0){
            //去重
            if(researchOrgIds.contains(sponsorOrgId)){
                researchOrgIds.remove(sponsorOrgId);
            }
            allOrgIds.addAll(researchOrgIds);
        }

        for(int orgId : allOrgIds){
            organizationList.add(organizationRepoImpl.getOrganizationById(orgId));
        }
        return organizationList;
    }

    @PrintServiceName(description = "机构管理&&添加了机构信息")
    public int createOrganization(Organization organization){
        return organizationRepoImpl.insertOrganization(organization);
    }

    @PrintServiceName(description = "机构管理&&修改了机构信息")
    public void updateOrganization(Organization organization){
        organizationRepoImpl.updateOrganization(organization);
    }

    public List<String> getOrganizationPropertyEnumValues(){
        List<String> organizationPropertyEnum = new ArrayList<>();
        organizationPropertyEnum.add("医院");
        organizationPropertyEnum.add("制药厂");
        organizationPropertyEnum.add("药监局");
        organizationPropertyEnum.add("统计机构");
        return organizationPropertyEnum;
    }
}
