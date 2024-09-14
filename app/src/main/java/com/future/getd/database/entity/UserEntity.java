package com.future.getd.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class UserEntity {
    @PrimaryKey(autoGenerate = true)
    public int userId;
    public String userName;
    public String password;
    private String userAccount="";//用户名（手机号或者邮箱）
    private String nickName = "";//名
    private int sex = 0;//性别
    private String birthday = "2000-1-1";//生日 e.g:1991-02-25
    private int height = 170;//身高（对应cm）
    private int weight = 50;//体重（对应kg）

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
