package com.android.prm.service.model.response;

import java.sql.Timestamp;

public class AccountResponse {

    private String id;
    private String username;
    private String password;
    private Timestamp createdDate;
    private String status;
    private String name;
    private String email;
    private String address;
    private String phone;
    private int roleId;
    private int groupId;

    public AccountResponse() {
    }

    public AccountResponse(String id, String username, String password, Timestamp createdDate,
                           String status, String name, String email, String address, String phone,
                           int roleId, int groupId) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.createdDate = createdDate;
        this.status = status;
        this.name = name;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.roleId = roleId;
        this.groupId = groupId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }
}
