package com.example.healthhub.fragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.healthhub.R;

import java.util.List;


public class mailVerification extends Fragment {
    Button email, chefregis;
    View view;

    public mailVerification() {
        // Required empty public constructor
    }

    private void initViews() {
        email = view.findViewById(R.id.Verify);
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGmailApp();
            }
        });
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_mail_verification, container, false);
        initViews();
//        setListeners();
        return view;
    }

    private void openGmailApp() {
//            Intent intent = new Intent(mailverify.this, mainmenu.class);
//            startActivity(intent);

            Intent launchIntent = null;
            try{
                launchIntent = new Intent(Intent.ACTION_MAIN);
                launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                launchIntent.addCategory(Intent.CATEGORY_APP_EMAIL);
                startActivity(launchIntent);
            } catch (Exception ignored) {}

            if(launchIntent == null){
                startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://play.google.com/store/apps/details?id=" + "com.google.android.gm")));
            } else {
                startActivity(launchIntent);
            }
    }
}