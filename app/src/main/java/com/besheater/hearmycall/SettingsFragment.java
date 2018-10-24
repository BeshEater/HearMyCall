package com.besheater.hearmycall;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {
    UserData userData;


    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_settings, container, false);

        //Register listeners
        ImageView avatarImage = view.findViewById(R.id.avatar_image);
        avatarImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), AvatarImageChoser.class);
                startActivityForResult(intent, 1);
            }
        });

        return view;
    }

}
