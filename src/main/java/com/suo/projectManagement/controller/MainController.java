package com.suo.projectManagement.controller;

import com.suo.projectManagement.po.*;
import com.suo.projectManagement.service.IGenerateRandomService;
import com.suo.projectManagement.service.IOfferNumberService;
import com.suo.projectManagement.service.impl.*;
import com.suo.projectManagement.vo.ProjectInfo;
import com.suo.projectManagement.vo.echarts.EChartsMap;
import com.suo.projectManagement.vo.echarts.StatisticalChart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Suo Tian on 2018/5/3.
 */

@Controller
public class MainController {
    @Autowired
    UserService userService;

    @Autowired
    OperationRecordService operationRecordService;

    @Autowired
    RoleInProjectService roleInProjectService;

    @Autowired
    ProjectService projectService;

    @Autowired
    CurrentProjectService currentProjectService;

    @Autowired
    IOfferNumberService offerNumberService;

    @Autowired
    IGenerateRandomService generateRandomService;



    private static Logger logger = LoggerFactory.getLogger(MainController.class);

    @RequestMapping(value = {"/login", "/"})
    public String login() {
        return "/login";
    }

    @GetMapping("/readme")
    public String readme(){
        return "readme";
    }

    @RequestMapping("/mainpage")
    public String entrance(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if(UserService.USER_ADMIN == username){
            return returnBackToMainPageByAdmin(model);
        }else {
            CurrentProject currentProject = currentProjectService.getCurrentProjectByUsername(username);
            int projectId = currentProject.getProjectId();
            String role = roleInProjectService.getRoles(projectId, username);

            //更新OperationRecord表，记录：登录成功
            OperationRecord operationRecord = new OperationRecord(projectId, username, userService.getUserByUsername(username).getName(), role, "登录", "登录成功");
            operationRecordService.insertOperationRecord(operationRecord);
            logger.info("User logged in: " + username);

            return returnBackToMainPage(model, projectId, username);
        }
    }

    @RequestMapping("/return_back_to_mainpage")
    public String returnToMainpage(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if(UserService.USER_ADMIN == username){
            return returnBackToMainPageByAdmin(model);
        }else {
            int projectId = currentProjectService.getCurrentProjectByUsername(username).getProjectId();
            return returnBackToMainPage(model, projectId, username);
        }
    }


    public String returnBackToMainPage(Model model, int projectId, String username){

        User userLoggedIn = userService.getUserByUsername(username);
        model.addAttribute("userLoggedIn", userLoggedIn);

        String role = roleInProjectService.getRoles(projectId, username);
        model.addAttribute("role", role);

        //主页显示已取药、试验总数、试验进行天数
        int randomNumberUsed = offerNumberService.getCountOfRandomNumberUsed(projectId);
        RandomSettings randomSettings = generateRandomService.findRandomSettings(projectId);
        int sampleSize;
        if(randomSettings == null){
            sampleSize = 0;
        }else {
            sampleSize = randomSettings.getSimpleSize();
        }

        int interval = offerNumberService.getTrailDuration(projectId);
        model.addAttribute("randomNumberUsed", randomNumberUsed);
        model.addAttribute("sampleSize", sampleSize);
        model.addAttribute("interval", interval);

        //右边栏显示项目信息
        Project project = projectService.getProjectById(projectId);
        ProjectInfo projectInfo = projectService.getProjectInfoFromProject(project);
        model.addAttribute("projectInfo", projectInfo);

        List<OperationRecord> operationRecords = operationRecordService.getOperationRecordsByProjectId(projectId);
        model.addAttribute("operationRecords", operationRecords);

        return "/mainpage";
    }

    public String returnBackToMainPageByAdmin(Model model){
//        User userLoggedIn = new User(UserService.USER_ADMIN, UserService.PASSWORD_ADMIN);
//        userLoggedIn.setName("管理员");
//        model.addAttribute("userLoggedIn", userLoggedIn);

        return "/mainpage_admin";
    }

    @RequestMapping("/mainpage/getEChartsData")
    @ResponseBody
    public List<StatisticalChart> getEChartsData() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        CurrentProject currentProject = currentProjectService.getCurrentProjectByUsername(username);
        int projectId = currentProject.getProjectId();

        List<StatisticalChart> list = offerNumberService.getOfferNumberChart(projectId);
        if(list == null || list.size()==0){
            return null;
        }else {
            return list;
        }
    }

    @RequestMapping("/mainpage/getEChartsOptions")
    @ResponseBody
    public EChartsMap getEChartsOptions() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        CurrentProject currentProject = currentProjectService.getCurrentProjectByUsername(username);
        int projectId = currentProject.getProjectId();

        EChartsMap map = offerNumberService.getOptionsForEChart(projectId);
        return map;
    }

    @RequestMapping(value = {"/readme"})
    public String manual() {
        return "/readme";
    }

}



