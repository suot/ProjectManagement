package com.suo.projectManagement.po;

/**
 * Created by Suo Tian on 2018-05-16.
 */
public class Organization {
    private int id;
    private String organizationNumber;
    private String name;
    private String address;
    private String contactName;
    private String contactPhone;
    private String contactDepartment;
    private String property;

    public Organization(){

    }

    public Organization(int id, String organizationNumber, String name, String address, String contactName, String contactPhone, String contactDepartment, String property) {
        this.id = id;
        this.organizationNumber = organizationNumber;
        this.name = name;
        this.address = address;
        this.contactName = contactName;
        this.contactPhone = contactPhone;
        this.contactDepartment = contactDepartment;
        this.property = property;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrganizationNumber() {
        return organizationNumber;
    }

    public void setOrganizationNumber(String organizationNumber) {
        this.organizationNumber = organizationNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactDepartment() {
        return contactDepartment;
    }

    public void setContactDepartment(String contactDepartment) {
        this.contactDepartment = contactDepartment;
    }

    @Override
    public String toString() {
        return "Organization{" +
                "id=" + id +
                ", organizationNumber='" + organizationNumber + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", contactName='" + contactName + '\'' +
                ", contactPhone='" + contactPhone + '\'' +
                ", contactDepartment='" + contactDepartment + '\'' +
                ", property='" + property + '\'' +
                '}';
    }
}
