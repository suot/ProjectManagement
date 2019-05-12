package com.suo.projectManagement.dao;

import com.suo.projectManagement.po.Organization;

import java.util.List;

/**
 * Created by TianSuo on 2018-07-04.
 */
public interface IOrganizationRepo{
    int insertOrganization(Organization organization);
    void updateOrganization(Organization organization);
    List<Organization> getAllOrganizations();
    Organization getOrganizationById(int id);
}
