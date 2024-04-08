package com.example.encryptalk.model;

import com.google.firebase.Timestamp;


public class UserInfo {

    private String Email;

    private String Name;
    private String Username;

    private Timestamp createdTimestamp;
    private String UserId;
    private String fcmToken;

    public UserInfo() {
    }

    public UserInfo(String email, String name, String username, String userId) {
        this.Email = email;
        this.Name = name;
        this.Username = username;
        this.createdTimestamp = createdTimestamp;
        this.UserId = userId;
    }

    public String getEmail() { return Email; }

    public void setEmail(String email) { this.Email = email; }

    public String getName() { return Name; }

    public void setName(String name) { this.Name = name; }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        this.Username = username;
    }

   public Timestamp getCreatedTimestamp() {
       return createdTimestamp;
   }

   public void setCreatedTimestamp(Timestamp createdTimestamp) {
       this.createdTimestamp = createdTimestamp;
   }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        this.UserId = userId;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
}

