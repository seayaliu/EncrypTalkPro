package com.example.encryptalk.model;
import com.google.firebase.Timestamp;

public class modelMessage {
    private String message;
    private String senderId;
    private Timestamp timestamp;
    private boolean selfDestruct;

    public modelMessage() {
    }

    public modelMessage(String message, String senderId, Timestamp timestamp, boolean selfDestruct) {
        this.message = message;
        this.senderId = senderId;
        this.timestamp = timestamp;
        this.selfDestruct = selfDestruct;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isSelfDestruct() {
        return selfDestruct;
    }

    public void setSelfDestruct(boolean selfDestruct) {
        this.selfDestruct = selfDestruct;
    }

}