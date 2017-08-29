package com.graduation.a3ltreq.ontheroad.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.graduation.a3ltreq.ontheroad.R;
import com.graduation.a3ltreq.ontheroad.app.Utility;
import com.graduation.a3ltreq.ontheroad.helper.TimelineContract;

import java.util.HashMap;


public class ProfileFragment extends Fragment {
    private TextView txtName;
    private TextView txtEmail;
    private Context mContext;



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
        mContext=getContext();
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        // Inflate the layout for this fragment
        txtName =  rootView.findViewById(R.id.name);
        txtEmail =  rootView.findViewById(R.id.email);




        HashMap<String, String> user = Utility.getUserDetails(mContext);
        String name = user.get(TimelineContract.PickEntry.COLUMN_USER_NAME);
        String email = user.get(TimelineContract.PickEntry.COLUMN_KEY_EMAIL);

        //Displaying the user details on the screen
        txtName.setText(name);
        txtEmail.setText(email);


        return rootView;

    }




}