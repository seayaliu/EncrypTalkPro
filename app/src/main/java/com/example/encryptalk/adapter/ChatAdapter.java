package com.example.encryptalk.adapter;

import static android.content.Intent.getIntent;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.encryptalk.ChatActivity;
import com.example.encryptalk.KeyManager;
import com.example.encryptalk.R;
import com.example.encryptalk.model.UserInfo;
import com.example.encryptalk.model.modelMessage;
import com.example.encryptalk.utils.AndroidUtil;
import com.example.encryptalk.utils.FireBaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import android.widget.RelativeLayout;
import android.widget.LinearLayout;

import java.util.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


public class ChatAdapter extends FirestoreRecyclerAdapter<modelMessage, ChatAdapter.ChatModelViewHolder> {

    Context context;
    String chatroomId;

    public ChatAdapter(@NonNull FirestoreRecyclerOptions<modelMessage> options,Context context, String chatroomId) {
        super(options);
        this.context = context;
        this.chatroomId = chatroomId;
    }
    @Override
    protected void onBindViewHolder(@NonNull ChatModelViewHolder holder, int position, @NonNull modelMessage model) {
        if(model.getSenderId().equals(FireBaseUtil.currentUserId())){
            holder.leftChatLayout.setVisibility(View.GONE);
            holder.rightChatLayout.setVisibility(View.VISIBLE);
            KeyManager k = new KeyManager();
            DocumentReference docRef = FirebaseFirestore.getInstance().collection("keys").document(chatroomId);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String keyString = document.getData().get("Key").toString();
                            byte[] dKey = Base64.getDecoder().decode(keyString);
                            SecretKey key = new SecretKeySpec(dKey, 0, dKey.length, "AES");
                            holder.rightChatTextview.setText(k.decodeMessage(model.getMessage(),key));
                        }
                    }
                }
            });
        }else{
            holder.rightChatLayout.setVisibility(View.GONE);
            holder.leftChatLayout.setVisibility(View.VISIBLE);
            KeyManager k = new KeyManager();
            DocumentReference docRef = FirebaseFirestore.getInstance().collection("keys").document(chatroomId);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String keyString = document.getData().get("Key").toString();
                            byte[] dKey = Base64.getDecoder().decode(keyString);
                            SecretKey key = new SecretKeySpec(dKey, 0, dKey.length, "AES");
                            holder.leftChatTextview.setText(k.decodeMessage(model.getMessage(),key));
                        }
                    }
                }
            });
        }
    }
    @NonNull
    @Override
    public ChatModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.message_recycler,parent,false);
        return new ChatModelViewHolder(view);
    }
    class ChatModelViewHolder extends RecyclerView.ViewHolder{
        LinearLayout leftChatLayout,rightChatLayout;
        TextView leftChatTextview,rightChatTextview;

        public ChatModelViewHolder(@NonNull View itemView) {
            super(itemView);

            leftChatLayout = itemView.findViewById(R.id.left_chat_layout);
            rightChatLayout = itemView.findViewById(R.id.right_chat_layout);
            leftChatTextview = itemView.findViewById(R.id.left_chat_textview);
            rightChatTextview = itemView.findViewById(R.id.right_chat_textview);
        }
    }
}