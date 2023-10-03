package com.example.healthhub.fragment;

import android.app.Dialog;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthhub.LocalStorage;
import com.example.healthhub.R;
import com.example.healthhub.activities.LoginSignupForgot;
import com.example.healthhub.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.hbb20.CountryCodePicker;
import com.lazyprogrammer.motiontoast.MotionStyle;
import com.lazyprogrammer.motiontoast.MotionToast;

import java.util.HashMap;

public class Signup extends Fragment {
    private static View view;
    private static TextInputLayout Fname, Email, mobileno, Pass,cpass;
    private static TextView login;
    private static Button signUpButton;
    CountryCodePicker Cpp;
    FirebaseAuth Fauth,mAuth;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    String fname,lname,emailid,password,confpassword,mobile;
    String role="Customer";
    Dialog dialog,dialog2;
    User user;
    LocalStorage localStorage;
    Gson gson;

    private static FragmentManager fragmentManager;
    public Signup() {
        // Required empty public constructor
    }

    private void initViews() {
        localStorage = new LocalStorage(getContext());
        fragmentManager = getActivity().getSupportFragmentManager();
        Fname = view.findViewById(R.id.Firstname);
        Email = view.findViewById(R.id.Emailid);
        mobileno = view.findViewById(R.id.Mobileno);
        Pass = view.findViewById(R.id.Pwd);
        cpass = view.findViewById(R.id.Cpass);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance("https://healthhub-bf02c-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users");

        signUpButton = view.findViewById(R.id.registersu);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog2.setContentView(R.layout.loading);
                dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog2.setCancelable(false);
                dialog2.show();
                signUp();
//                checker();
            }
        });
        login = view.findViewById(R.id.email);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new LoginSignupForgot().replaceLoginFragment();
            }
        });
//        progressDialog = new ProgressDialog(getContext());
        dialog = new Dialog (getContext());
        dialog2 = new Dialog (getContext());

     /*   progressBar = new ProgressBar(this);
        progressBar = (ProgressBar)findViewById(R.id.spin_kit);
        Sprite doubleBounce = new DoubleBounce();
        progressBar.setVisibility(View.INVISIBLE);*/

        Cpp = view.findViewById(R.id.CountryCode);

        Fauth = FirebaseAuth.getInstance();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_signup, container, false);
        initViews();
//        setListeners();
        return view;
    }

//    private void checker(){
//        fname = Fname.getEditText().getText().toString().trim();
//        emailid = Email.getEditText().getText().toString().trim();
//        mobile = mobileno.getEditText().getText().toString().trim();
//        password = Pass.getEditText().getText().toString().trim();
//        confpassword = cpass.getEditText().getText().toString().trim();
//
//
//        if (isValid()){
//            Log.d("SignupActivity", "Valid check sucessfull");
//            MotionToast motionToast = new MotionToast(getContext(),
//                    0,
//                    MotionStyle.LIGHT,
//                    MotionStyle.WARNING,
//                    MotionStyle.BOTTOM,
//                    "Loading",
//                    "........",
//                    MotionStyle.LENGTH_SHORT)
//                    .show();
////            TastyToast.makeText(getActivity(), "Loading !", TastyToast.LENGTH_LONG, TastyToast.WARNING);
////            dialog2.setContentView(R.layout.progress);
////            dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
////            dialog2.setCancelable(false);
////            dialog2.show();
//
//                /*    progressBar.setVisibility(View.VISIBLE);
//                    progressBar.setIndeterminateDrawable(doubleBounce);
//                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
//                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);*/
//
//
//                 /*   final ProgressDialog mDialog = new ProgressDialog(registration.this);
//                    mDialog.setCancelable(false);
//                    mDialog.setCanceledOnTouchOutside(false);
//                    mDialog.setMessage("Registration in progress......");
//                    mDialog.show();*/
//
//            Fauth.createUserWithEmailAndPassword(emailid,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                @Override
//                public void onComplete(@NonNull Task<AuthResult> task) {
//
//                    if (task.isSuccessful()){
//                        Log.d("SignupActivity", "task succesfull");
//
//                        String useridd = FirebaseAuth.getInstance().getCurrentUser().getUid();
//                        databaseReference = FirebaseDatabase.getInstance().getReference("User").child(useridd);
//                        final HashMap<String , String> hashMap = new HashMap<>();
//                        hashMap.put("Role",role);
//                        databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                Log.d("SignupActivity", "oncomplete successful");
//                                User user = new User(useridd, fname, emailid, mobile, password);
////                                HashMap<String , String> hashMap1 = new HashMap<>();
////                                hashMap1.put("Mobile No",mobile);
////                                hashMap1.put("First Name",fname);
////                                hashMap1.put("Last Name",lname);
////                                hashMap1.put("EmailId",emailid);
////                                hashMap1.put("Password",password);
////                                hashMap1.put("Confirm Password",confpassword);
//
//
//                                FirebaseDatabase.getInstance().getReference("Customer")
//                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                                        .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                            @Override
//                                            public void onComplete(@NonNull Task<Void> task) {
//                                                //mDialog.dismiss();
//                                                // progressBar.setVisibility(View.INVISIBLE);
//                                                Log.d("SignupActivity", "reference set in user successful");
//                                                Fauth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                    @Override
//                                                    public void onComplete(@NonNull Task<Void> task) {
//
//                                                        if(task.isSuccessful()){
//                                                            Log.d("SignupActivity", "Signup successful");
//                                                                MotionToast motionToast = new MotionToast(getContext(),
//                                                                0,
//                                                                MotionStyle.LIGHT,
//                                                                MotionStyle.SUCCESS,
//                                                                MotionStyle.BOTTOM,
//                                                                "Done",
//                                                                "Details stored Successfully",
//                                                                MotionStyle.LENGTH_SHORT)
//                                                                .show();
////                                                            TastyToast.makeText(getActivity(), "Done", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
////                                                            new LoginSignupForgot().replaceLoginFragment();
////                                                    dialog2.dismiss();
////                                                    opendialog();
//
//                                                            /*AlertDialog.Builder builder = new AlertDialog.Builder(registration.this);
//                                                            builder.setMessage( "    Your Registration is almost Complete.\n    Next Step, Verify Your Phone. ");
//                                                            builder.setCancelable(false);
//                                                            builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
//                                                                @Override
//                                                                public void onClick(DialogInterface dialog, int which) {
//
//                                                                    dialog.dismiss();
//                                                                    String phonenumber = Cpp.getSelectedCountryCodeWithPlus() + mobile;
//                                                                    Intent b = new Intent(registration.this,phoneverify.class);
//                                                                    b.putExtra("phonenumber",phonenumber);
//                                                                    startActivity(b);
//
//                                                                }
//                                                            });
//                                                            AlertDialog Alert = builder.create();
//                                                            Alert.show();*/
//                                                        }
//                                                        else{
//                                                            Log.d("SignupActivity", "Signup Failed");
//                                                            dialog2.dismiss();
//                                                            // mDialog.dismiss();
//                                                            MotionToast motionToast = new MotionToast(getContext(),
//                                                                    0,
//                                                                    MotionStyle.LIGHT,
//                                                                    MotionStyle.ERROR,
//                                                                    MotionStyle.BOTTOM,
//                                                                    "Error",
//                                                                    task.getException().getMessage(),
//                                                                    MotionStyle.LENGTH_SHORT)
//                                                                    .show();
////                                                            TastyToast.makeText(getActivity(), task.getException().getMessage(), TastyToast.LENGTH_LONG, TastyToast.ERROR);
//                                                        }
//                                                    }
//                                                });
//
//                                            }
//                                        });
//
//                            }
//                        });
//                    }
//                    else {
//                        dialog2.dismiss();
//                        // mDialog.dismiss();
//                        Log.d("SignupActivity", "Task falied");
//                        MotionToast motionToast = new MotionToast(getContext(),
//                                0,
//                                MotionStyle.LIGHT,
//                                MotionStyle.ERROR,
//                                MotionStyle.BOTTOM,
//                                "Error",
//                                task.getException().getMessage(),
//                                MotionStyle.LENGTH_LONG)
//                                .show();
////                        TastyToast.makeText(getActivity(), task.getException().getMessage(), TastyToast.LENGTH_LONG, TastyToast.ERROR);
//
//                        // Toast.makeText(emailogin.this, task.getException().getMessage(),Toast.LENGTH_LONG).show();
//                    }
//                }
//            });
//        }
//    }

    private void signUp() {
        fname = Fname.getEditText().getText().toString().trim();
        emailid = Email.getEditText().getText().toString().trim();
        mobile = mobileno.getEditText().getText().toString().trim();
        password = Pass.getEditText().getText().toString().trim();
        confpassword = cpass.getEditText().getText().toString().trim();

        final String name = Fname.getEditText().getText().toString().trim();
        final String email = Email.getEditText().getText().toString().trim();
        final String password = Pass.getEditText().getText().toString().trim();
        final String phoneNumber = mobileno.getEditText().getText().toString().trim();

        if(isValid()){
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Signup success, update user profile and send verification email
                                FirebaseUser user = mAuth.getCurrentUser();
                                if (user != null) {
                                    user.updateProfile(new UserProfileChangeRequest.Builder()
                                                    .setDisplayName(name)
                                                    .build())
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> profileTask) {

                                                    if (profileTask.isSuccessful()) {
                                                        sendEmailVerification();
                                                    } else {
                                                        dialog2.dismiss();
                                                        MotionToast motionToast = new MotionToast(getContext(),
                                                                    0,
                                                                    MotionStyle.LIGHT,
                                                                    MotionStyle.ERROR,
                                                                    MotionStyle.BOTTOM,
                                                                    "Error",
                                                                "Failed to update user profile.",
                                                                    MotionStyle.LENGTH_SHORT)
                                                                    .show();
//                                                        Toast.makeText(getActivity(), "Failed to update user profile.", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                }
                            } else {
                                // Signup failed
                                dialog2.dismiss();
                                MotionToast motionToast = new MotionToast(getContext(),
                                        0,
                                        MotionStyle.LIGHT,
                                        MotionStyle.ERROR,
                                        MotionStyle.BOTTOM,
                                        "Signup failed",
                                        task.getException().getMessage(),
                                        MotionStyle.LENGTH_SHORT)
                                        .show();
//                                Toast.makeText(getActivity(), "Signup failed. " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else{
            dialog2.dismiss();
        }
    }

    private void sendEmailVerification() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            user.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> emailTask) {
                            if (emailTask.isSuccessful()) {
                                // Verification email sent successfully
                                // Store user data in Firebase Realtime Database
                                storeUserData();
                                dialog2.dismiss();
                                MotionToast motionToast = new MotionToast(getContext(),
                                        0,
                                        MotionStyle.LIGHT,
                                        MotionStyle.SUCCESS,
                                        MotionStyle.BOTTOM,
                                        "Step 1 Complete",
                                        "Continue",
                                        MotionStyle.LENGTH_SHORT)
                                        .show();


                                String phonenumber = Cpp.getSelectedCountryCodeWithPlus() + mobile;
                                localStorage.setPhoneNumber(phonenumber);


                                fragmentManager
                                        .beginTransaction()
                                        .replace(R.id.frameContainer,
                                                new phoneVerify(),
                                                "phoneVerify").commit();

//                                Toast.makeText(getActivity(), "Verification email sent.", Toast.LENGTH_SHORT).show();
                            } else {
                                dialog2.dismiss();
                                MotionToast motionToast = new MotionToast(getContext(),
                                        0,
                                        MotionStyle.LIGHT,
                                        MotionStyle.ERROR,
                                        MotionStyle.BOTTOM,
                                        "Error",
                                        "Failed to send verification email.",
                                        MotionStyle.LENGTH_SHORT)
                                        .show();
//                                Toast.makeText(getActivity(), "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void storeUserData() {
        String userId = mAuth.getCurrentUser().getUid();
        String name = Fname.getEditText().getText().toString().trim();
        String email = Email.getEditText().getText().toString().trim();
        String phoneNumber = mobileno.getEditText().getText().toString().trim();
        String password = Pass.getEditText().getText().toString().trim();

        User user = new User(userId, name, email, phoneNumber,password);
        databaseReference.child(userId).setValue(user);

    }
    String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z._-]+";
    public boolean isValid(){
        Email.setErrorEnabled(false);
        Email.setError("");
        Fname.setErrorEnabled(false);
        Fname.setError("");
        Pass.setErrorEnabled(false);
        Pass.setError("");
        mobileno.setErrorEnabled(false);
        mobileno.setError("");
        cpass.setErrorEnabled(false);
        cpass.setError("");


        boolean isValid = false,isValidname=false,isValidemail=false,isValidpassword=false,isValidconfpassword=false,isValidmobilenum=false;
        if(TextUtils.isEmpty(fname)){
            dialog2.dismiss();
            Fname.setErrorEnabled(true);
            Fname.setError("Enter First Name");
        }else{
            isValidname = true;
        }
        if(TextUtils.isEmpty(emailid)){
            dialog2.dismiss();
            Email.setErrorEnabled(true);
            Email.setError("Email Is Required");
        }else{
            if(emailid.matches(emailpattern)){
                isValidemail = true;
            }else{
                dialog2.dismiss();
                Email.setErrorEnabled(true);
                Email.setError("Enter a Valid Email Id");
            }
        }
        if(TextUtils.isEmpty(password)){
            dialog2.dismiss();
            Pass.setErrorEnabled(true);
            Pass.setError("Enter Password");
        }else{
            if(password.length()<8){
                dialog2.dismiss();
                Pass.setErrorEnabled(true);
                Pass.setError("Password is Weak.\nA Strong Password must contain Alphabets, Numbers and Special Characters.");
            }else{
                isValidpassword = true;
            }
        }
        if(TextUtils.isEmpty(confpassword)){
            dialog2.dismiss();
            cpass.setErrorEnabled(true);
            cpass.setError("Enter Password Again");
        }else{
            if(!password.equals(confpassword)){
                dialog2.dismiss();
                cpass.setErrorEnabled(true);
                cpass.setError("Password Dosen't Match");
            }else{
                isValidconfpassword = true;
            }
        }
        if(TextUtils.isEmpty(mobile)){
            dialog2.dismiss();
            mobileno.setErrorEnabled(true);
            mobileno.setError("Mobile Number Is Required");
        }else{
            if(mobile.length()<10){
                dialog2.dismiss();
                mobileno.setErrorEnabled(true);
                mobileno.setError("Invalid Mobile Number");
            }
            else if(mobile.length()>10){
                dialog2.dismiss();
                mobileno.setErrorEnabled(true);
                mobileno.setError("Invalid Mobile Number");
            }
            else{
                isValidmobilenum = true;
            }
        }


        isValid = ( isValidconfpassword && isValidpassword  && isValidemail && isValidmobilenum && isValidname ) ? true : false;
        return isValid;


    }

}