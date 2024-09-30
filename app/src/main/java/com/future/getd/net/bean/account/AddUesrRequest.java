package com.future.getd.net.bean.account;

public class AddUesrRequest {
    //  "username": "admin",
    //  "password": "123456",
    //  "userAvatarUrl": "https://avatars.githubusercontent.com/u/13444266",
    //  "email": "freui874@qq.com",
    //  "phoneNumber": "18",
    //  "createdTime": "2024-01-27 09:40:41",
    //  "updatedTime": "2025-06-17 17:07:42",
    //  "lastLoginTime": "2024-11-18 00:38:31",
    //  "status": "active",
    //  "gender": "man",
    //  "userType": "domestic"
    String username;
    String password;
    String userAvatarUrl;
    String email;
    String phoneNumber;
    String createdTime;
    String updatedTime;
    String status;
    String gender;
    String userType;

    public AddUesrRequest() {
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

    public String getUserAvatarUrl() {
        return userAvatarUrl;
    }

    public void setUserAvatarUrl(String userAvatarUrl) {
        this.userAvatarUrl = userAvatarUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(String updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
