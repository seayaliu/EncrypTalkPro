package com.example.encryptalk.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.encryptalk.model.UserInfo;
import com.google.firebase.firestore.auth.User;

public class AndroidUtil {

    public static  void showToast(Context context,String message){
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }

    public static void passUserModelAsIntent(Intent intent, UserInfo model){
        intent.putExtra("Email",model.getEmail());
        intent.putExtra("Name",model.getName());
        intent.putExtra("Username",model.getUsername());
        intent.putExtra("UserId", model.getUserId());
        intent.putExtra("fcmToken",model.getFcmToken());
    }

    public static UserInfo getUserModelFromIntent(Intent intent){
        UserInfo userModel = new UserInfo();
        userModel.setEmail(intent.getStringExtra("Email"));
        userModel.setName(intent.getStringExtra("Name"));
        userModel.setUsername(intent.getStringExtra("Username"));
        userModel.setUserId(intent.getStringExtra("UserId"));
        userModel.setFcmToken(intent.getStringExtra("fcmToken"));
        return userModel;
    }

    public static void setProfilePic(Context context, Uri imageUri, ImageView imageView){
        Glide.with(context).load(imageUri).apply(RequestOptions.circleCropTransform()).into(imageView);
    }
}