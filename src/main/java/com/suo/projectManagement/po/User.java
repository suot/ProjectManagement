package com.suo.projectManagement.po;

import java.util.Date;

/**
 * Created by Suo Tian on 2018-04-26.
 */
public class User {
    private int id;
    private String username;
    private String password;
    private String name;
    private String phone;
    private String cellphone;
    private String email;
    private int organizationId;
    private String role;
    private String valid;
    private String activated;
    private Date createTime;
    public User(){

    }
    public User(String username, String password){
        this.username = username;
        this.password = password;
    }

    public User(String username, String name, String phone, String cellphone, String email, int organizationId, String role, String valid, String activated) {
        this.username = username;
        this.name = name;
        this.phone = phone;
        this.cellphone = cellphone;
        this.email = email;
        this.organizationId = organizationId;
        this.role = role;
        this.valid = valid;
        this.activated = activated;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return (username==null)?"":username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return (password==null)?"":password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return (name==null)?"":name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return (username==phone)?"":phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCellphone() {
        return (cellphone==null)?"":cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public String getEmail() {
        return (email==null)?"":email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getOrganizationId() {
        return this.organizationId;
    }

    public void setOrganizationId(int organizationId) {
        this.organizationId = organizationId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getValid() {
        return (valid==null)?"":valid;
    }

    public void setValid(String valid) {
        this.valid = valid;
    }

    public String getActivated() {
        return (activated==null)?"":activated;
    }

    public void setActivated(String activated) {
        this.activated = activated;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", cellphone='" + cellphone + '\'' +
                ", email='" + email + '\'' +
                ", organizationId='" + organizationId + '\'' +
                ", role='" + role + '\'' +
                ", valid='" + valid + '\'' +
                ", activated='" + activated + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
