package com.nhatminh.chatapp.Model;

public class ConversationMetaInfo {

    String title;
    String lastMessageSent;
    long timeStamp;


    public ConversationMetaInfo() {
    }

    public ConversationMetaInfo(String title, String lastMessageSent, long timeStamp) {
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

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
