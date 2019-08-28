package com.nhatminh.chatapp.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nhatminh.chatapp.Adapter.MessageListAdapter;
import com.nhatminh.chatapp.CompleteFlag;
import com.nhatminh.chatapp.GlobalConstants;
import com.nhatminh.chatapp.Model.BasicUserInfo;
import com.nhatminh.chatapp.Model.ConversationMetaInfo;
import com.nhatminh.chatapp.Model.UserMessage;
import com.nhatminh.chatapp.R;

import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    // conversation id from previous activity
    String conversationId;

    //variables use for store data load from database
    HashMap<String, String> members = new HashMap<>();
    CompleteFlag isCompleteLoadingData = new CompleteFlag();
    ArrayList<UserMessage> messageList = new ArrayList<>();

    // variables use for display message
    RecyclerView messageRecycler;
    MessageListAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        conversationId = getIntent().getExtras().getString(GlobalConstants.CONVERSATION_ID);

        loadConversationMemberIdAndName();
        loadConversationMessages();

    }



    void loadConversationMemberIdAndName() {

        isCompleteLoadingData.addFlag(false);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("members").child(conversationId);


        ChildEventListener membersListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                members.put(dataSnapshot.getKey(),dataSnapshot.getValue().toString());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        ref.addChildEventListener(membersListener);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                isCompleteLoadingData.setOneFlagComplete();
                loadComplete();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }

    void loadConversationMessages(){
        isCompleteLoadingData.addFlag(false);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("conversation").child(conversationId);

        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                UserMessage userMessage = dataSnapshot.getValue(UserMessage.class);
                messageList.add(userMessage);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                isCompleteLoadingData.setOneFlagComplete();
                loadComplete();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void loadComplete(){
        if (isCompleteLoadingData.isAllFlagComplete())
        {



            messageRecycler = (RecyclerView) findViewById(R.id.reyclerview_message_list);
            messageAdapter = new MessageListAdapter(this, messageList,
                    FirebaseAuth.getInstance().getCurrentUser().getUid(), members);
            messageRecycler.setLayoutManager(new LinearLayoutManager(this));
            messageRecycler.setAdapter(messageAdapter);

        }

    }




}
