package com.example.encryptalk;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.io.*;



public class KeyManager extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        File file = new File("app/src/main/assets/keyDB.txt");
        try
        {
            FileWriter myWriter = new FileWriter(file);
            myWriter.write(1);
            myWriter.close();

        }
        catch(Exception e){

        }
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
