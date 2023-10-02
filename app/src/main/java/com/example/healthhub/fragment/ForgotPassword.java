package com.example.healthhub.fragment;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.healthhub.R;
import com.example.healthhub.activities.LoginSignupForgot;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.lazyprogrammer.motiontoast.MotionStyle;
import com.lazyprogrammer.motiontoast.MotionToast;


public class ForgotPassword extends Fragment {

    TextInputLayout forgetpassword;
    Button Reset;
    FirebaseAuth FAuth;
    Dialog dialog;
    private static View view;
    public ForgotPassword() {
        // Required empty public constructor
    }
    private void initViews() {
        forgetpassword = view.findViewById(R.id.Emailid);
        Reset = view.findViewById(R.id.reset);
        Reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checker();
            }
        });
        FAuth = FirebaseAuth.getInstance();

        // Setting text selector over textviews
//        @SuppressLint("ResourceType") XmlResourceParser xrp = getResources().getXml(R.drawable.text_selector);
//        try {
//            ColorStateList csl = ColorStateList.createFromXml(getResources(),
//                    xrp);
//
//            back.setTextColor(csl);
//            submit.setTextColor(csl);
//
//        } catch (Exception e) {
//        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_forgot_password, container, false);
        initViews();
//        setListeners();
        return view;
    }

    private void checker() {
//        dialog.setContentView(R.layout.checkinfo);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        dialog.setCancelable(false);
//        dialog.show();
               /* final ProgressDialog mDialog = new ProgressDialog(forgotpass.this);
                mDialog.setCancelable(false);
                mDialog.setCanceledOnTouchOutside(false);
                mDialog.setMessage("Checking info...");
                mDialog.show();*/

        FAuth.sendPasswordResetEmail(forgetpassword.getEditText().getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
//                    dialog.dismiss();
                    MotionToast motionToast = new MotionToast(getContext(),
                            0,
                            MotionStyle.LIGHT,
                            MotionStyle.SUCCESS,
                            MotionStyle.BOTTOM,
                            "Link Sent Succesfully",
                            "Reset Password Link sent Succesfully to your Email Account",
                            MotionStyle.LENGTH_SHORT)
                            .show();
//                    TastyToast.makeText(getActivity(), "Reset Password Link sent Succesfully to your Email Account ", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                    new LoginSignupForgot().replaceLoginFragment();

                } else {
//                    dialog.dismiss();
                    MotionToast motionToast = new MotionToast(getContext(),
                            0,
                            MotionStyle.LIGHT,
                            MotionStyle.ERROR,
                            MotionStyle.BOTTOM,
                            "Error",
                            task.getException().getMessage(),
                            MotionStyle.LENGTH_SHORT)
                            .show();
//                    TastyToast.makeText(getActivity(), task.getException().getMessage(), TastyToast.LENGTH_LONG, TastyToast.ERROR);
                    //  ReusableCodeForAll.ShowAlert(ChefForgotPassword.this, "Error", task.getException().getMessage());
                }
            }
        });
    }
}