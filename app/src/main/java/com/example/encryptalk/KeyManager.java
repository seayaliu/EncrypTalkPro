package com.example.encryptalk;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.encryptalk.model.modelMessage;
import com.example.encryptalk.utils.FireBaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


public class KeyManager extends AppCompatActivity {
    String decrypted;

    protected static void createKey() {
        SecretKey key;
        FirebaseFirestore mStore;

        mStore = FirebaseFirestore.getInstance();
        try {
            key = KeyGenerator.getInstance("AES").generateKey();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        //https://firebase.google.com/docs/firestore/query-data/order-limit-data
        Query time = mStore.collection("chatrooms").orderBy("timestamp", Query.Direction.DESCENDING).limit(1);
        time.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QueryDocumentSnapshot document = task.getResult().iterator().next();
                    String users = document.getData().get("userIds").toString();
                    String user2 = users.substring(1,users.indexOf(","));
                    String user1 = users.substring(users.indexOf(",") + 2, users.indexOf("]"));
                    String userId = user1+"_"+user2;
                    DocumentReference docRef = mStore.collection("keys").document(userId);
                    Map<String, Object> keys = new HashMap<>();
                    keys.put("User 1", user1);
                    keys.put("User 2", user2);
                    keys.put("Key", Base64.getEncoder().encodeToString(key.getEncoded()));
                    docRef.set(keys);
                }
            }
        });





    }


    public static void encodeMessage(String message, String chatroomId) {
        FirebaseFirestore mStore;
        mStore = FirebaseFirestore.getInstance();
        DocumentReference docRef = mStore.collection("keys").document(chatroomId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String keyString = document.getData().get("Key").toString();
                        byte[] dKey = Base64.getDecoder().decode(keyString);
                        SecretKey key = new SecretKeySpec(dKey, 0, dKey.length, "AES");
                        byte[] bytes = message.getBytes();
                        Cipher cipher;
                        try {
                            cipher = Cipher.getInstance("AES");
                        } catch (NoSuchAlgorithmException e) {
                            throw new RuntimeException(e);
                        } catch (NoSuchPaddingException e) {
                            throw new RuntimeException(e);
                        }

                        try {
                            cipher.init(Cipher.ENCRYPT_MODE, key);
                        } catch (InvalidKeyException e) {
                            throw new RuntimeException(e);
                        }
                        byte[] encryptedBytes;
                        try {
                            encryptedBytes = cipher.doFinal(bytes);
                        } catch (BadPaddingException e) {
                            throw new RuntimeException(e);
                        } catch (IllegalBlockSizeException e) {
                            throw new RuntimeException(e);
                        }

                        String encrypted = Base64.getEncoder().encodeToString(encryptedBytes);

                        modelMessage modelMessage = new modelMessage(encrypted, FireBaseUtil.currentUserId(), Timestamp.now());
                        FireBaseUtil.getChatroomMessageReference(chatroomId).add(modelMessage);
                        Log.d("key", "Message: "+ message);
                        Log.d("key", "Encrypted: "+ encrypted);
                    }
                }
            }
        });
    }
    public String decodeMessage(String message, SecretKey key) {
        byte[] encryptedBytes = Base64.getDecoder().decode(message);
        Log.d("testtt", message);
        Log.d("testtt", encryptedBytes.toString());
        Cipher cipher;
        try {
            cipher = Cipher.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        }

        try {
            cipher.init(Cipher.DECRYPT_MODE, key);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
        byte[] decryptedBytes = new byte[2];
        try {
            decryptedBytes = cipher.doFinal(encryptedBytes);
        } catch (Exception e){

        }

        decrypted = new String(decryptedBytes);
        Log.d("testt", "Decrypted: "+ decrypted);
        return decrypted;
    }
//    void retrieveKeys(){
//
//    }
//    void updateKeys(){
//
//    }
}
