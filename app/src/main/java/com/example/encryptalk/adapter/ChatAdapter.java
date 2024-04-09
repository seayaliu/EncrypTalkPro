package com.example.encryptalk.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.encryptalk.ChatActivity;
import com.example.encryptalk.R;
import com.example.encryptalk.model.modelMessage;
import com.example.encryptalk.utils.AndroidUtil;
import com.example.encryptalk.utils.FireBaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Firebase;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import android.widget.RelativeLayout;
import android.widget.LinearLayout;

import org.w3c.dom.Document;


public class ChatAdapter extends FirestoreRecyclerAdapter<modelMessage, ChatAdapter.ChatModelViewHolder> {

    Context context;
    FirebaseFirestore mStore;

    public ChatAdapter(@NonNull FirestoreRecyclerOptions<modelMessage> options,Context context) {
        super(options);
        this.context = context;
        mStore = FirebaseFirestore.getInstance();
    }
    @Override
    protected void onBindViewHolder(@NonNull ChatModelViewHolder holder, int position, @NonNull modelMessage model) {
        if (model.isSelfDestruct()) {
            Timestamp messageSent = model.getTimestamp();
            long currentTimeMS = System.currentTimeMillis();
            long msgTimeMS = messageSent.toDate().getTime();
            long timeDiffMS = currentTimeMS - msgTimeMS;
            long timeDiffS = timeDiffMS / 1000;

            if (timeDiffS >= 30) {
                deleteMessage(getSnapshots().getSnapshot(position));
            }
        }

        Timestamp timestamp = model.getTimestamp();
        if(model.getSenderId().equals(FireBaseUtil.currentUserId())){
            holder.leftChatLayout.setVisibility(View.GONE);
            holder.rightChatLayout.setVisibility(View.VISIBLE);
            holder.rightChatTextview.setText(model.getMessage());
        }else{
            holder.rightChatLayout.setVisibility(View.GONE);
            holder.leftChatLayout.setVisibility(View.VISIBLE);
            holder.leftChatTextview.setText(model.getMessage());
        }
    }

    private void deleteMessage(DocumentSnapshot snapshot) {
        snapshot.getReference().delete();
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