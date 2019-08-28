package com.nhatminh.chatapp.Model;

import java.util.ArrayList;
import java.util.List;

public class Conversation {
    String conversationId;
    ConversationMetaInfo metaInfo;
    List<User> members;
    List<UserMessage> messages;


    public Conversation(String conversationId) {
        this();
        this.conversationId = conversationId;

    }

    public Conversation(String conversationId, ConversationMetaInfo metaInfo) {
        this();
        this.conversationId = conversationId;
        this.metaInfo = metaInfo;

    }

    public Conversation() {
        members = new ArrayList<>();
        messages = new ArrayList<>();
        metaInfo = new ConversationMetaInfo();
    }

    public Conversation(String conversationId, ConversationMetaInfo metaInfo, List<User> members, List<UserMessage> messages) {
        this.conversationId = conversationId;
        this.metaInfo = metaInfo;
        this.members = members;
        this.messages = messages;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public ConversationMetaInfo getMetaInfo() {
        return metaInfo;
    }

    public void setMetaInfo(ConversationMetaInfo metaInfo) {
        this.metaInfo = metaInfo;
    }

    public List<User> getMembers() {
        return members;
    }

    public void setMembers(List<User> members) {
        this.members = members;
    }

    public List<UserMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<UserMessage> messages) {
        this.messages = messages;
    }

    public void insertMember(User user) {
        this.members.add(user);
    }

    public void insertMessage(UserMessage message) {
        this.messages.add(message);
    }
}
