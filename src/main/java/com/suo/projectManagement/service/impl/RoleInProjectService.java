package com.suo.projectManagement.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.suo.projectManagement.aop.PrintServiceName;
import com.suo.projectManagement.dao.*;
import com.suo.projectManagement.po.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by TianSuo on 2018-05-08.
 */
@Service
public class RoleInProjectService {
    @Autowired
    IRoleInProjectRepo roleInProjectRepo;

    @Autowired
    ProjectService projectService;

    @Autowired
    IUserRepo userRepo;

    @Autowired
    ICurrentProjectRepo currentProjectRepo;


    public ArrayList<String> getAllRoles() {
        ArrayList<String> result = new ArrayList<String>();
        //result.add("ROLE_admin");
        result.add("projectManager");
        result.add("doctor");
        result.add("statistician");
        return result;
    }

//    public List<String> getRolesByUsername(String username){
//        return roleInProjectRepository.getRolesByUsername(username);
//    }

    private void insertRoleInProject(RoleInProject roleInProject){
        roleInProjectRepo.add(roleInProject);
    }

    //@PrintServiceName(description = "角色管理&&根据项目编号和角色查询")
    public String[] getUsernamesByRole(int projectId, String role){
        List<RoleInProject> roleInProjectList = roleInProjectRepo.getRoleInProjectListByProjectIdAndRole(projectId, role);
        if(roleInProjectList.size()>0){
            int length = roleInProjectList.size();
            String[] usernameArray = new String[length];
            for (int i=0; i<length; i++) {
                usernameArray[i] = roleInProjectList.get(i).getUsername();
            }
            return usernameArray;
        }else{
            return null;
        }
    }


    //@PrintServiceName(description = "角色管理&&根据项目编号查询")
    public List<RoleInProject> getRoleInProjectList(int projectId){
        return roleInProjectRepo.getRoleInProjectListByProjectId(projectId);
    }


    @PrintServiceName(description = "角色管理&&设置了角色")
    public void setRoleInProject(int projectId, String[] usernames, String role){
        //String[] -> List<String>
        List<String> pmList = new ArrayList<>();
        if(usernames != null) {
            for (int i = 0; i < usernames.length; i++) {
                pmList.add(usernames[i]);
            }
        }
        //query pmListInDB
        List<String> pmListInDB = roleInProjectRepo.getUsernameList(projectId, role);

        //logic branches
        List<String> insertList=new ArrayList<>(), deleteList=new ArrayList<>();
        int pmListSize = pmList.size(), pmListInDBSize = pmListInDB.size();
        //数据库里没有记录，也没有输入
        if(pmListSize==0 && pmListInDBSize==0){

        }
        //数据库里没有记录，但有输入，那么只进行插入操作
        else if(pmListInDBSize==0 && pmListSize>0){
            insertList.addAll(pmList);
        }
        //数据库里有记录，但没有输入，那么只进行删除操作
        else if(pmListInDBSize>0 && pmListSize==0){
            deleteList.addAll(pmListInDB);
        }
        //数据库里有记录，也有输入，那么就进行判断，是否需要删除和/或插入操作
        else if(pmListInDBSize>0 && pmListSize>0){
            //先假设数据库中原有的pm都要删除
            deleteList.addAll(pmListInDB);
            for (String pm : pmList) {
                if(deleteList.contains(pm)){
                    //输入的pm恰好也在数据库中就不删除。
                    deleteList.remove(pm);
                }else {
                    //输入的pm没在数据库中就要添加。
                    insertList.add(pm);
                }
            }
        }
        this.deteleUserFromProject(projectId, deleteList, role);
        this.addUserToProject(projectId, insertList, role);
    }


    public void deteleUserFromProject(int projectId, List<String> deleteList, String role){
        //删除输入中没有而数据库里有的老数据，更新3个表：roleInProject, currentProject, user.
        RoleInProject roleInProject;
        List<String> rolesInDB;
        CurrentProject currentProject, currentProjectInDB;
        List<Integer> projectList;
        User user;

        if(deleteList.size()>0) {
            for (String pm : deleteList) {
                roleInProject = new RoleInProject(projectId, pm, role);
                roleInProjectRepo.delete(roleInProject);

                //若pm在projectId中只有这一个权限且他的current project恰好是projectId，删除currentProject中的这个(pm, projectId)
                rolesInDB = roleInProjectRepo.getRolesInProject(projectId, pm);
                rolesInDB.remove(role);

                currentProjectInDB = currentProjectRepo.getCurrentProjectByUsername(pm);

                if(rolesInDB.size()==0 && currentProjectInDB.getProjectId()==projectId){
                    currentProject = new CurrentProject(pm, projectId);
                    currentProjectRepo.delete(currentProject);
                }

                projectList = roleInProjectRepo.getProjectIdListByUsername(pm);
                if(projectList == null || projectList.size()==0){
                    //在roleInProject中删除(projectId, username, projectManager)这行记录之后，如果pm在别的项目中有角色，或者在这个projectId项目中有普通角色，那么不改变他的activated.否则就说明他不在任何项目中了，就把他的activated设为“未激活”
                    user = userRepo.getUserByUsername(pm);
                    if(user.getActivated().equals("已激活")){
                        user.setActivated("未激活");
                        userRepo.updateByAdmin(user);
                    }
                }
            }
        }
    }

    public void addUserToProject(int projectId, List<String> insertList, String role){
        //插入输入中有而数据库里没有的新增数据, 更新3个表：roleInProject, currentProject, user.
        RoleInProject roleInProject;
        CurrentProject currentProject, currentProjectInDB;
        User user;

        if(insertList.size()>0) {
            for (String pm : insertList) {
                roleInProject = new RoleInProject(projectId, pm, role);
                roleInProjectRepo.add(roleInProject);

                //默认添加的user，activated="未激活"，添加了项目经理后要置为"已激活"
                user = userRepo.getUserByUsername(pm);
                if(user.getActivated().equals("未激活")){
                    user.setActivated("已激活");
                    userRepo.updateByAdmin(user);
                }

                //若(pm, projectId)不在currentProject中，就添加进去。否则说明该pm或者在其他项目中，或者以其他role在这个项目中。
                currentProjectInDB = currentProjectRepo.getCurrentProjectByUsername(pm);//因为currentProject表里username是unique的，所以该pm最多只能有1条记录
                if(currentProjectInDB == null){
                    currentProject = new CurrentProject(pm, projectId);
                    currentProjectRepo.add(currentProject);
                }
            }
        }
    }

    public List<String> getRolesInProject(int projectId, String username){
        return roleInProjectRepo.getRolesInProject(projectId,username);
    }


//    //得到我参与的所有的项目的细节信息
//    public List<Project> getMyProjects(String username){
//        List<Integer> projectIdList = roleInProjectRepo.getProjectIdListByUsername(username);
//        if(projectIdList.size()>0){
//            List<Project> projectList = new ArrayList<Project>();
//            for(int projectId : projectIdList){
//                Project project = projectService.getProjectById(projectId);
//                projectList.add(project);
//            }
//            return projectList;
//        }else {
//            return null;
//        }
//    }
//
//    public Map<Integer, List<RoleInProject>> getUsersInMyProjects(String username){
//        List<Integer> projectIdList = roleInProjectRepo.getProjectIdListByUsername(username);
//        if(projectIdList.size()>0){
//            Map<Integer, List<RoleInProject>> roleInProjectListMap = new HashMap<>();
//            for(int projectId : projectIdList){
//                List<RoleInProject> roleInProjectList = roleInProjectRepo.getRoleInProjectListByProjectId(projectId);
//                roleInProjectListMap.put(projectId, roleInProjectList);
//            }
//            return roleInProjectListMap;
//        }else{
//            return null;
//        }
//    }

    public String getRoles(int projectId, String username){
        List<String> roles = this.getRolesInProject(projectId, username);
        String role="";
        int size = roles.size();
        if(size>0) {
            for (String r : roles){
                role = role + r.trim() + "_";
            }
            role = role.substring(0, role.length()-1);
        }
        return role;
    }

}
