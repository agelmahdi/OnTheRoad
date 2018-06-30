package com.graduation.a3ltreq.ontheroad.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.graduation.a3ltreq.ontheroad.R;

import static com.graduation.a3ltreq.ontheroad.activity.LoginActivity.PREFERENCE_KEY;
import static com.graduation.a3ltreq.ontheroad.activity.LoginActivity.PREF_USER_MAIL;
import static com.graduation.a3ltreq.ontheroad.activity.LoginActivity.PREF_USER_NAME;


public class ProfileFragment extends Fragment {

    SharedPreferences SharedPrefs;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SharedPrefs = getContext().getSharedPreferences(PREFERENCE_KEY, 0);

        Context mContext = getContext();
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        // Inflate the layout for this fragment
        TextView txtName = rootView.findViewById(R.id.name);
        TextView txtEmail = rootView.findViewById(R.id.email);


        String name = SharedPrefs.getString(PREF_USER_NAME, "");
        String email =SharedPrefs.getString(PREF_USER_MAIL,"");



        //Displaying the user details on the screen
        txtName.setText(name);
        txtEmail.setText(email);


        return rootView;

    }




}