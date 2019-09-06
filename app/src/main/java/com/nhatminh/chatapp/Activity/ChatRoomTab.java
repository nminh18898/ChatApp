package com.nhatminh.chatapp.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nhatminh.chatapp.Adapter.ConversationListAdapter;
import com.nhatminh.chatapp.CompleteFlag;
import com.nhatminh.chatapp.GlobalConstants;
import com.nhatminh.chatapp.Interface.ConversationListAdapterClickListener;
import com.nhatminh.chatapp.Model.Conversation;
import com.nhatminh.chatapp.Model.ConversationMetaInfo;
import com.nhatminh.chatapp.R;

import java.util.ArrayList;
import java.util.List;

public class ChatRoomTab extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    Button btnSignOut;
    RecyclerView rvFriendList;
    ProgressBar pbLoadingConversation;

    ConversationListAdapter adapter;

    List<ConversationMetaInfo> conversationMetaInfoList;
    List<Conversation> conversationList;
    List<String> conversationIDList;

    String TAG = "TAG_CHAT_APP";

    CompleteFlag isLoadingComplete;
    boolean isFirstLoading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        isLoadingComplete = new CompleteFlag();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        setupView();
        conversationMetaInfoList = new ArrayList<>();
        conversationList = new ArrayList<>();
        conversationIDList = new ArrayList<>();

        isFirstLoading = true;
        loadConversationIDList();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void setupView(){
        btnSignOut = findViewById(R.id.btnSignOut);
        btnSignOut.setOnClickListener(this);

        rvFriendList = findViewById(R.id.rvFriendList);
        rvFriendList.setLayoutManager(new LinearLayoutManager(this));

        pbLoadingConversation = findViewById(R.id.pbLoadConversationProgress);
    }




    public void loadConversationMetaInfo(){
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

        for(int i=0;i<conversationIDList.size();i++){

            isLoadingComplete.addFlag(false);

           rootRef.child("conversationMetaInfo").child(conversationIDList.get(i))
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            ConversationMetaInfo info = dataSnapshot.getValue(ConversationMetaInfo.class);

                            if(isFirstLoading) {
                                conversationMetaInfoList.add(info);

                                Conversation conversation = new Conversation(dataSnapshot.getKey(), info);
                                conversationList.add(conversation);

                                isLoadingComplete.setOneFlagComplete();

                                if (isLoadingComplete.isAllFlagComplete()) {
                                    setConversationAdapter();
                                    isFirstLoading = false;
                                }
                            }
                            else
                            {
                                int pos = conversationIDList.indexOf(dataSnapshot.getKey());
                                conversationMetaInfoList.set(pos,info);
                                adapter.notifyDataSetChanged();
                            }

                            Log.e(TAG,"New message");
                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }
    }



    private void setConversationAdapter(){
        adapter = new ConversationListAdapter(ChatRoomTab.this, conversationMetaInfoList, new ConversationListAdapterClickListener() {
            @Override
            public void onClick(int position, ConversationMetaInfo conversationMetaInfo) {
                Intent intent = new Intent(ChatRoomTab.this,ChatActivity.class);
                intent.putExtra(GlobalConstants.CONVERSATION_ID, conversationList.get(position).getConversationId());
                intent.putExtra(GlobalConstants.CONVERSATION_TITLE,conversationMetaInfo.getTitle());
                startActivity(intent);

            }
        });

        rvFriendList.setAdapter(adapter);
        pbLoadingConversation.setVisibility(View.GONE);

    }

    public void loadConversationIDList()
    {
        pbLoadingConversation.setVisibility(View.VISIBLE);
        pbLoadingConversation.invalidate();

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference conversationRef = rootRef.child("users")
                .child(firebaseUser.getUid()).child("conversation");

        conversationRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String conversationID = dataSnapshot.getValue().toString();
                conversationIDList.add(conversationID);
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

        conversationRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                loadConversationMetaInfo();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }



    /**
     * Return to login activity
     * */
    private void signOut()
    {

        firebaseAuth.signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSignOut:
                signOut();
                break;


        }

    }
}
