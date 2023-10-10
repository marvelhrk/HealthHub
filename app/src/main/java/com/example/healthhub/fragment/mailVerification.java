package com.example.healthhub.fragment;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.healthhub.LocalStorage;
import com.example.healthhub.R;
import com.example.healthhub.activities.LoginSignupForgot;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lazyprogrammer.motiontoast.MotionStyle;
import com.lazyprogrammer.motiontoast.MotionToast;

import java.util.List;


public class mailVerification extends Fragment {
    Button email;
    View view;

    LocalStorage localStorage;
    private static FragmentManager fragmentManager;
    public mailVerification() {
        // Required empty public constructor
    }

    private void initViews() {
        email = view.findViewById(R.id.Verify);
        localStorage = new LocalStorage(getContext());
        fragmentManager = getActivity().getSupportFragmentManager();
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
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        checkemail();
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

    private void checkemail() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null && user.isEmailVerified()) {
            MotionToast motionToast = new MotionToast(getContext(),
                    0,
                    MotionStyle.LIGHT,
                    MotionStyle.SUCCESS,
                    MotionStyle.BOTTOM,
                    "Registration Completed",
                    "You can successfully login to your account.",
                    MotionStyle.LENGTH_SHORT)
                    .show();
            new LoginSignupForgot().replaceLoginFragment();


        } else {
            MotionToast motionToast = new MotionToast(getContext(),
                    0,
                    MotionStyle.LIGHT,
                    MotionStyle.WARNING,
                    MotionStyle.BOTTOM,
                    "Email Not Yet Verified",
                    "Please complete your email verification inorder to Sign in",
                    MotionStyle.LENGTH_SHORT)
                    .show();

            // User's email is not verified
            // You can prompt the user to verify their email or take appropriate actions
        }

    }



}