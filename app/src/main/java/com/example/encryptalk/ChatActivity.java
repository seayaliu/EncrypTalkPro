package com.example.encryptalk;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.encryptalk.adapter.ChatAdapter;
import com.example.encryptalk.adapter.SearchUserRecyclerAdapter;
import com.example.encryptalk.model.OpenChatModel;
import com.example.encryptalk.model.UserInfo;
import com.example.encryptalk.model.modelMessage;
import com.example.encryptalk.utils.AndroidUtil;
import com.example.encryptalk.utils.FireBaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    UserInfo otherUser;
    String chatroomId;
    OpenChatModel modelMessage;
    EditText messageInput;
    ImageButton sendMessageButton;
    ImageButton backButton;
    TextView otherUsername;
    RecyclerView recyclerView;
    ChatAdapter adapter;
    Boolean selfDestruct;
    ToggleButton sd_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat);

        otherUser = AndroidUtil.getUserModelFromIntent(getIntent());
        otherUsername = findViewById(R.id.other_username);
        otherUsername.setText(otherUser.getUsername());


        chatroomId = FireBaseUtil.getChatroomId(FireBaseUtil.currentUserId(), otherUser.getUserId());


        messageInput = findViewById(R.id.chat_message_input);
        sendMessageButton = findViewById(R.id.send_message_button);
        backButton = findViewById(R.id.back_btn);
        otherUsername = findViewById(R.id.other_username);
        recyclerView = findViewById(R.id.recycler_view);
        sd_btn = findViewById(R.id.sd_btn);
        selfDestruct = sd_btn.isChecked();


        backButton.setOnClickListener((v) -> {
            onBackPressed();
        });

        sd_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean checked) {
                if (checked) {
                    Toast.makeText(ChatActivity.this, "Self-Destruct ON", Toast.LENGTH_SHORT).show();
                    selfDestruct = true;
                } else {
                    Toast.makeText(ChatActivity.this, "Self-Destruct OFF", Toast.LENGTH_SHORT).show();
                    selfDestruct = false;
                }
            }
        });

        sendMessageButton.setOnClickListener((v -> {
            String message = messageInput.getText().toString().trim();
            if (message.isEmpty())
                return;
            sendMessage(message);
        }));

        getOrCreateOpenChatModel();
        setChatView();

    }

    void setChatView(){
        Query query = FireBaseUtil.getChatroomMessageReference(chatroomId)
                .orderBy("timestamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<modelMessage> options = new FirestoreRecyclerOptions.Builder<modelMessage>()
                .setQuery(query,modelMessage.class).build();

        adapter = new ChatAdapter(options,getApplicationContext(), chatroomId);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(true);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }
    void sendMessage(String message){

        modelMessage.setTimestamp(Timestamp.now());
        modelMessage.setLastMsgSenderId(FireBaseUtil.currentUserId());

        modelMessage.setLastMessage(message);
        modelMessage.setSelfDestruct(selfDestruct);
        FireBaseUtil.getChatroomReference(chatroomId).set(modelMessage);
//        modelMessage modelMessage = new modelMessage(message,FireBaseUtil.currentUserId(),Timestamp.now());
//        FireBaseUtil.getChatroomMessageReference(chatroomId).add(modelMessage)
//                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentReference> task) {
//                        if(task.isSuccessful()){
                            messageInput.setText("");
//
//                        }
//                    }
//                });
        KeyManager.encodeMessage(message,chatroomId, selfDestruct);
    }

    void getOrCreateOpenChatModel() {
        FireBaseUtil.getChatroomReference(chatroomId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                modelMessage = task.getResult().toObject(OpenChatModel.class);
                if (modelMessage ==null) {
                    ArrayList<String> list = new ArrayList<String>();
                    if(FireBaseUtil.currentUserId().hashCode()<otherUser.getUserId().hashCode()) {
                        list.add(FireBaseUtil.currentUserId());
                        list.add(otherUser.getUserId());
                    }
                    else{
                        list.add(otherUser.getUserId());
                        list.add(FireBaseUtil.currentUserId());
                    }
                    modelMessage = new OpenChatModel(
                            chatroomId,
                            list,
                            Timestamp.now(),
                            "",
                            selfDestruct
                    );
                    FireBaseUtil.getChatroomReference(chatroomId).set(modelMessage);
                    KeyManager.createKey();
                }
            }
        });
    }
}