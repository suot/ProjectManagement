package com.suo.projectManagement.controller;

import com.suo.projectManagement.po.*;
import com.suo.projectManagement.service.IGenerateRandomService;
import com.suo.projectManagement.service.impl.*;
import com.suo.projectManagement.utils.RoleUtil;
import com.suo.projectManagement.vo.ProjectInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Suo Tian on 2018-05-17.
 */


@Controller
public class ProjectController {
    @Autowired
    ProjectService projectService;
    @Autowired
    UserService userService;
    @Autowired
    CurrentProjectService currentProjectService;
    @Autowired
    RoleInProjectService roleInProjectService;
    @Autowired
    OrganizationService organizationService;
    @Autowired
    MainController mainController;
    @Autowired
    MyUserDetailsService myUserDetailsService;

    @Autowired
    IGenerateRandomService generateRandomService;

    private static Logger logger = LoggerFactory.getLogger(MainController.class);

    //项目查询
    @RequestMapping("/project/list_all_projects")
    public String listAllProjects(Model model){
        return "/project :: list_all_projects";
    }

    @ResponseBody
    @RequestMapping("/project/list_all_projects/queryProjectList")
    public List<ProjectInfo> queryProjectInfoList(){
        logger.info("Entered page: /project/list_all_projects/queryProjectInfoList");

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<ProjectInfo> projectInfoList;

        if(UserService.USER_ADMIN == username){
            projectInfoList = projectService.getAllProjectInfo();
        }else {
            int projectId = currentProjectService.getCurrentProjectByUsername(username).getProjectId();
            Project project = projectService.getProjectById(projectId);
            projectInfoList = new ArrayList<>();
            projectInfoList.add(projectService.getProjectInfoFromProject(project));
        }

        return projectInfoList;
    }

    //项目添加
    @RequestMapping("/project/create")
    public String createProject(Model model){
        logger.info("Entered page: /project/create");
        List<Organization> allOrganizations = organizationService.getAllOrganizations();
        model.addAttribute("allOrganizations", allOrganizations);

        List<User> allUsers = userService.getAllUsers();
        model.addAttribute("allUsers", allUsers);

        model.addAttribute("statusEnumValues", projectService.getStatusEnumValues());
        model.addAttribute("moduleEnumValues", projectService.getModuleEnumValues());

        model.addAttribute("project", new Project());
        return "/project :: create_project";
    }

    @ResponseBody
    @PostMapping("/project/create/submit")
    public ProjectInfo createProjectSubmit(Model model, Project project, HttpServletRequest request) throws Exception{
        logger.info("Entered page: /project/create/submit");

        //id是数据库自动生成的，所以要取出来塞入project对象
        int projectId = projectService.createProject(project);
        logger.info("Project creation succeeded: projectId="+projectId);
        project.setId(projectId);


        /*
            一个表单中要提交多个对象，怎么办:
            html form中定义th:object="${project}" method="post"，project对象的field前面加上th, 其他值不加th.
            js ajax中：type: 'post', url: '/project/create/submit', data: $("#form_create_project").serialize(),
            controller中: project对象直接取field，其他对象request.getParameter或者getParameterValues取数组或列表。
        */

        //插入roleInProject表和currentProject表
        String[] projectManagerUserNames = request.getParameterValues("projectManagerUserNames");
        int affectedCountOfProjectManagerUserNames = projectService.addProjectManagers(projectId, projectManagerUserNames);

        //插入investiOrganization表
        String[] researchOrganizationIds = request.getParameterValues("researchOrganizationIds");
        int affectedCountOfResearchOrganizationIds = projectService.addResearchOrganizations(projectId, researchOrganizationIds);

        //根据projectId从数据库里读project, roleInProject, investiOrganization表，组织成projectInfo对象，返回给js显示。
        ProjectInfo projectInfo = projectService.getProjectInfoFromProject(project);
        return projectInfo;
    }

    //项目修改
    @RequestMapping("/project/modify")
    public String modifyProject(Model model, @RequestParam int id){
        logger.info("Entered page: /project/modify");
        logger.info("project's id is " + id);

        Project project = projectService.getProjectById(id);
        model.addAttribute("project", project);

        List<Organization> allOrganizations = organizationService.getAllOrganizations();
        model.addAttribute("allOrganizations", allOrganizations);

        List<User> allUsers = userService.getAllUsers();
        model.addAttribute("allUsers", allUsers);

        model.addAttribute("orgId", project.getOrganizationId());
        model.addAttribute("statusEnumValues", projectService.getStatusEnumValues());
        model.addAttribute("moduleEnumValues", projectService.getModuleEnumValues());
        model.addAttribute("updatedProject", new Project());

        return "/project :: modify_project";
    }

    @ResponseBody
    @RequestMapping("/project/modify/queryCurrentProjectManagers")
    public Object[] getCurrentProjectManagers(@RequestParam int projectId){
        logger.info("Entered page: /project/modify/queryCurrentProjectManagers?projectId="+projectId);
        Object[] currentProjectManagers = projectService.getProjectManagerUserNames(projectId);
        return currentProjectManagers;
    }


    @ResponseBody
    @RequestMapping("/project/modify/queryCurrentResearchOrganizations")
    public Object[] getCurrentResearchOrganizations(@RequestParam int projectId){
        logger.info("Entered page: /project/modify/queryCurrentResearchOrganizations?projectId="+projectId);
        Object[] currentResearchOrganizations = projectService.getResearchOrganizationIds(projectId);
        return currentResearchOrganizations;
    }


    @ResponseBody
    @RequestMapping("/project/modify/queryCurrentRandomSettings")
    public Boolean queryCurrentRandomSettings(@RequestParam int projectId){
        logger.info("Entered page: /project/modify/queryCurrentRandomSettings?projectId="+projectId);
        RandomSettings randomSettings = generateRandomService.findRandomSettings(projectId);
        if(randomSettings == null){
            return false;
        }else {
            return true;
        }
    }



    //表单提交js中用post,表单serialize, controller中参数直接是实体类
    @ResponseBody
    @PostMapping("/project/modify/submit")
    public ProjectInfo modifyProjectSubmit(Project updatedProject, HttpServletRequest request){
        logger.info("Entered page: /project/modify/submit");
        projectService.updateProject(updatedProject);

        String[] projectManagerUserNames = request.getParameterValues("projectManagerUserNames");
        roleInProjectService.setRoleInProject(updatedProject.getId(), projectManagerUserNames, RoleUtil.ROLE_PROJECT_MANAGER);

        String[] researchOrganizationIds = request.getParameterValues("researchOrganizationIds");
        projectService.updateInvestiOrganizations(updatedProject.getId(), researchOrganizationIds);

        ProjectInfo projectInfo = projectService.getProjectInfoFromProject(updatedProject);
        logger.info("project information is modified. Project\'s id is " + updatedProject.getId());
        return projectInfo;
    }



    @RequestMapping(value = {"/project/list_my_projects"})
    public String listMyProjects(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<ProjectInfo> projectInfoList = projectService.getMyProjectInfo(username);
        model.addAttribute("projectInfoList", projectInfoList);
//        //为myProjects页面中的项目列表赋值
//        List<Project> projectList = roleInProjectService.getMyProjects(username);
//        model.addAttribute("myProjects", projectList);
//
//        //得到我参与的每个项目中的roleInproject信息，存入Map中
//        Map<Integer, List<RoleInProject>> roleInProjectsMap = roleInProjectService.getUsersInMyProjects(username);
//        model.addAttribute("roleInProjectsMap", roleInProjectsMap);
//
//        //尚未用到
//        Map<String, User> userMap = userService.getUserMapByUsername();
//        model.addAttribute("userMap", userMap);

        return "/project :: list_my_projects";
    }

    @RequestMapping(value = {"/project/change_project"})
    public String changeProject(Model model, @RequestParam int projectId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        //更新数据库中的currentproject表，用于下次登录时获取角色。
        CurrentProject currentProject= new CurrentProject(username, projectId);
        currentProjectService.updateCurrentProject(currentProject);

        //取老项目中的Authentication,更新它的Authorities，然后再装回SecurityContextHolder。
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Authentication authWithUpdatedAuthorities = myUserDetailsService.updateAuthentication(auth, projectId);
        SecurityContextHolder.getContext().setAuthentication(authWithUpdatedAuthorities);

        return mainController.returnBackToMainPage(model, projectId, username);
    }

}
