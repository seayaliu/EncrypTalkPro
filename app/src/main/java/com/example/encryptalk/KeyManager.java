package com.example.encryptalk;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;


public class KeyManager extends AppCompatActivity {

    String userId;
    String user1;
    String user2;
    SecretKey key;
    FirebaseAuth mAuth;
    FirebaseFirestore mStore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user1 = "E8sztTbulydiLEio4UPnfhn73pw1";
        user2 = "OL6Uj25LodNOpEmR2riNZEPlJQo1";
        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();

        try {
            key = KeyGenerator.getInstance("DES").generateKey();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        userId = user1+user2;
        DocumentReference docRef = mStore.collection("keys").document(userId);
        Map<String, Object> keys = new HashMap<>();
        keys.put("User 1", user1);
        keys.put("User 2", user2);
        keys.put("Key", Base64.getEncoder().encodeToString(key.getEncoded()));
        docRef.set(keys);
    }


//    void generateKeys(String user1, String user2) {
//
//
//    }
//    void retrieveKeys(){
//
//    }
//    void updateKeys(){
//
//    }
}
