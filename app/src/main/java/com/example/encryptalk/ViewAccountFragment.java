package com.example.encryptalk;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;

public class ViewAccountFragment extends Fragment {

    FirebaseFirestore mStore;
    FirebaseAuth mAuth;
    FirebaseUser user;
    ImageView profile;
    TextView username;
    TextView name;
    TextView email;
    ListenerRegistration listener;



    public ViewAccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_account, container, false);
        profile = view.findViewById(R.id.user_icon);

        username = view.findViewById(R.id.view_username);
        name = view.findViewById(R.id.view_name);
        email = view.findViewById(R.id.view_email);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        mStore = FirebaseFirestore.getInstance();

        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        user = mAuth.getCurrentUser();
        if (user != null) {
            loadUserData(user.getUid());
        }
    }

    private void loadUserData(String userId) {
        listener = mStore.collection("users").document(userId)
                .addSnapshotListener((documentSnapshot, e) -> {
                    if (e != null) {
                        return;
                    }
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        // User data exists
                        String getUser = documentSnapshot.getString("Username");
                        String getName = documentSnapshot.getString("Name");
                        String getEmail = documentSnapshot.getString("Email");
                        username.setText(getUser);
                        name.setText(getName);
                        email.setText(getEmail);
                    } else {
                        // Doesn't exist
                        username.setText("No username found");
                        name.setText("No name found");
                        email.setText("No email found");
                    }
                });
    }

    @Override
    public void onStop() {
        super.onStop();
        if (listener != null) {
            listener.remove();
        }
    }
}