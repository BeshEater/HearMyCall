package com.besheater.hearmycall;

import android.os.Handler;
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
    private UserData userData;
    private boolean isUpdating = false;
    private MessagesAdapter messagesAdapter;
    private RecyclerView chatRecyclerView;
    private ConnectedUserListener listener;
    private WebServerHandler webServerHandler;
    private final long updateInterval = 3000; //ms

    public ChatHandler(UserData userData) {
        this.userData = userData;
    }

    public ChatHandler(Parcel source) {
        ChatMessage[] arr = (ChatMessage[]) source.readParcelableArray(null);
        messages = new ArrayList<>(Arrays.asList(arr));
    }

    public void createChat(ConnectedUserListener listener,
                           RecyclerView chatRecyclerView, WebServerHandler webServerHandler) {
        this.chatRecyclerView = chatRecyclerView;
        this.webServerHandler = webServerHandler;
        this.listener = listener;

        // Bind chat to adapter
        messagesAdapter = new MessagesAdapter(messages, userData);
        chatRecyclerView.setAdapter(messagesAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(chatRecyclerView.getContext());
        chatRecyclerView.setLayoutManager(layoutManager);
    }

    public int getNumberOfMessages() {
        return messages.size();
    }

    public void scrollChatToBottom() {
        chatRecyclerView.smoothScrollToPosition(getNumberOfMessages() - 1);
    }

    public boolean addMessage(ChatMessage message) {
        this.messages.add(message);
        webServerHandler.sendMessage(message);
        messagesAdapter.notifyDataSetChanged();
        return true;
    }

    public void startPeriodicUpdates () {
        isUpdating = true;

        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (isUpdating) {
                    // Update connected user
                    listener.setConnectedUser(webServerHandler.getConnectedUser());

                    // Update messages
                    List<ChatMessage> newMessages = webServerHandler.getMessages();
                    if (newMessages != null && newMessages.size() >= messages.size()) {
                        messages.clear();
                        messages.addAll(newMessages);
                        messagesAdapter.notifyDataSetChanged();
                    }
                    // Request the same later
                    handler.postDelayed(this, updateInterval);
                }
            }
        });

    }

    public void stopPeriodicUpdates() {
        isUpdating = false;
    }

    public boolean isChatEmpty() {
        return messages.isEmpty();
    }

    public void fillWithTestValues() {
        User user1 = new User(2, "Jack", 53.215,
                63.621, 4, null, null,
                new Date().getTime());
        User user2 = new User(3,"Margaret", 53.2157,
                63.6215, 7, null, null,
                new Date().getTime());
        User user3 = new User(4, "Omar", 53.2152,
                63.6211, 10,
                "Who want to watch football and drink some beers?\n" +
                        "Come to me if you want, bring some chips with you though", null,
                new Date().getTime());

        messages.add(new ChatMessage(
                user1,
                "So what we gonna do tonight?",
                new Date().getTime()));
        messages.add(new ChatMessage(
                user2,
                "I don't know. Maybe drink somewhere. Or just go to the disco dance As you already mentioned that",
                new Date().getTime()));
        messages.add(new ChatMessage(
                userData.getThisUserObject(),
                "I don't care",
                new Date().getTime()));
        messages.add(new ChatMessage(
                user1,
                "Maybe others have some ideas?",
                new Date().getTime()));
        messages.add(new ChatMessage(
                user2,
                "Let's go to the beach. It will be fun I think.",
                new Date().getTime()));

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

    public interface ConnectedUserListener {
        void  setConnectedUser(User user);
    }
}
