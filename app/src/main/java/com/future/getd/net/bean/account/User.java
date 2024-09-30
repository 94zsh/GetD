package com.future.getd.net.bean.account;

public class User {
//    {
//	"code": 200,
//	"message": "成功",
//	"data": {
//		"id": 1,
//		"username": "532253827@qq.com",
//		"password": "123456",
//		"userAvatarUrl": null,
//		"email": "532253827@qq.com",
//		"phoneNumber": null,
//		"createdTime": "2024-09-29 08:43:26",
//		"updatedTime": "2024-09-29 08:43:26",
//		"lastLoginTime": null,
//		"status": "active",
//		"gender": null,
//		"userType": "international"
//	}
//    }
    int id;
    String username = "";
    String password = "";
    String userAvatarUrl = "";
    String email = "";
    String phoneNumber = "";
    String createdTime = "";
    String updatedTime = "";
    String lastLoginTime = "";
    String status = "";
    String gender = "";
    String userType = "";

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
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

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", userAvatarUrl='" + userAvatarUrl + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", createdTime='" + createdTime + '\'' +
                ", updatedTime='" + updatedTime + '\'' +
                ", lastLoginTime='" + lastLoginTime + '\'' +
                ", status='" + status + '\'' +
                ", gender='" + gender + '\'' +
                ", userType='" + userType + '\'' +
                '}';
    }
}
