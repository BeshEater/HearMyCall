package com.besheater.hearmycall;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.List;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder> {
    public static final int USER_MESSAGE = 0;
    public static final int TEAMMATE_MESSAGE = 1;
    private UserData userData;
    private List<ChatMessage> messages;

    public MessagesAdapter(List<ChatMessage> messages, UserData userData) {
        this.messages = messages;
        this.userData = userData;

    }

    @NonNull
    @Override
    public MessagesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        // Create FrameLayout variable for return
        FrameLayout messageFrame;
        // Find to whom message belongs - user or teammate
        if (viewType == USER_MESSAGE) {
            // This is a user message
            messageFrame = (FrameLayout) LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.user_message, viewGroup, false);
        } else {
            // This is a teammate message
            messageFrame = (FrameLayout) LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.teammate_message, viewGroup, false);
        }

        return new ViewHolder(messageFrame);
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage message = messages.get(position);
        if (isUserMessage(message)) {
            // This is a User message
            return USER_MESSAGE;
        } else {
            // This is a teammate message
            return TEAMMATE_MESSAGE;
        }
    }

    private boolean isUserMessage(ChatMessage message) {
        return message.getUser().id == userData.getId();
    }

    @Override
    public void onBindViewHolder(@NonNull MessagesAdapter.ViewHolder viewHolder, int position) {
        FrameLayout messageFrame = viewHolder.messageFrame;
        ChatMessage message = messages.get(position);
        // Set message author and avatar image if it's message from teammate
        if (!isUserMessage(message)) {
            // Set Author
            TextView messageAuthor = messageFrame.findViewById(R.id.teammate_name);
            messageAuthor.setText(message.getAuthor());
            // Set Avatar image
            int avatarImageNum = message.getUser().avatarImageNum;
            ImageView avatarImage = messageFrame.findViewById(R.id.avatar_image);
            avatarImage.setImageResource(AppData.getAvatarImage(avatarImageNum).getAvatarImageLargeId());
        }
        // Set message text
        TextView messageText = messageFrame.findViewById(R.id.message_text);
        messageText.setText(message.getText());
        // Set message time
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
        simpleDateFormat.applyPattern("HH:mm:ss");
        String time = simpleDateFormat.format(message.getTime());
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
