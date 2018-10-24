package com.besheater.hearmycall;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatHandler {

    public static final String USER_MESSAGE = "user message";
    private List<ChatMessage> messages = new ArrayList<>();
    private MessagesAdapter messagesAdapter;
    private RecyclerView chatRecyclerView;
    private String userName;

    public ChatHandler(RecyclerView chatRecyclerView, String userName) {
        this.chatRecyclerView = chatRecyclerView;
        this.userName = userName;

        //Bind chat to adapter
        messagesAdapter = new MessagesAdapter(getListOfMessages(), "User");
        chatRecyclerView.setAdapter(messagesAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(chatRecyclerView.getContext());
        chatRecyclerView.setLayoutManager(layoutManager);
    }

    public String getUserName() {
        return userName;
    }

    public List<ChatMessage> getListOfMessages() {
        return messages;
    }
    public int getNumberOfMessages() {
        return messages.size();
    }

    public void scrollChatToBottom() {
        chatRecyclerView.smoothScrollToPosition(getNumberOfMessages() - 1);
    }

    public boolean addMessage(ChatMessage message) {
        this.messages.add(message);
        messagesAdapter.notifyDataSetChanged();
        return true;
    }

    public void fillWithTestValues() {
        messages.add(new ChatMessage(
                "Jack",
                "So what we gonna do tonight?",
                new Date()));
        messages.add(new ChatMessage(
                "Caroline",
                "I don't know. Maybe drink somewhere. Or just go to the disco dance As you already mentioned that",
                new Date()));
        messages.add(new ChatMessage(
                "User",
                "I don't care",
                new Date()));
        messages.add(new ChatMessage(
                "User",
                "Maybe others have some ideas?",
                new Date()));
        messages.add(new ChatMessage(
                "Alex",
                "Let's go to the beach. It will be fun I think.",
                new Date()));

        messagesAdapter.notifyDataSetChanged();
    }
}
