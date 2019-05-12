package com.suo.projectManagement.service.impl;


import com.suo.projectManagement.dao.IRoleInProjectRepo;
import com.suo.projectManagement.dao.IUserRepo;
import com.suo.projectManagement.po.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TianSuo on 2018-05-02.
 */

@Service
public class MyUserDetailsService implements UserDetailsService {
    //Spring Security提供了一个AuthenticationProvider 的简单实现DaoAuthenticationProvider，这也是框架最早支持的provider。它使用了一个UserDetailsService 来查询用户名、密码和GrantedAuthority 。其简单的通过比较封装了用户的密码信息的UsernamePasswordAuthenticationToken和通过UserDetailsService查询出来的用户的密码是否相同来验证用户。对于这个provider的配置非常简单。UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;返回的UserDetails 也是一个接口，其只提供了getters方法来保证用户的验证信息例如用户名、密码和被授予的权利等不会因为用户账号的禁用或者启用而被置为空。大部分验证提供者都会使用UserDetailsService，即使username和password在验证过程中不需要使用到。他们也可能只使用UserDetails 对象中GrantedAuthority 信息。因为一些其他的系统(例如 LDAP or X.509 or CAS)接管了验证用户信息的责任。

    @Autowired
    IUserRepo IUserRepo;

    @Autowired
    IRoleInProjectRepo IRoleInProjectRepo;

    @Autowired CurrentProjectService currentProjectService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, InternalAuthenticationServiceException {
        String password;
        Boolean valid;
        List<String> roles;

        if(UserService.USER_ADMIN == username){
            password = "admin";
            valid = true;
            roles = new ArrayList<>();
            roles.add("admin");
        }else {
            User user = IUserRepo.getUserByUsername(username);
            if (null == user) {
                throw new UsernameNotFoundException("该用户不存在");
            }
            password = user.getPassword();
            String activated = user.getActivated();
            if(activated.equals("已激活")){
                valid = true;
            }else {
                valid = false;
                throw new InternalAuthenticationServiceException("该用户未激活");
            }

            int projectId = currentProjectService.getCurrentProjectByUsername(username).getProjectId();
            roles = IRoleInProjectRepo.getRolesInProject(projectId, username);
        }
        return new org.springframework.security.core.userdetails.User(username, password, valid, true, true, true, this.getGrantedAuthority(roles));
    }

    private List<GrantedAuthority> getGrantedAuthority(List<String> rolesInProject){
        List<GrantedAuthority> roles = new ArrayList<>();
        for (String role : rolesInProject) {
            roles.add(new SimpleGrantedAuthority(role));
        }
        return roles;
    }

    public Authentication updateAuthentication(Authentication auth, int projectId){
        List<String> rolesInProject = IRoleInProjectRepo.getRolesInProject(projectId, auth.getName());
        return new UsernamePasswordAuthenticationToken(auth.getName(), auth.getCredentials(), this.getGrantedAuthority(rolesInProject));
    }
}
