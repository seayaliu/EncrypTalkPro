package com.example.encryptalk.model;

import com.google.firebase.Timestamp;

import java.util.List;

public class OpenChatModel {
    String chatroomId;
    List<String> userIds; // to store user id on either end
    Timestamp timestamp;
    String lastMsgSenderId;
    String lastMessage;

    Boolean selfDestruct;


    public OpenChatModel() {
    }



    public OpenChatModel(String chatroomId, List<String> userIds, Timestamp timestamp, String lastMsgSenderId, Boolean selfDestruct) {
        this.chatroomId = chatroomId;
        this.userIds = userIds;
        this.timestamp = timestamp;
        this.lastMsgSenderId = lastMsgSenderId;
        this.selfDestruct = selfDestruct;
    }

    public String getChatroomId() {
        return chatroomId;
    }

    public void setChatroomId(String chatroomId) {
        this.chatroomId = chatroomId;
    }

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getLastMsgSenderId() {
        return lastMsgSenderId;
    }

    public void setLastMsgSenderId(String lastMsgSenderId) {
        this.lastMsgSenderId = lastMsgSenderId;

    }

    public Boolean getSelfDestruct() {
        return selfDestruct;
    }

    public void setSelfDestruct(Boolean selfDestruct) {
        this.selfDestruct = selfDestruct;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
}
