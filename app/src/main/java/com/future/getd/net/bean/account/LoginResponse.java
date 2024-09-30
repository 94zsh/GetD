package com.future.getd.net.bean.account;

public class LoginResponse {
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

    int code;
    String message;
    User data;

    public LoginResponse() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getData() {
        return data;
    }

    public void setData(User data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
