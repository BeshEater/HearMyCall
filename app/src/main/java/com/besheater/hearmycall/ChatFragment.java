package com.besheater.hearmycall;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {

    private ChatHandler chatHandler;
    private MessagesAdapter messagesAdapter;

    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_chat, container, false);

        //Create Chat
        RecyclerView chatRecyclerView = view.findViewById(R.id.chat_recycler);
        this.chatHandler = new ChatHandler(chatRecyclerView,"User");
        chatHandler.fillWithTestValues();

        //Register listeners
        //For send button
        ImageButton sendButton = view.findViewById(R.id.send_message_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Send new message to chat
                EditText newMessageText = view.findViewById(R.id.new_message_text);
                String text = newMessageText.getText().toString();
                String author = chatHandler.getUserName();
                Date date = new Date();
                ChatMessage message = new ChatMessage(author, text, date);
                chatHandler.addMessage(message);
                chatHandler.scrollChatToBottom();
                newMessageText.getText().clear();
            }
        });

        return view;
    }

}
