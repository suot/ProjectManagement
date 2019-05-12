package com.suo.projectManagement;

import com.suo.projectManagement.aop.PrintServiceName;
import com.suo.projectManagement.controller.MainController;
import com.suo.projectManagement.po.OperationRecord;
import com.suo.projectManagement.service.impl.CurrentProjectService;
import com.suo.projectManagement.service.impl.OperationRecordService;
import com.suo.projectManagement.service.impl.RoleInProjectService;
import com.suo.projectManagement.service.impl.UserService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;


/**
 * Created by Suo Tian on 2018-05-24.
 */
@Aspect
@Component
public class LogAspect {
    private static Logger logger = LoggerFactory.getLogger(MainController.class);

    @Autowired
    OperationRecordService operationRecordService;

    @Autowired
    RoleInProjectService roleInProjectService;

    @Autowired
    UserService userService;

    @Autowired
    CurrentProjectService currentProjectService;

    @After("within(com.suo.projectManagement..*) && @annotation(printServiceName)")
    public void afterService(JoinPoint joinPoint, PrintServiceName printServiceName) throws Throwable {
        // 接收到请求，记录请求内容
        String who = SecurityContextHolder.getContext().getAuthentication().getName();
        if (UserService.USER_ADMIN != who) {
            int projectId = currentProjectService.getCurrentProjectByUsername(who).getProjectId();

            String[] description = printServiceName.description().split("&&");
            String name = userService.getUserByUsername(who).getName();
            String role = roleInProjectService.getRoles(projectId, who);

            OperationRecord operationRecord = new OperationRecord(projectId, who, name, role, description[0], description[1]);
            operationRecordService.insertOperationRecord(operationRecord);

            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            logger.info(role + ":" + who + "从IP: " + request.getRemoteAddr() + "访问了URL: " + request.getRequestURL().toString() + "，调用了方法: " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName() + ", 参数列表: " + Arrays.toString(joinPoint.getArgs()));
        }
    }
}
