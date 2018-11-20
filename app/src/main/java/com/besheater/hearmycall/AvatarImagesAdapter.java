package com.besheater.hearmycall;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.List;

public class AvatarImagesAdapter extends RecyclerView.Adapter<AvatarImagesAdapter.ViewHolder> {
    private Listener listener;
    private List<AvatarImage> avatarImages;

    public AvatarImagesAdapter(List<AvatarImage> list) {
        this.avatarImages = list;
    }

    @NonNull
    @Override
    public AvatarImagesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        CardView cv;
        cv = (CardView) LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.avatar_image_card, viewGroup, false);
        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(@NonNull AvatarImagesAdapter.ViewHolder viewHolder,final int position) {
        CardView cv = viewHolder.cv;
        AvatarImage avatarImage = avatarImages.get(position);
        // Set ImageView
        ImageView imageView = cv.findViewById(R.id.avatar_image);
        imageView.setImageResource(avatarImage.getAvatarImageLargeId());
        // Set listener for each avatar image
        cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return avatarImages.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private CardView cv;

        public ViewHolder(@NonNull CardView cv) {
            super(cv);
            this.cv = cv;
        }
    }

    interface Listener {
        void onClick(int position);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }
}
