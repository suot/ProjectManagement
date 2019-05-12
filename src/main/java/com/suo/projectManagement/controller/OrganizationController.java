package com.suo.projectManagement.controller;

import com.suo.projectManagement.po.Organization;
import com.suo.projectManagement.service.impl.CurrentProjectService;
import com.suo.projectManagement.service.impl.OrganizationService;
import com.suo.projectManagement.service.impl.UserService;
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

import java.util.List;

/**
 * Created by TianSuo on 2018-05-17.
 */


@Controller
public class OrganizationController {
    private static Logger logger = LoggerFactory.getLogger(MainController.class);

    @Autowired
    OrganizationService organizationService;
    @Autowired
    CurrentProjectService currentProjectService;

    //机构查询
    @RequestMapping("/organization/list_all_organizations")
    public String listAllOrganizations(Model model){
        return "/organization :: list_all_organizations";
    }

    @ResponseBody
    @RequestMapping("/organization/list_all_organizations/queryOrganizationList")
    public List<Organization> queryOrganizationList(){
        logger.info("Entered page: /organization/list_all_organizations/queryOrganizationList");

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Organization> organizationList;

        if(UserService.USER_ADMIN == username){
            organizationList = organizationService.getAllOrganizations();
        }else {
            int projectId = currentProjectService.getCurrentProjectByUsername(username).getProjectId();
            organizationList = organizationService.getOrganizationsByProjectId(projectId);
        }

        return organizationList;
    }


    //机构添加
    @RequestMapping("/organization/create")
    public String createOrganization(Model model){
        logger.info("Entered page: /organization/create");
        model.addAttribute("organization", new Organization());
        model.addAttribute("organizationPropertyEnumValues", organizationService.getOrganizationPropertyEnumValues());
        return "/organization :: create_organization";
    }

    @ResponseBody
    @PostMapping("/organization/create/submit")
    public Organization createOrganizationSubmit(Model model, Organization organization){
        logger.info("Entered page: /organization/create/submit");
        int id = organizationService.createOrganization(organization);
        logger.info("Organization creation succeeded");
        organization.setId(id);
        return organization;
    }


    //机构修改
    @RequestMapping("/organization/modify")
    public String modifyOrganization(Model model,
                                @RequestParam int id,
                                @RequestParam String organizationNumber,
                                @RequestParam String name,
                                @RequestParam String address,
                                @RequestParam String contactName,
                                @RequestParam String contactPhone,
                                @RequestParam String contactDepartment,
                                @RequestParam String property){

        logger.info("Entered page: /organization/modify");
        logger.info("organization\'s id is " + id);

        Organization organization = new Organization(id, organizationNumber, name, address, contactName, contactPhone, contactDepartment, property);

        model.addAttribute("organization", organization);
        model.addAttribute("organizationPropertyEnumValues", organizationService.getOrganizationPropertyEnumValues());
        model.addAttribute("updatedOrganization", new Organization());

        return "/organization :: modify_organization";
    }

    @ResponseBody
    @PostMapping("/organization/modify/submit")
    public Organization modifyOrganizationSubmit(Organization updatedOrganization){
        logger.info("Entered page: /organization/modify/submit");
        organizationService.updateOrganization(updatedOrganization);
        logger.info("organization information is modified. Organization\'s id is " + updatedOrganization.getId());
        return updatedOrganization;
    }

}
