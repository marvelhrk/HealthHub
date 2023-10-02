package com.example.healthhub.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.healthhub.LocalStorage;
import com.example.healthhub.R;
import com.example.healthhub.activities.MainScreen;
import com.example.healthhub.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.lazyprogrammer.motiontoast.MotionStyle;
import com.lazyprogrammer.motiontoast.MotionToast;

public class EmailLogin extends Fragment {
    private static View view;

    private static TextInputLayout email, pass;
    private static Button loginButton;
    private static TextView forgotPassword, signUp;
    private static CheckBox show_hide_password;
    private static LinearLayout loginLayout;
    private static FragmentManager fragmentManager;

    ProgressDialog progressDialog;
    LocalStorage localStorage;
    String userString;
    User user;

    TextView Forgotpassword;
    SharedPreferences sp;
    FirebaseAuth Fauth;
    String emailid, pwd;
    Dialog dialog;
    public EmailLogin() {
        // Required empty public constructor
    }

    private void initViews() {
        fragmentManager = getActivity().getSupportFragmentManager();

        email = view.findViewById(R.id.Lemail);
        pass = view.findViewById(R.id.Lpassword);
        loginButton = view.findViewById(R.id.login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                MotionToast motionToast = new MotionToast(getContext(),
//                        0,
//                        MotionStyle.LIGHT,
//                        MotionStyle.INFO,
//                        MotionStyle.CENTER,
//                        "Loading",
//                        ".......",
//                        MotionStyle.LENGTH_SHORT)
//                        .show();
                dialog.setContentView(R.layout.loading);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCancelable(false);
                dialog.show();
//                TastyToast.makeText(getActivity(), "Loading....", TastyToast.LENGTH_SHORT, TastyToast.CONFUSING);
//                Toast.makeText(getActivity(),"YO",Toast.LENGTH_SHORT).show();

                checkValidation();
            }
        });
        forgotPassword =(Button) view.findViewById(R.id.forgotpass);
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.frameContainer,
                                new ForgotPassword(),
                                "ForgotPassword").commit();
            }
        });
        signUp = view.findViewById(R.id.createnew);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.frameContainer,
                                new Signup(),
                                "Signup").commit();
            }
        });
//      show_hide_password = view.findViewById(R.id.show_hide_password);
        loginLayout = view.findViewById(R.id.login_layout);
        progressDialog = new ProgressDialog(getContext());
        localStorage = new LocalStorage(getContext());
        String userString = localStorage.getUserLogin();
        Gson gson = new Gson();
        userString = localStorage.getUserLogin();
        user = gson.fromJson(userString, User.class);
        Log.d("User", userString);

        dialog = new Dialog(getContext());
        Fauth = FirebaseAuth.getInstance();

    }

//    private void setListeners() {
//        loginButton.setOnClickListener((View.OnClickListener) this);
//        forgotPassword.setOnClickListener((View.OnClickListener) this);
//        signUp.setOnClickListener((View.OnClickListener) this);
//
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_email_login, container, false);
        initViews();
//        setListeners();
        return view;

    }


    private void checkValidation(){
        emailid = email.getEditText().getText().toString().trim();
        pwd = pass.getEditText().getText().toString().trim();
        emailid = email.getEditText().getText().toString().trim();
        pwd = pass.getEditText().getText().toString().trim();

        if (isValid()) {
                       /* final ProgressDialog mDialog = new ProgressDialog(emailogin.this);
                        mDialog.setCanceledOnTouchOutside(false);
                        mDialog.setCancelable(false);
                        mDialog.setMessage("Signing In Please Wait.......");
                        mDialog.show();*/

            Fauth.signInWithEmailAndPassword(emailid, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        // mDialog.dismiss();

                        if (Fauth.getCurrentUser().isEmailVerified()) {
//                            dialog.dismiss();
                            // mDialog.dismiss();
//                            SharedPreferences sharedPreferences = getSharedPreferences(emailogin.PREFS_NAME,0);
//                            SharedPreferences.Editor editor = sharedPreferences.edit();
//                            editor.putBoolean("hasloggedin",true);
//                            editor.commit();
                            dialog.dismiss();
                            MotionToast motionToast = new MotionToast(getContext(),
                                    0,
                                    MotionStyle.LIGHT,
                                    MotionStyle.SUCCESS,
                                    MotionStyle.BOTTOM,
                                    "Login Successfull",
                                    "Congratulation! You Have Successfully Login",
                                    MotionStyle.LENGTH_SHORT)
                                    .show();
//                            TastyToast.makeText(getActivity(), "Congratulation!\nYou Have Successfully Login", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                            // Toast.makeText(emailogin.this, "      Congratulation! \nYou Have Successfully Login", Toast.LENGTH_SHORT).show();
                            localStorage = new LocalStorage(getContext());
                            localStorage.createUserLoginSession(userString);
                            Intent Z = new Intent(getActivity(), MainScreen.class);
                            startActivity(Z);

                        } else {
                            dialog.dismiss();
                            MotionToast motionToast = new MotionToast(getContext(),
                                    0,
                                    MotionStyle.LIGHT,
                                    MotionStyle.ERROR,
                                    MotionStyle.BOTTOM,
                                    "Verification Failed",
                                    "You Have Not Verified Account on Your Email.",
                                    MotionStyle.LENGTH_SHORT)
                                    .show();
//                            TastyToast.makeText(getActivity(), "Verification Failed.\n", TastyToast.LENGTH_LONG, TastyToast.INFO);
                            //  Toast.makeText(emailogin.this, "      Verification Failed. \nYou Have Not Verified Account on Your Email Address.",Toast.LENGTH_SHORT).show();

                        }
                    } else {
                        dialog.dismiss();
                        // mDialog.dismiss();
                        MotionToast motionToast = new MotionToast(getContext(),
                                0,
                                MotionStyle.LIGHT,
                                MotionStyle.ERROR,
                                MotionStyle.BOTTOM,
                                "Error",
                                task.getException().getMessage(),
                                MotionStyle.LENGTH_SHORT)
                                .show();
//                        TastyToast.makeText(getActivity(), task.getException().getMessage(), TastyToast.LENGTH_LONG, TastyToast.ERROR);
                        // Toast.makeText(emailogin.this, task.getException().getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

    }

    String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z._/]+";

    public boolean isValid() {

        email.setErrorEnabled(false);
        email.setError("");
        pass.setErrorEnabled(false);
        pass.setError("");

        boolean isvalid = false, isvalidemail = false, isvalidpassword = false;
        if (TextUtils.isEmpty(emailid)) {
            dialog.dismiss();
            email.setErrorEnabled(true);
            email.setError("Email is required");
        }
        else {
            if (emailid.matches(emailpattern)) {
                isvalidemail = true;
            } else {
                dialog.dismiss();
                email.setErrorEnabled(true);
                email.setError("Invalid Email Address");
            }
        }
        if (TextUtils.isEmpty(pwd)) {
            dialog.dismiss();
            pass.setErrorEnabled(true);
            pass.setError("Password is Required");
        } else {
            isvalidpassword = true;
        }
        isvalid = (isvalidemail && isvalidpassword) ? true : false;
        return isvalid;
    }

}