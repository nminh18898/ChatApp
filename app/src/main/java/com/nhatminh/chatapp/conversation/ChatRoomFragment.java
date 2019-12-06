package com.nhatminh.chatapp.conversation;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nhatminh.chatapp.R;
import com.nhatminh.chatapp.Utils.CompleteFlag;
import com.nhatminh.chatapp.Utils.GlobalConstants;
import com.nhatminh.chatapp.login.LoginActivity;

import java.util.ArrayList;
import java.util.List;


public class ChatRoomFragment extends Fragment implements View.OnClickListener {

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


    public ChatRoomFragment() {
    }

    public static ChatRoomFragment newInstance() {

        Bundle args = new Bundle();

        ChatRoomFragment fragment = new ChatRoomFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat_room, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupView(view);

        loadConversationIDList();
    }

    private void setupView(View view){
        btnSignOut = view.findViewById(R.id.btnSignOut);
        btnSignOut.setOnClickListener(this);

        rvFriendList = view.findViewById(R.id.rvFriendList);
        rvFriendList.setLayoutManager(new LinearLayoutManager(getActivity()));

        pbLoadingConversation = view.findViewById(R.id.pbLoadConversationProgress);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        isLoadingComplete = new CompleteFlag();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        conversationMetaInfoList = new ArrayList<>();
        conversationList = new ArrayList<>();
        conversationIDList = new ArrayList<>();

        isFirstLoading = true;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSignOut:
                signOut();
                break;
        }
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
        adapter = new ConversationListAdapter(getActivity(), conversationMetaInfoList, new ConversationListAdapterClickListener() {
            @Override
            public void onClick(int position, ConversationMetaInfo conversationMetaInfo) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);
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

    private void signOut() {
        firebaseAuth.signOut();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

}
