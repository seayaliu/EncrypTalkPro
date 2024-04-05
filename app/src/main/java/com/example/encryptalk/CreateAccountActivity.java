package com.example.encryptalk;

import android.accounts.Account;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.encryptalk.authentication.AccountAuthentication;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

// The following code is inspired by https://www.youtube.com/watch?v=QAKq8UBv4GI&ab_channel=CodesEasy
public class CreateAccountActivity extends AppCompatActivity {

    public static final String TAG = "TAG";

    EditText nameInput;
    EditText usernameInput;
    EditText emailInput;
    EditText passwordInput;
    Button accountBtn;
    FirebaseAuth mAuth;
    FirebaseFirestore mStore;
    ProgressBar progressBar;
    TextView switchLogin;

    String userId;


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_account);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();
        nameInput = findViewById(R.id.full_name);
        usernameInput = findViewById(R.id.username);
        emailInput = findViewById(R.id.email);
        passwordInput = findViewById(R.id.password);
        accountBtn = findViewById(R.id.create_acc_btn);
        progressBar = findViewById(R.id.progressBar);
        switchLogin = findViewById(R.id.loginNow);

        switchLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Following code is largely inspired by: https://www.youtube.com/watch?v=QAKq8UBv4GI&ab_channel=CodesEasy
        accountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String email, password, username, name;
                email = String.valueOf(emailInput.getText());
                password = String.valueOf(passwordInput.getText());
                username = String.valueOf(usernameInput.getText());
                name = String.valueOf(nameInput.getText());

                // might delete create username so not checking if there's text rn

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(CreateAccountActivity.this, "Enter Email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(CreateAccountActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                // The following method is modified from the Firebase Authentication Documentation: https://firebase.google.com/docs/auth/android/password-auth?_gl=1*ax5d2t*_up*MQ..*_ga*MTk0NDg3NjEzMS4xNzEyMjYwNjEx*_ga_CW55HF8NVT*MTcxMjI2MDYxMS4xLjAuMTcxMjI2MDYxMS4wLjAuMA..#java_3
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    Toast.makeText(CreateAccountActivity.this, "Account created.",
                                            Toast.LENGTH_SHORT).show();
                                    userId = mAuth.getCurrentUser().getUid();
                                    DocumentReference docRef = mStore.collection("users").document(userId);
                                    Map<String, Object> user = new HashMap<>();
                                    user.put("Username", username);
                                    user.put("Name", name);
                                    user.put("Email", email);
                                    docRef.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Log.d(TAG, "onSuccess: user profile has been created for "+ userId);
                                        }
                                    });
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(CreateAccountActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}