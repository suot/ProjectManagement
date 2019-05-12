package com.suo.projectManagement.controller;

import com.suo.projectManagement.config.SecurityConfig;
import com.suo.projectManagement.po.Organization;
import com.suo.projectManagement.po.User;
import com.suo.projectManagement.service.impl.CurrentProjectService;
import com.suo.projectManagement.service.impl.OrganizationService;
import com.suo.projectManagement.service.impl.ProjectService;
import com.suo.projectManagement.service.impl.UserService;
import com.suo.projectManagement.vo.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by Suo Tian on 2018-05-17.
 */

@Controller
public class UserController {
    private static Logger logger = LoggerFactory.getLogger(MainController.class);
    @Autowired
    UserService userService;
    @Autowired
    ProjectService projectService;
    @Autowired
    MainController mainController;
    @Autowired
    CurrentProjectService currentProjectService;
    @Autowired
    OrganizationService organizationService;

    @RequestMapping("/user/list_all_users")
    public String listAllUsers(Model model){
        return "/user :: list_all_users";
    }

    @ResponseBody
    @RequestMapping("/user/list_all_users/queryUserInfoList")
    public List<UserInfo> queryUserInfoList(){
        logger.info("Entered page: /user/list_all_users/queryUserInfoList");

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<UserInfo> userInfoList;

        if(UserService.USER_ADMIN == username){
            userInfoList = userService.getAllUserInfo();
        }else {
            int projectId = currentProjectService.getCurrentProjectByUsername(username).getProjectId();
            userInfoList = userService.getAllUserInfoByProjectId(projectId);
        }

        return userInfoList;
    }


    @RequestMapping("/user/create")
    public String createUser(Model model){
        logger.info("Entered page: /user/create");
        List<Organization> allOrganizations = organizationService.getAllOrganizations();
        model.addAttribute("allOrganizations", allOrganizations);
        model.addAttribute("roleEnumValues", userService.getRoleEnumValues());
        model.addAttribute("validEnumValues", userService.getValidEnumValues());
        model.addAttribute("activatedEnumValues", userService.getActivatedEnumValues());
        model.addAttribute("activatedStatus", "未激活");

        model.addAttribute("user", new User());

        return "/user :: create_user";
    }

    @ResponseBody
    @PostMapping("/user/create/submit")
    public UserInfo createUserSubmit(Model model, User user) throws Exception{
        logger.info("Entered page: /user/create/submit");
        userService.createUser(user);
        logger.info("User creation succeeded");
        UserInfo userInfo = userService.getUserInfoFromUser(user);
        return userInfo;
    }


    @RequestMapping("/user/modify_admin")
    public String modifyUserAdmin(Model model, @RequestParam String username){
        logger.info("Entered page: /user/modify_admin");

        User user = userService.getUserByUsername(username);
        model.addAttribute("userToBeModified", user);

        List<Organization> allOrganizations = organizationService.getAllOrganizations();
        model.addAttribute("allOrganizations", allOrganizations);
        model.addAttribute("orgId", user.getOrganizationId());

        model.addAttribute("roleEnumValues", userService.getRoleEnumValues());
        model.addAttribute("validEnumValues", userService.getValidEnumValues());
        model.addAttribute("activatedEnumValues", userService.getActivatedEnumValues());

        model.addAttribute("updatedUserByAdmin", new User());

        return "/user :: modify_user_by_admin";
    }

    //表单提交js中用post,表单serialize, controller中参数直接是实体类
    @ResponseBody
    @PostMapping("/user/modify_admin/submit")
    public UserInfo modifyUserAdminSubmit(User updatedUserByAdmin){
        logger.info("Entered page: /user/modify_admin/submit");
        userService.modifyUserByAdmin(updatedUserByAdmin);
        UserInfo userInfo = userService.getUserInfoFromUser(updatedUserByAdmin);
        logger.info("user information is modified by admin. Username is " + updatedUserByAdmin.getUsername());
        return userInfo;
    }


    @RequestMapping("/user/modify")
    public String modifyUser(Model model){
        logger.info("Entered page: /user/modify");
        model.addAttribute("userLoggedIn", userService.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
        model.addAttribute("updatedUser", new User());
        return "/user :: modify_user";
    }

    @PostMapping("/user/modify/submit")
    public String modifyUserSubmit(Model model, User updatedUser){
        logger.info("Entered page: /user/modify/submit");
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        int projectId = currentProjectService.getCurrentProjectByUsername(username).getProjectId();
        updatedUser.setUsername(username);
        userService.modifyUser(updatedUser);

        return mainController.returnBackToMainPage(model, projectId, username);
    }

    @RequestMapping("/user/modify_password")
    public String modifyPassword(Model model){
        logger.info("Entered page: /user/modify_password");
        return "/user :: modify_password";
    }

    //非表单提交，js中用get方法（默认），controller中用@RequestMapping, @RequestParam 来取参数。
    @ResponseBody
    @RequestMapping("/user/modify_password/match")
    public boolean matchOldPassword(Model model, @RequestParam("oldPasswordInput") String oldPasswordInput, @RequestParam("password") String password){
        logger.info("Entered page: /user/modify_password/match");
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User userLoggedIn = userService.getUserByUsername(username);

        boolean matched = SecurityConfig.matchPassword(oldPasswordInput, userLoggedIn.getPassword());
        if(matched){
            //旧密码校验通过，把新密码更新到数据库
            User updatedUser = new User(username, password);
            userService.modifyPassword(updatedUser);
            logger.info("Password is updated successfully");
        }
        return matched;
    }

//    formValidation.remote时使用这个方法
//    @ResponseBody
//    @RequestMapping("/user/modify_password/match")
//    public Map<String, Object> matchOldPassword(@RequestParam("oldPasswordInput") String oldPasswordInput){
//        User userInfoLoggedIn = userInfoService.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
//        boolean matched = SecurityConfig.matchPassword(oldPasswordInput, userInfoLoggedIn.getPassword());
//
//        Map<String, Object> jasonToBeReturned = new HashMap<>();
//        jasonToBeReturned.put("valid", matched);
//        return jasonToBeReturned;
//    }

//    @ResponseBody
//    @RequestMapping("/user/modify_password/submit")
//    public String modifyPasswordSubmit(Model model, @RequestParam("password") String password){
//        logger.info("Entered page: /user/modify_password/submit");
//
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//        User updatedUserInfo = new User(username, password);
//        userInfoService.modifyPassword(updatedUserInfo);
//        logger.info("Password is updated successfully");
//
//        return "密码修改成功";
//    }

    @RequestMapping("/user/reset_password")
    public String resetPassword(Model model){
        logger.info("Entered page: /user/reset_password");
        List<List<User>> userGroupList = userService.getUserByOrganizationGroup();
        model.addAttribute("userGroupList", userGroupList);
        return "/user :: reset_password";
    }

    @ResponseBody
    @PostMapping("/user/reset_password/submit")
    public String resetPasswordSubmit(HttpServletRequest request){
        logger.info("Entered page: /user/reset_password/submit");
        String username = request.getParameter("username");
        User user = new User(username, "123456");
        userService.modifyPassword(user);

        return "密码重置成功";
    }
}

