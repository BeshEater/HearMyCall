package com.besheater.hearmycall;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ChatHandler implements Parcelable {
    public static final Parcelable.Creator<ChatHandler> CREATOR = new MyCreator();

    private List<ChatMessage> messages = new ArrayList<>();
    private MessagesAdapter messagesAdapter;
    private RecyclerView chatRecyclerView;

    public ChatHandler() {
    }

    public ChatHandler(Parcel source) {
        ChatMessage[] arr = (ChatMessage[]) source.readParcelableArray(null);
        messages = new ArrayList<>(Arrays.asList(arr));
    }

    public void createChat(RecyclerView chatRecyclerView) {
        this.chatRecyclerView = chatRecyclerView;

        //Bind chat to adapter
        messagesAdapter = new MessagesAdapter(messages);
        chatRecyclerView.setAdapter(messagesAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(chatRecyclerView.getContext());
        chatRecyclerView.setLayoutManager(layoutManager);
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
                new Date(),
                false,
                true));
        messages.add(new ChatMessage(
                "Caroline",
                "I don't know. Maybe drink somewhere. Or just go to the disco dance As you already mentioned that",
                new Date(),
                false,
                true));
        messages.add(new ChatMessage(
                "User",
                "I don't care",
                new Date(),
                true,
                true));
        messages.add(new ChatMessage(
                "User",
                "Maybe others have some ideas?",
                new Date(),
                true,
                true));
        messages.add(new ChatMessage(
                "Alex",
                "Let's go to the beach. It will be fun I think.",
                new Date(),
                false,
                true));

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        ChatMessage[] arr = new ChatMessage[messages.size()];
        arr = messages.toArray(arr);
        dest.writeParcelableArray(arr, 0);
    }

    public static class MyCreator implements Parcelable.Creator<ChatHandler> {
        public ChatHandler createFromParcel(Parcel source) {
            return new ChatHandler(source);
        }
        public ChatHandler[] newArray(int size) {
            return new ChatHandler[size];
        }
    }
}
