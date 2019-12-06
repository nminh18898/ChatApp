package com.nhatminh.chatapp.conversation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nhatminh.chatapp.Utils.CompleteFlag;
import com.nhatminh.chatapp.Utils.DateManipulation;
import com.nhatminh.chatapp.Utils.GlobalConstants;
import com.nhatminh.chatapp.R;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {

    // conversation id and title from previous activity
    String conversationId;
    String conversationTitle;

    //variables use for store data load from database
    HashMap<String, String> members = new HashMap<>();
    CompleteFlag isCompleteLoadingData = new CompleteFlag();
    ArrayList<UserMessage> messageList = new ArrayList<>();


    // variables and view use for display message
    RecyclerView messageRecycler;
    MessageListAdapter messageAdapter;
    Button btnSend;
    EditText etMessageContent;
    LinearLayoutManager linearLayoutManager;

    //convert timestamp to String
    DateManipulation dateManipulation = new DateManipulation();


    FirebaseUser currentUser;
    ChildEventListener memberListener, messageListener;
    DatabaseReference memberRef, messageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        setupView();

        conversationId = getIntent().getExtras().getString(GlobalConstants.CONVERSATION_ID);
        conversationTitle = getIntent().getExtras().getString(GlobalConstants.CONVERSATION_TITLE);



        currentUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadConversationMemberIdAndName();
        loadConversationMessages();
    }

    @Override
    protected void onStop() {
        super.onStop();
        messageRef.removeEventListener(messageListener);
        memberRef.removeEventListener(memberListener);

    }

    void loadConversationMemberIdAndName() {

        isCompleteLoadingData.addFlag(false);

        memberRef = FirebaseDatabase.getInstance().getReference().child("members").child(conversationId);
        memberListener = memberRef.addChildEventListener(new ChildEventListener() {
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
        });

        memberRef.addListenerForSingleValueEvent(new ValueEventListener() {
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

        messageRef = FirebaseDatabase.getInstance().getReference().child("conversation").child(conversationId);

        messageListener = messageRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                UserMessage userMessage = dataSnapshot.getValue(UserMessage.class);
                messageList.add(userMessage);

                if(isCompleteLoadingData.isAllFlagComplete()){
                    messageAdapter.notifyDataSetChanged();
                    makeAsLastMessage(userMessage);
                    scrollToBottomMessageList();

                }
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

        messageRef.addListenerForSingleValueEvent(new ValueEventListener() {
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

    private void makeAsLastMessage(UserMessage message){
        ConversationMetaInfo metaInfo = new ConversationMetaInfo(conversationTitle,message.getMessageContent(),message.getCreatedAt());
        FirebaseDatabase.getInstance().getReference()
                .child("conversationMetaInfo")
                .child(conversationId)
                .setValue(metaInfo);
    }

    private void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager)getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etMessageContent.getWindowToken(), 0);
    }

    private void loadComplete(){
        if (isCompleteLoadingData.isAllFlagComplete())
        {
           createAdapter();
        }

    }

    private void createAdapter(){
        messageAdapter = new MessageListAdapter(this, messageList,
               currentUser.getUid(), members);
        linearLayoutManager = new LinearLayoutManager(this);

        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setSmoothScrollbarEnabled(true);

        messageRecycler.setLayoutManager(linearLayoutManager);
        messageRecycler.setAdapter(messageAdapter);

    }

    private void scrollToBottomMessageList(){
        int messageCount = messageAdapter.getItemCount();
        messageRecycler.smoothScrollToPosition(messageCount-1);
    }

    private void setupView()
    {
        messageRecycler = findViewById(R.id.reyclerview_message_list);
        btnSend = findViewById(R.id.btnSend);
        etMessageContent = findViewById(R.id.etMessageContent);
        btnSend.setOnClickListener(this);

        btnSend.setEnabled(false);

        //disable send button when edit text is empty
        etMessageContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().length() == 0){
                    btnSend.setEnabled(false);
                }
                else{
                    btnSend.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }





    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSend:
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                UserMessage data = new UserMessage(etMessageContent.getText().toString(),
                        FirebaseAuth.getInstance().getCurrentUser().getUid(),
                        dateManipulation.timestampToString(timestamp));

                addMessageToFireBase(data);
                etMessageContent.getText().clear();
                hideKeyboard();
                break;
        }
    }



    private void addMessageToFireBase(UserMessage message)
    {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("conversation").child(conversationId).push();
        ref.setValue(message);

    }
}
