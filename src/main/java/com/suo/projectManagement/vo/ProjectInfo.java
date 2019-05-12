package com.suo.projectManagement.vo;

import com.suo.projectManagement.po.InvestiOrganization;
import com.suo.projectManagement.po.Organization;
import com.suo.projectManagement.po.Project;
import com.suo.projectManagement.po.RoleInProject;

import java.util.List;

/**
 * Created by TianSuo on 2018-07-09.
 */
public class ProjectInfo {
    private Project project;
    private Organization organization;
    private List<InvestiOrganization> investiOrganizationList;
    private String researchOrganizationNames;
    private List<RoleInProject> roleInProjectList;
    private String projectManagerNames;
    private int progress;


    public ProjectInfo(){
    }

    public ProjectInfo(Project project, Organization organization, List<InvestiOrganization> investiOrganizationList, List<RoleInProject> roleInProjectList) {
        this.project = project;
        this.organization = organization;
        this.investiOrganizationList = investiOrganizationList;
        this.roleInProjectList = roleInProjectList;
    }

    public ProjectInfo(Project project, Organization organization, List<InvestiOrganization> investiOrganizationList, String researchOrganizationNames, List<RoleInProject> roleInProjectList, String projectManagerNames) {
        this.project = project;
        this.organization = organization;
        this.investiOrganizationList = investiOrganizationList;
        this.researchOrganizationNames = researchOrganizationNames;
        this.roleInProjectList = roleInProjectList;
        this.projectManagerNames = projectManagerNames;
    }

    public ProjectInfo(Project project, List<RoleInProject> roleInProjectList, int progress) {
        this.project = project;
        this.roleInProjectList = roleInProjectList;
        this.progress = progress;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public List<InvestiOrganization> getInvestiOrganizationList() {
        return investiOrganizationList;
    }

    public void setInvestiOrganizationList(List<InvestiOrganization> investiOrganizationList) {
        this.investiOrganizationList = investiOrganizationList;
    }

    public String getResearchOrganizationNames() {
        return researchOrganizationNames;
    }

    public void setResearchOrganizationNames(String researchOrganizationNames) {
        this.researchOrganizationNames = researchOrganizationNames;
    }

    public List<RoleInProject> getRoleInProjectList() {
        return roleInProjectList;
    }

    public void setRoleInProjectList(List<RoleInProject> roleInProjectList) {
        this.roleInProjectList = roleInProjectList;
    }

    public String getProjectManagerNames() {
        return projectManagerNames;
    }

    public void setProjectManagerNames(String projectManagerNames) {
        this.projectManagerNames = projectManagerNames;
    }


    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    @Override
    public String toString() {
        return "ProjectInfo{" +
                "project=" + project +
                ", organization=" + organization +
                ", investiOrganizationList=" + investiOrganizationList +
                ", researchOrganizationNames='" + researchOrganizationNames + '\'' +
                ", roleInProjectList=" + roleInProjectList +
                ", projectManagerNames='" + projectManagerNames + '\'' +
                ", progress=" + progress +
                '}';
    }
}
