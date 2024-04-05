package com.example.encryptalk.model;

import com.google.firebase.Timestamp;


public class UserInfo {
    private String username;
    private Timestamp createdTimestamp;
    private String userId;
    private String fcmToken;

    public UserInfo() {
    }

    public UserInfo(String phone, String username, Timestamp createdTimestamp,String userId) {
        this.username = username;
        this.createdTimestamp = createdTimestamp;
        this.userId = userId;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Timestamp getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(Timestamp createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
}

