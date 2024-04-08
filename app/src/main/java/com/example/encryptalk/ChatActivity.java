package com.example.encryptalk;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

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


import java.util.Arrays;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat);

        otherUser = AndroidUtil.getUserModelFromIntent(getIntent());
        chatroomId = FireBaseUtil.getChatroomId(FireBaseUtil.currentUserId(), otherUser.getUserId());

        messageInput = findViewById(R.id.chat_message_input);
        sendMessageButton = findViewById(R.id.send_message_button);
        backButton = findViewById(R.id.back_btn);
        otherUsername = findViewById(R.id.other_username);
        recyclerView = findViewById(R.id.recycler_view);

        backButton.setOnClickListener((v) -> {
            onBackPressed();
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

        adapter = new ChatAdapter(options,getApplicationContext());
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

        FireBaseUtil.getChatroomReference(chatroomId).set(modelMessage);

        modelMessage modelMessage = new modelMessage(message,FireBaseUtil.currentUserId(),Timestamp.now());
        FireBaseUtil.getChatroomMessageReference(chatroomId).add(modelMessage)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if(task.isSuccessful()){
                            messageInput.setText("");

                        }
                    }
                });
    }

    void getOrCreateOpenChatModel() {
        FireBaseUtil.getChatroomReference(chatroomId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                modelMessage = task.getResult().toObject(OpenChatModel.class);
                if (modelMessage ==null) {
                    modelMessage = new OpenChatModel(
                            chatroomId,
                            Arrays.asList(FireBaseUtil.currentUserId(), otherUser.getUserId()),
                            Timestamp.now(),
                            ""
                    );
                    FireBaseUtil.getChatroomReference(chatroomId).set(modelMessage);
                    KeyManager.createKey();
                }
            }
        });
    }
}