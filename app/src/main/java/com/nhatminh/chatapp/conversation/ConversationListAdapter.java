package com.nhatminh.chatapp.conversation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.nhatminh.chatapp.R;

import java.util.List;

public class ConversationListAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<ConversationMetaInfo> conversationMetaInfoList;

    private ConversationListAdapterClickListener listener;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view =  LayoutInflater.from(context)
                .inflate(R.layout.item_chat_room, parent, false);
        return new ConversationListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ConversationListViewHolder) holder).bind(conversationMetaInfoList.get(position), listener);


    }

    @Override
    public int getItemCount() {
        return conversationMetaInfoList.size();
    }


    public ConversationListAdapter(Context context, List<ConversationMetaInfo> conversationMetaInfoList, ConversationListAdapterClickListener conversationListAdapterClickListener) {
        this.context = context;
        this.conversationMetaInfoList = conversationMetaInfoList;
        this.listener = conversationListAdapterClickListener;
    }


    // View holder
    private class ConversationListViewHolder extends RecyclerView.ViewHolder{
        ImageView ivProfilePicture;
        TextView tvConversationName, tvLastMessageSent, tvTimeStamp;
        ConversationListAdapterClickListener listener;


        public ConversationListViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfilePicture = itemView.findViewById(R.id.ivUserProfilePicture);
            tvConversationName = itemView.findViewById(R.id.tvConversationName);
            tvLastMessageSent = itemView.findViewById(R.id.tvLastMessageSent);
            tvTimeStamp = itemView.findViewById(R.id.tvTimeStamp);
        }

        void bind(final ConversationMetaInfo conversationMetaInfo, final ConversationListAdapterClickListener listener){
            /**
             * ivProfilePicture handle later
             */
            tvConversationName.setText(conversationMetaInfo.getTitle());
            tvLastMessageSent.setText(conversationMetaInfo.getLastMessageSent());
            tvTimeStamp.setText(conversationMetaInfo.getTimeStamp());

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
