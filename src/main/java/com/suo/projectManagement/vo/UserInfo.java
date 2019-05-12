package com.suo.projectManagement.vo;

import com.suo.projectManagement.po.Organization;
import com.suo.projectManagement.po.User;

/**
 * Created by TianSuo on 2018-07-09.
 */
public class UserInfo {
    private User user;
    private Organization organization;

    public UserInfo() {
    }

    public UserInfo(User user, Organization organization) {
        this.user = user;
        this.organization = organization;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "user=" + user +
                ", organization=" + organization +
                '}';
    }
}
