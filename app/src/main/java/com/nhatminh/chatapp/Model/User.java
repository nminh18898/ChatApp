package com.nhatminh.chatapp.Model;

import java.util.List;

public class User {
    int uID;
    int email;
    BasicUserInfo basicInfo;
    List<User> friends;

    public User() {
    }

    public User(BasicUserInfo basicInfo) {
        this.basicInfo = basicInfo;
    }

    public BasicUserInfo getBasicInfo() {
        return basicInfo;
    }

    public void setBasicInfo(BasicUserInfo basicInfo) {
        this.basicInfo = basicInfo;
    }
}
