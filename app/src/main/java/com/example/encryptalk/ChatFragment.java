package com.example.encryptalk;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.encryptalk.adapter.ChatRoomAdapter;
import com.example.encryptalk.adapter.SearchUserRecyclerAdapter;
import com.example.encryptalk.model.OpenChatModel;
import com.example.encryptalk.model.modelMessage;
import com.example.encryptalk.model.UserInfo;
import com.example.encryptalk.utils.FireBaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

public class ChatFragment extends Fragment {

    RecyclerView recyclerView;
    ChatRoomAdapter adapter;
    public ChatFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_chat, container, false);
        recyclerView = view.findViewById(R.id.recyler_view);
        setupRecyclerView();

        return view;
    }
    void setupRecyclerView(){

        Query query = FireBaseUtil.allChatroomCollectionReference()
                .whereArrayContains("userIds",FireBaseUtil.currentUserId())
                .orderBy("lastMessageTimestamp",Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<OpenChatModel> options = new FirestoreRecyclerOptions.Builder<OpenChatModel>()
                .setQuery(query,OpenChatModel.class).build();

        adapter = new ChatRoomAdapter(options,getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    @Override
    public void onStart() {
        super.onStart();
        if (adapter != null) {
            adapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }
}
