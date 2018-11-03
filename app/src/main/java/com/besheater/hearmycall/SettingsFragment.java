package com.besheater.hearmycall;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {
    public static final int PICK_AVATAR_IMAGE_REQUEST = 1;
    private UserData userData;
    private View view;

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_settings, container, false);


        //Set UserData object reference
        MainActivity mainActivity = (MainActivity) getActivity();
        userData = mainActivity.getUserData();

        //Set all settings according to UserData
        updateFromUserData();

        //*********Register listeners************

        //Avatar name
        final EditText avatarName = view.findViewById(R.id.avatar_name);
        avatarName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                userData.setName(s.toString());
            }
        });

        //Avatar image chooser
        ImageView avatarImage = view.findViewById(R.id.avatar_image);
        avatarImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AvatarImageChooser.class);
                startActivityForResult(intent, PICK_AVATAR_IMAGE_REQUEST);
            }
        });

        //Invisible check box
        final CheckBox isInvisible = view.findViewById(R.id.isInvisible);
        isInvisible.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    userData.setVisible(false);
                } else {
                    userData.setVisible(true);
                }
            }
        });

        //Channel
        final Spinner channel = view.findViewById(R.id.channel);
        channel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                userData.setChatChannelPos(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Receive notification check box
        final CheckBox isReceiveNotification = view.findViewById(R.id.isReceiveNotification);
        isReceiveNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                userData.setReceiveNotification(isChecked);
            }
        });

        //Radius for notification
        SeekBar radiusOfNotify = view.findViewById(R.id.radius_seekbar);
        radiusOfNotify.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                userData.setNotificationRadius(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        return view;
    }

    public void updateFromUserData() {
        //Avatar name
        EditText avatarName = view.findViewById(R.id.avatar_name);
        avatarName.setText(userData.getName());
        //Avatar image
        ImageView avatarImage = view.findViewById(R.id.avatar_image);
        avatarImage.setImageResource(userData.getAvatarImage().getAvatarImageLargeId());
        //Invisible check box
        CheckBox isInvisible = view.findViewById(R.id.isInvisible);
        isInvisible.setChecked(!userData.isVisible());
        //Channel
        Spinner channel = view.findViewById(R.id.channel);
        channel.setSelection(userData.getChatChannelPos());
        //Receive notification check box
        CheckBox isReceiveNotification = view.findViewById(R.id.isReceiveNotification);
        isReceiveNotification.setChecked(userData.isReceiveNotification());
        //Radius for notification
        SeekBar radiusOfNotify = view.findViewById(R.id.radius_seekbar);
        radiusOfNotify.setProgress(userData.getNotificationRadius());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        //Avatar image chosen
        if (requestCode == PICK_AVATAR_IMAGE_REQUEST) {
            if(resultCode == Activity.RESULT_OK){
                //Acquiring data from Intent
                int avatarImageNum = intent.getIntExtra(AvatarImageChooser.AVATAR_IMAGE_NUM, 2);

                ImageView imageView = getActivity().findViewById(R.id.avatar_image);
                AvatarImage avatarImage = AppData.getAvatarImageAtPos(avatarImageNum);
                //Save to UserData
                userData.setAvatarImage(avatarImage);
                //Display new avatar image
                imageView.setImageResource(avatarImage.getAvatarImageLargeId());

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

}
