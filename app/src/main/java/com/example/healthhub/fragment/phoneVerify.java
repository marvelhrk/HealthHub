package com.example.healthhub.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthhub.LocalStorage;
import com.example.healthhub.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.lazyprogrammer.motiontoast.MotionStyle;
import com.lazyprogrammer.motiontoast.MotionToast;

import java.util.concurrent.TimeUnit;

public class phoneVerify extends Fragment {
    String verificationId;
    FirebaseAuth FAuth;
    Button verify , Resend ;
    TextView txt;
    EditText entercode;
    String phoneno;
    Dialog dialog,dialog2;
    LocalStorage localStorage;
    private static FragmentManager fragmentManager;
    private static View view;
    private void initViews() {
        fragmentManager = getActivity().getSupportFragmentManager();
        localStorage = new LocalStorage(getContext());
        phoneno = localStorage.getPhoneNumber().trim();
        Log.d("verifyPhone  ", phoneno);

        entercode = view.findViewById(R.id.code);
        txt = view.findViewById(R.id.text);
        Resend = view.findViewById(R.id.Resendotp);
        verify = view.findViewById(R.id.Verify);
        FAuth = FirebaseAuth.getInstance();
        dialog = new Dialog(getContext());
        dialog2 = new Dialog(getContext());
        Resend.setVisibility(View.INVISIBLE);
        txt.setVisibility(View.INVISIBLE);

        sendverificationcode(phoneno);

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String code = entercode.getText().toString().trim();
                Resend.setVisibility(View.INVISIBLE);

                if (code.isEmpty() && code.length()<6){
                    entercode.setError("Enter code");
                    entercode.requestFocus();
                    return;
                }
                verifyCode(code);
            }
        });

        new CountDownTimer(60000,1000){

            @Override
            public void onTick(long millisUntilFinished) {

                txt.setVisibility(View.VISIBLE);
                txt.setText("Resend Code After "+millisUntilFinished/1000+" Seconds");

            }

            /**
             * Callback fired when the time is up.
             */
            @Override
            public void onFinish() {
                Resend.setVisibility(View.VISIBLE);
                txt.setVisibility(View.INVISIBLE);

            }
        }.start();

        Resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Resend.setVisibility(View.INVISIBLE);
                Resendotp(phoneno);

                new CountDownTimer(60000,1000){

                    @Override
                    public void onTick(long millisUntilFinished) {

                        txt.setVisibility(View.VISIBLE);
                        txt.setText("Resend Code After "+millisUntilFinished/1000+" seconds");

                    }

                    /**
                     * Callback fired when the time is up.
                     */
                    @Override
                    public void onFinish() {
                        Resend.setVisibility(View.VISIBLE);
                        txt.setVisibility(View.INVISIBLE);

                    }
                }.start();
            }
        });

    }
    public phoneVerify() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_phone_verify, container, false);
        initViews();
//        setListeners();
        return view;
    }

    private void sendverificationcode(String number) {

        PhoneAuthProvider.verifyPhoneNumber(
                PhoneAuthOptions
                        .newBuilder(FirebaseAuth.getInstance())
                        .setActivity(requireActivity())
                        .setPhoneNumber(number)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setCallbacks(mcallBack)
                        .build()
        );
    }

    private void Resendotp(String phonenum) {

        sendverificationcode(phonenum);
    }



    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mcallBack=new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            String code = phoneAuthCredential.getSmsCode();
            if(code != null){
                entercode.setText(code);  // Auto Verification
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            MotionToast motionToast = new MotionToast(getContext(),
                    0,
                    MotionStyle.LIGHT,
                    MotionStyle.ERROR,
                    MotionStyle.BOTTOM,
                    "Error",
                    e.getMessage(),
                    MotionStyle.LENGTH_SHORT)
                    .show();
//            Toast.makeText(phoneverify.this , e.getMessage(),Toast.LENGTH_LONG).show();

        }

        @Override
        public void onCodeSent(String s , PhoneAuthProvider.ForceResendingToken forceResendingToken){
            super.onCodeSent(s,forceResendingToken);

            verificationId = s;

        }
    };

    private void verifyCode(String code) {

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId , code);
        linkCredential(credential);
        dialog2.setContentView(R.layout.loading);
        dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog2.setCancelable(false);
        dialog2.show();
       /* final ProgressDialog mDialog = new ProgressDialog(phoneverify.this);
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setMessage("Verifying Code......");
        mDialog.show();*/
    }

    private void linkCredential(PhoneAuthCredential credential) {

        FAuth.getCurrentUser().linkWithCredential(credential)
                .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful())
                        {dialog2.dismiss();
                            opendialog();

                            /*AlertDialog.Builder builder = new AlertDialog.Builder(phoneverify.this);
                            builder.setMessage("\n    Your Number is Verified. ");
                            builder.setCancelable(false);
                            builder.setPositiveButton("Next", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    dialog.dismiss();
                                    Intent b = new Intent(phoneverify.this,mailverify.class);
                                    startActivity(b);
                                }
                            });
                            AlertDialog Alert = builder.create();
                            Alert.show();*/
                        }
                        else{
                            dialog2.dismiss();
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
//                            TastyToast.makeText(getContext(), task.getException().getMessage(), TastyToast.LENGTH_LONG, TastyToast.ERROR);
                            fragmentManager
                                    .beginTransaction()
                                    .replace(R.id.frameContainer,
                                            new EmailLogin(),
                                            "EmailLogin").commit();
//                            Intent b = new Intent(phoneverify.this,mainmenu.class);
                        }
                    }
                });

    }
    private void opendialog() {
        MotionToast motionToast = new MotionToast(getContext(),
                0,
                MotionStyle.LIGHT,
                MotionStyle.SUCCESS,
                MotionStyle.BOTTOM,
                "Step 2 Completed",
                "Next Step, Verify Email",
                MotionStyle.LENGTH_SHORT)
                .show();

        fragmentManager
                .beginTransaction()
                .replace(R.id.frameContainer,
                        new mailVerification(),
                        "mailVerification").commit();
    }
//        dialog.setContentView(R.layout.otpverified);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        dialog.setCancelable(false);
//        dialog.show();
//        Button btnok = dialog.findViewById(R.id.lesgotp);
//        btnok.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                dialog.dismiss();
//                Intent b = new Intent(phoneverify.this,mailverify.class);
//                startActivity(b);
//
//            }
//        });



}