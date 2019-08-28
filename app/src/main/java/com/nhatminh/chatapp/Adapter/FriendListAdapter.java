package com.nhatminh.chatapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.nhatminh.chatapp.Interface.FriendListAdapterClickListener;
import com.nhatminh.chatapp.Model.ConversationMetaInfo;
import com.nhatminh.chatapp.Model.User;
import com.nhatminh.chatapp.R;

import java.util.List;

public class FriendListAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<ConversationMetaInfo> conversationMetaInfoList;

    private FriendListAdapterClickListener listener;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view =  LayoutInflater.from(context)
                .inflate(R.layout.item_friend_list, parent, false);
        return new FriendListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((FriendListViewHolder) holder).bind(conversationMetaInfoList.get(position), listener);


    }

    @Override
    public int getItemCount() {
        return conversationMetaInfoList.size();
    }


    public FriendListAdapter(Context context, List<ConversationMetaInfo> conversationMetaInfoList, FriendListAdapterClickListener friendListAdapterClickListener) {
        this.context = context;
        this.conversationMetaInfoList = conversationMetaInfoList;
        this.listener = friendListAdapterClickListener;
    }


    // View holder
    private class FriendListViewHolder extends RecyclerView.ViewHolder{
        ImageView ivProfilePicture;
        TextView tvConversationName, tvLastMessageSent, tvTimeStamp;
        FriendListAdapterClickListener listener;


        public FriendListViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfilePicture = itemView.findViewById(R.id.ivUserProfilePicture);
            tvConversationName = itemView.findViewById(R.id.tvConversationName);
            tvLastMessageSent = itemView.findViewById(R.id.tvLastMessageSent);
            tvTimeStamp = itemView.findViewById(R.id.tvTimeStamp);
        }

        void bind(final ConversationMetaInfo conversationMetaInfo, final FriendListAdapterClickListener listener){
            /**
             * ivProfilePicture handle later
             */
            tvConversationName.setText(conversationMetaInfo.getTitle());
            tvLastMessageSent.setText(conversationMetaInfo.getLastMessageSent());


            FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl();


            //tvTimeStamp.setText();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(getAdapterPosition(),conversationMetaInfo);
                }
            });


        }


    }




}
