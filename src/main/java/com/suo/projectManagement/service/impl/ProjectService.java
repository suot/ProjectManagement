package com.suo.projectManagement.service.impl;

import com.suo.projectManagement.aop.PrintServiceName;
import com.suo.projectManagement.dao.*;
import com.suo.projectManagement.po.*;
import com.suo.projectManagement.utils.RoleUtil;
import com.suo.projectManagement.vo.ProjectInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Suo Tian on 2018/5/4.
 */

@Service
public class ProjectService {
    @Autowired
    IProjectRepo projectRepoImpl;

    @Autowired
    OrganizationService organizationService;

    @Autowired
    IInvestiOrganizationRepo investiOrganizationRepo;

    @Autowired
    IRoleInProjectRepo roleInProjectRepo;

    @Autowired
    IUserRepo userRepo;

    @Autowired
    ICurrentProjectRepo currentProjectRepo;

    @Autowired
    IOfferNumberResultRepo offerNumberResultRepo;

    @Autowired
    IRandomSettingsRepo randomSettingsRepo;


    @PrintServiceName(description = "项目管理&&添加了项目信息")
    public int createProject(Project project){
        return projectRepoImpl.add(project);
    }

    //@PrintServiceName(description = "项目管理&&查询单行")
    public Project getProjectById(int projectId) {
        return projectRepoImpl.getProjectById(projectId);
    }

    //@PrintServiceName(description = "项目管理&&查询所有行")
    public List<Project> getAllProjects(){
        return projectRepoImpl.getAllProjects();
    }

    @PrintServiceName(description = "项目管理&&修改了项目信息")
    public void updateProject(Project project){
        projectRepoImpl.update(project);
    }



    public List<ProjectInfo> getAllProjectInfo(){
        List<Project> projectList = this.getAllProjects();
        if(projectList.size()>0){
            List<ProjectInfo> projectInfoList = new ArrayList<>();
            Organization organization;
            ProjectInfo projectInfo;
            List<InvestiOrganization> investiOrganizationList;
            String researchOrganizationNames;
            List<RoleInProject> roleInProjectList;
            String projectManagerNames;
            int projectId;

            for (Project project : projectList){
                projectId = project.getId();
                organization = organizationService.getOrganizationById(project.getOrganizationId());
                investiOrganizationList = investiOrganizationRepo.get(projectId);
                researchOrganizationNames = this.getResearchOrganizationNames(investiOrganizationList);
                roleInProjectList = roleInProjectRepo.getRoleInProjectListByProjectIdAndRole(projectId, RoleUtil.ROLE_PROJECT_MANAGER);
                projectManagerNames = this.getProjectManagerNames(roleInProjectList);
                projectInfo = new ProjectInfo(project, organization, investiOrganizationList, researchOrganizationNames, roleInProjectList, projectManagerNames);
                projectInfoList.add(projectInfo);
            }
            return projectInfoList;
        }else {
            return null;
        }
    }

    public ProjectInfo getProjectInfoFromProject(Project project){
        int projectId = project.getId();

        Organization organization = organizationService.getOrganizationById(project.getOrganizationId());
        List<InvestiOrganization> investiOrganizationList = investiOrganizationRepo.get(projectId);
        String researchOrganizationNames = this.getResearchOrganizationNames(investiOrganizationList);
        List<RoleInProject> roleInProjectList = roleInProjectRepo.getRoleInProjectListByProjectIdAndRole(projectId, RoleUtil.ROLE_PROJECT_MANAGER);
        String projectManagerNames = this.getProjectManagerNames(roleInProjectList);

        return new ProjectInfo(project, organization, investiOrganizationList, researchOrganizationNames, roleInProjectList, projectManagerNames);

    }



    public int addResearchOrganizations(int projectId, String[] orgIds) throws Exception{
        int count=0;
        InvestiOrganization investiOrganization;
        if(orgIds != null){
            for (int i=0; i<orgIds.length; i++){
                investiOrganization = new InvestiOrganization(projectId, Integer.parseInt(orgIds[i]));
                count += investiOrganizationRepo.add(investiOrganization);
            }
            return count;
        }else {
            throw new Exception("输入的临床研究机构是空");
        }
    }

    public void updateInvestiOrganizations(int projectId, String[] orgIds){
        //String[] -> List<Integer>
        List<Integer> orgIdList = new ArrayList<>();
        for(int i=0; i<orgIds.length; i++){
            orgIdList.add(Integer.parseInt(orgIds[i]));
        }

        //query orgIdsInDB
        List<Integer> orgIdListInDB = investiOrganizationRepo.getOrgIds(projectId);

        //logic branches
        List<Integer> insertList=new ArrayList<>(), deleteList=new ArrayList<>();
        int orgIdListSize = orgIdList.size(), orgIdListInDBSize = orgIdListInDB.size();
        //该project在数据库里没有记录，也没有输入
        if(orgIdListInDBSize==0 && orgIdListSize==0){

        }
        //数据库里没有记录，但有输入，那么只进行插入操作
        else if(orgIdListInDBSize==0 && orgIdListSize>0){
            insertList.addAll(orgIdList);
        }
        //数据库里有记录，但没有输入，那么只进行删除操作
        else if(orgIdListInDBSize>0 && orgIdListSize==0){
            deleteList.addAll(orgIdListInDB);
        }
        //数据库里该role有记录，也有输入，那么就进行判断，是否需要删除和/或插入操作
        else if(orgIdListInDBSize>0 && orgIdListSize>0){
            deleteList.addAll(orgIdListInDB);
            for (Integer orgId : orgIdList) {
                if(deleteList.contains(orgId)){
                    //输入的orgId恰好也在数据库中就不删除。
                    deleteList.remove(orgId);
                }else {
                    insertList.add(orgId);
                }
            }
        }
        //删除输入没有而数据库里有的老数据
        InvestiOrganization investiOrganization;
        if(deleteList.size()>0) {            
            for (int orgId : deleteList) {
                investiOrganization = new InvestiOrganization(projectId, orgId);
                investiOrganizationRepo.delete(investiOrganization);
            }
        }
        //插入输入有而数据库里没有的新增数据
        if(insertList.size()>0) {
            for (int orgId : insertList) {
                investiOrganization = new InvestiOrganization(projectId, orgId);
                investiOrganizationRepo.add(investiOrganization);
            }
        }
    }

    public String getResearchOrganizationNames(List<InvestiOrganization> list){
        String researchOrganizationNames = "";
        if(list.size()>0) {
            String name;
            for (InvestiOrganization investiOrganization : list) {
                name = organizationService.getOrganizationById(investiOrganization.getOrgId()).getName();
                researchOrganizationNames = researchOrganizationNames + name + ", ";
            }
            researchOrganizationNames = researchOrganizationNames.substring(0, researchOrganizationNames.length()-2);
        }
        return researchOrganizationNames;
    }

    public Object[] getResearchOrganizationIds(int projectId){
        return investiOrganizationRepo.getOrgIds(projectId).toArray();
    }


    public int addProjectManagers(int projectId, String[] usernames){
        int count=0;
        RoleInProject roleInProject;
        CurrentProject currentProject;
        List<String> usernamesInCurrentProject = currentProjectRepo.getAllUsernames();
        String username;
        User user;

        for (int i=0; i<usernames.length; i++){
            username = usernames[i];
            roleInProject = new RoleInProject(projectId, username, RoleUtil.ROLE_PROJECT_MANAGER);
            count += roleInProjectRepo.add(roleInProject);

            //默认添加的user，activated="未激活"，添加了项目经理后要置为"已激活"
            user = userRepo.getUserByUsername(username);
            if(user.getActivated().equals("未激活")){
                user.setActivated("已激活");
                userRepo.updateByAdmin(user);
            }

            //如果currentproject表里有，说明该username在其他项目中存在，不需要在currentproject表里添加；若没有说明是第一次添加进系统的用户,要在currentproject里为该用户添加记录。
            if(!usernamesInCurrentProject.contains(username)){
                currentProject = new CurrentProject(username, projectId);
                currentProjectRepo.add(currentProject);
            }
        }
        return count;
    }

    public String getProjectManagerNames(List<RoleInProject> list){
        String projectManagerNames = "";
        if(list.size()>0) {
            String name;
            for (RoleInProject roleInProject : list) {
                name = userRepo.getUserByUsername(roleInProject.getUsername()).getName();
                projectManagerNames = projectManagerNames + name + ", ";
            }
            projectManagerNames = projectManagerNames.substring(0, projectManagerNames.length()-2);
        }
        return projectManagerNames;
    }

    public Object[] getProjectManagerUserNames(int projectId){
        return roleInProjectRepo.getUsernameList(projectId, RoleUtil.ROLE_PROJECT_MANAGER).toArray();
    }


    public List<ProjectInfo> getMyProjectInfo(String username){
        List<Integer> projectIdList = roleInProjectRepo.getProjectIdListByUsername(username);
        if(projectIdList.size()>0){
            List<ProjectInfo> projectInfoList = new ArrayList<ProjectInfo>();
            ProjectInfo projectInfo;
            Project project;
            List<RoleInProject> roleInProjectList;
            int progress, numberCount, sampleSize;
            RandomSettings randomSettings;


            for(int projectId : projectIdList){
                project = this.getProjectById(projectId);
                roleInProjectList = roleInProjectRepo.getRoleInProjectListByProjectId(projectId);

                numberCount = offerNumberResultRepo.countAll(projectId);
                randomSettings = randomSettingsRepo.find(projectId);
                if(randomSettings != null){
                    sampleSize = randomSettings.getSimpleSize();
                    if(sampleSize > 0) {
                        progress = numberCount * 100 / sampleSize;
                    }else {
                        progress = 0;
                    }
                }else {
                    progress = 0;
                }

                projectInfo = new ProjectInfo(project, roleInProjectList, progress);
                projectInfoList.add(projectInfo);
            }
            return projectInfoList;
        }else {
            return null;
        }
    }


    public List<String> getStatusEnumValues(){
        List<String> statusEnum = new ArrayList<>();
        statusEnum.add("启动");
        statusEnum.add("准备中");
        statusEnum.add("暂停");
        statusEnum.add("完成");
        return statusEnum;
    }

    public List<String> getModuleEnumValues(){
        List<String> moduleEnum = new ArrayList<>();
        moduleEnum.add("中央随机 CRS");
        moduleEnum.add("药物管理 CWS");
        moduleEnum.add("电子数据录入 EDC");
        moduleEnum.add("受试者自评 PRO");
        return moduleEnum;
    }
}
