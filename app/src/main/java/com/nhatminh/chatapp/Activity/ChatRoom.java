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
import com.nhatminh.chatapp.Adapter.FriendListAdapter;
import com.nhatminh.chatapp.GlobalConstants;
import com.nhatminh.chatapp.Interface.FriendListAdapterClickListener;
import com.nhatminh.chatapp.Model.Conversation;
import com.nhatminh.chatapp.Model.ConversationMetaInfo;
import com.nhatminh.chatapp.Model.User;
import com.nhatminh.chatapp.R;

import java.util.ArrayList;
import java.util.List;

public class ChatRoom extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    Button btnSignOut;
    RecyclerView rvFriendList;
    ProgressBar pbLoadingConversation;


    List<ConversationMetaInfo> conversationMetaInfoList = new ArrayList<>();
    List<Conversation> conversations = new ArrayList<>();


    String TAG = "TAG_CHAT_APP";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        setupView();
        loadConversationListFromFireBase();






    }

    private void setupView(){
        btnSignOut = findViewById(R.id.btnSignOut);
        btnSignOut.setOnClickListener(this);

        rvFriendList = findViewById(R.id.rvFriendList);
        rvFriendList.setLayoutManager(new LinearLayoutManager(this));

        pbLoadingConversation = findViewById(R.id.pbLoadConversationProgress);


    }


    public void loadConversationListFromFireBase()
    {
        pbLoadingConversation.setVisibility(View.VISIBLE);
        pbLoadingConversation.invalidate();

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference conversationRef = rootRef.child("users").child(firebaseUser.getUid()).child("conversation");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //conversationMetaInfoList = new ArrayList<>();
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    ConversationMetaInfo metaInfo = childSnapshot.getValue(ConversationMetaInfo.class);

                    conversationMetaInfoList.add(metaInfo);
                    conversations.add(new Conversation(childSnapshot.getKey(),metaInfo));
                    Log.e(TAG,"On data change");
                }

                FriendListAdapter adapter = new FriendListAdapter(ChatRoom.this, conversationMetaInfoList, new FriendListAdapterClickListener() {
                    @Override
                    public void onClick(int position, ConversationMetaInfo conversationMetaInfo) {
                        Intent intent = new Intent(ChatRoom.this,ChatActivity.class);
                        intent.putExtra(GlobalConstants.CONVERSATION_ID, conversations.get(position).getConversationId());
                        startActivity(intent);

                    }
                });

                rvFriendList.setAdapter(adapter);
                pbLoadingConversation.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}


        };
        conversationRef.addListenerForSingleValueEvent(eventListener);

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
