package com.suo.projectManagement.vo;

import com.suo.projectManagement.po.InvestiOrganization;
import com.suo.projectManagement.po.Organization;

/**
 * Created by TianSuo on 2018-07-23.
 */
public class InvestiOrganizationInfo {
    private InvestiOrganization investiOrganization;
    private Organization organization;

    public InvestiOrganizationInfo(InvestiOrganization investiOrganization, Organization organization) {
        this.investiOrganization = investiOrganization;
        this.organization = organization;
    }

    public InvestiOrganization getInvestiOrganization() {
        return investiOrganization;
    }

    public void setInvestiOrganization(InvestiOrganization investiOrganization) {
        this.investiOrganization = investiOrganization;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    @Override
    public String toString() {
        return "InvestiOrganizationInfo{" +
                "investiOrganization=" + investiOrganization +
                ", organization=" + organization +
                '}';
    }
}
