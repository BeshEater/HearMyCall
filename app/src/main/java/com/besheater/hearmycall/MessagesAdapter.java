package com.besheater.hearmycall;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.List;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder> {

    private List<ChatMessage> messages;
    private String userName;

    public MessagesAdapter(List<ChatMessage> messages, String userName) {
        this.messages = messages;
        this.userName = userName;
    }

    @NonNull
    @Override
    public MessagesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        //Create FrameLayout variable for return
        FrameLayout messageFrame;
        //Find to whom message belongs - user or teammate
        if (viewType == 0) {
            //This is user message
            messageFrame = (FrameLayout) LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.user_message, viewGroup, false);
        } else {
            //This is teammate message
            messageFrame = (FrameLayout) LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.teammate_message, viewGroup, false);
        }

        return new ViewHolder(messageFrame);
    }

    @Override
    public int getItemViewType(int position) {
        //return 0 if user message
        //return 1 if teammate message
        ChatMessage message = messages.get(position);
        if (message.getAuthor().equals(userName)) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessagesAdapter.ViewHolder viewHolder, int position) {
        FrameLayout messageFrame = viewHolder.messageFrame;
        ChatMessage message = messages.get(position);
        //Set message author if it's message from teammate
        if (!message.getAuthor().equals(userName)) {
            TextView messageAuthor = messageFrame.findViewById(R.id.teammate_name);
            messageAuthor.setText(message.getAuthor());
        }
        //Set message text
        TextView messageText = messageFrame.findViewById(R.id.message_text);
        messageText.setText(message.getMessageText());
        //Set message time
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
        simpleDateFormat.applyPattern("HH:mm:ss");
        String time = simpleDateFormat.format(message.getMessageTime());
        TextView messageTime = messageFrame.findViewById(R.id.message_time);
        messageTime.setText(time);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private FrameLayout messageFrame;

        public ViewHolder(@NonNull FrameLayout messageFrame) {
            super(messageFrame);
            this.messageFrame = messageFrame;
        }
    }
}
