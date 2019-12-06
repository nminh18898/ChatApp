package com.nhatminh.chatapp.conversation;

public class ConversationMetaInfo {

    String title;
    String lastMessageSent;
    String timeStamp;


    public ConversationMetaInfo() {
    }

    public ConversationMetaInfo(String title, String lastMessageSent, String timeStamp) {
        this.title = title;
        this.lastMessageSent = lastMessageSent;
        this.timeStamp = timeStamp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLastMessageSent() {
        return lastMessageSent;
    }

    public void setLastMessageSent(String lastMessageSent) {
        this.lastMessageSent = lastMessageSent;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
