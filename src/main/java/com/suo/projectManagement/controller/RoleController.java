package com.suo.projectManagement.controller;

import com.suo.projectManagement.po.User;
import com.suo.projectManagement.service.impl.CurrentProjectService;
import com.suo.projectManagement.service.impl.MyUserDetailsService;
import com.suo.projectManagement.service.impl.RoleInProjectService;
import com.suo.projectManagement.service.impl.UserService;
import com.suo.projectManagement.utils.RoleUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Suo Tian on 2018-05-20.
 */
@Controller
public class RoleController {
    private static Logger logger = LoggerFactory.getLogger(MainController.class);

    @Autowired
    RoleInProjectService roleInProjectService;
    @Autowired
    CurrentProjectService currentProjectService;
    @Autowired
    UserService userService;
    @Autowired
    MyUserDetailsService myUserDetailsService;
    @Autowired
    MainController mainController;

    @RequestMapping("/roles/set")
    public String setRoles(Model model){
        logger.info("Entered page: /roles/set");
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        int projectId = currentProjectService.getCurrentProjectByUsername(username).getProjectId();

        List<List<User>> doctorGroupList = userService.getUserByOrganizationGroup(projectId);
        model.addAttribute("doctorGroupList", doctorGroupList);

        List<List<User>> userGroupList = userService.getUserByOrganizationGroup();
        model.addAttribute("userGroupList", userGroupList);

        return "/role :: set_roles";
    }

    @ResponseBody
    @RequestMapping("/roles/set/queryCurrentRoles")
    public Map<String, String[]> queryCurrentRoles(Model model){
        logger.info("Entered page: /roles/set/queryCurrentRoles");
        //查询各个权限中已被选中的人员
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        int projectId = currentProjectService.getCurrentProjectByUsername(username).getProjectId();

        //String[] projectManagers = roleInProjectService.getUsernamesByRole(projectId,RoleUtil.ROLE_PROJECT_MANAGER);
        String[] doctors = roleInProjectService.getUsernamesByRole(projectId, RoleUtil.ROLE_DOCTOR);
        String[] statisticians = roleInProjectService.getUsernamesByRole(projectId,RoleUtil.ROLE_STATISTICIAN);

        Map<String, String[]> usersInAllRoles = new HashMap<>();
        //usersInAllRoles.put(RoleUtil.ROLE_PROJECT_MANAGER, projectManagers);
        usersInAllRoles.put(RoleUtil.ROLE_DOCTOR, doctors);
        usersInAllRoles.put(RoleUtil.ROLE_STATISTICIAN, statisticians);

        return usersInAllRoles;
    }



    @ResponseBody
    @PostMapping("/roles/set/submit")
    public String setRolesSubmit(Model model, HttpServletRequest request){
        logger.info("Entered page: /roles/set/submit");
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        int projectId = currentProjectService.getCurrentProjectByUsername(username).getProjectId();

        String[] doctorUserNames = request.getParameterValues("doctor");
        roleInProjectService.setRoleInProject(projectId, doctorUserNames, RoleUtil.ROLE_DOCTOR);

        String[] statisticianUserNames = request.getParameterValues("statistician");
        roleInProjectService.setRoleInProject(projectId, statisticianUserNames, RoleUtil.ROLE_STATISTICIAN);


        //当PM为自己设置或修改了doctor、statistician权限后，要当时生效而不需要重新登录。
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Authentication authWithUpdatedAuthorities = myUserDetailsService.updateAuthentication(auth, projectId);
        SecurityContextHolder.getContext().setAuthentication(authWithUpdatedAuthorities);

        return "角色设置成功";
    }
}

