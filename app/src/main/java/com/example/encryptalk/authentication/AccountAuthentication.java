package com.example.encryptalk.authentication;

import android.content.Intent;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AccountAuthentication implements Authentication{

    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    public void authenticate() {
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
    }

}
