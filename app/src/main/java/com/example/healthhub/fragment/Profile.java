package com.example.healthhub.fragment;

import static android.app.ProgressDialog.show;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.healthhub.LocalStorage;
import com.example.healthhub.R;
import com.example.healthhub.activities.LoginSignupForgot;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.lazyprogrammer.motiontoast.MotionStyle;
import com.lazyprogrammer.motiontoast.MotionToast;


public class Profile extends Fragment {
    View view;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private ImageView imageView;
    private FirebaseUser currentUser;
    private TextView nameTextView, phoneNumberTextView, emailTextView;

    Button edit;
    private static final int PICK_IMAGE_REQUEST = 1;
    Button logout;

    Dialog dialog,dialog2;

    LocalStorage localStorage;

    public Profile() {
        // Required empty public constructor
    }

    private void initViews() {
        localStorage = new LocalStorage(getContext());
        imageView = view.findViewById(R.id.profileimg);
        nameTextView = view.findViewById(R.id.p_name);
        phoneNumberTextView = view.findViewById(R.id.p_mobile);
        emailTextView = view.findViewById(R.id.p_email);
        dialog = new Dialog(getContext());
        dialog2 = new Dialog(getContext());
        logout = view.findViewById(R.id.logoutus);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opendialog();
            }
        });

        edit = view.findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadProfileImage();
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
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        initViews();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        checkProfileImageExists();
        fetchAndSetUserData();
//        setListeners();
        return view;
    }

    public void fetchAndSetUserData() {
        dialog.setContentView(R.layout.loading);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.show();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://healthhub-bf02c-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users").child(userId);

            // Attach a ValueEventListener to retrieve user data
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Handle data retrieval here
                    if (dataSnapshot.exists()) {

                        // Data exists, you can access it
                        String username = dataSnapshot.child("name").getValue(String.class);
                        String phone = dataSnapshot.child("mobile").getValue(String.class);
                        String email = dataSnapshot.child("email").getValue(String.class);

                        if (username != null) {
                            Log.d("username = ", username);
                            nameTextView.setText(username);
                        }
                        else{
                            MotionToast motionToast = new MotionToast(getContext(),
                                    0,
                                    MotionStyle.LIGHT,
                                    MotionStyle.ERROR,
                                    MotionStyle.BOTTOM,
                                    "Unknown Error",
                                    "Please Restart App",
                                    MotionStyle.LENGTH_SHORT)
                                    .show();
                        }

                        if (phone != null) {
                            Log.d("phone = ", phone);
                            phoneNumberTextView.setText(phone);
                        }

                        if (email != null) {
                            Log.d("email = ", email);
                            emailTextView.setText(email);
                        }

                        dialog.dismiss();

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle errors, if any
                    // For example, you can display an error message in a TextView
                    MotionToast motionToast = new MotionToast(getContext(),
                            0,
                            MotionStyle.LIGHT,
                            MotionStyle.ERROR,
                            MotionStyle.BOTTOM,
                            "Unknown Error",
                            databaseError.getMessage(),
                            MotionStyle.LENGTH_SHORT)
                            .show();

                    dialog.dismiss();
                }
            });
        }
    }

    private void opendialog() {
        dialog2.setContentView(R.layout.exit);
        dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog2.setCancelable(false);
        dialog2.show();
        Button btnok = dialog2.findViewById(R.id.logoute);
        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.dismiss();
                Log.d("Logout Button", "Clicked");
                localStorage = new LocalStorage(getContext());
                localStorage.logoutUser();
                MotionToast motionToast = new MotionToast(getContext(),
                        0,
                        MotionStyle.LIGHT,
                        MotionStyle.SUCCESS,
                        MotionStyle.BOTTOM,
                        "Logout Successful",
                        "User have been logged out successfully",
                        MotionStyle.LENGTH_SHORT)
                        .show();
                Intent Z = new Intent(getActivity(), LoginSignupForgot.class);
                startActivity(Z);
                getActivity().finishAffinity();


            }
        });
        Button btncancel = dialog2.findViewById(R.id.cancele);
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.dismiss();

            }
        });
    }

    public void checkProfileImageExists() {
        String userId = currentUser.getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://healthhub-bf02c-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("profileimages").child(userId);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String imageUrl = dataSnapshot.getValue(String.class);
                    Glide.with(getActivity()) // Use 'this' if you're inside an Activity, or use 'getContext()' if you're inside a Fragment
                            .load(imageUrl) // Load the image from the specified URL or file path
                            .centerCrop()
                            .placeholder(R.drawable.user_24) // Placeholder image while loading (optional)
                            .error(R.drawable.user_24) // Error image to display if loading fails (optional)
                            .into(imageView);
                } else {
                    // The child with the specified user ID does not exist
                    // Set the default image in the ImageView
                    imageView.setImageResource(R.drawable.user_24);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors, if any
                // For example, you can display an error message
            }
        });
    }



    private void uploadProfileImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
    }

    // Handle the result of image picker
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            // Call the function to upload and save the image
            uploadImageAndSaveURL(getContext(), imageUri);
        }
    }

    private void uploadImageAndSaveURL(Context context, Uri imageUri) {
        dialog.setContentView(R.layout.loading);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.show();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();
            StorageReference storageReference = FirebaseStorage.getInstance().getReference("profileimages").child(userId);

            // Upload the image to Firebase Storage
            storageReference.putFile(imageUri)
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                // Image upload is successful, now get the download URL
                                storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> downloadUrlTask) {
                                        if (downloadUrlTask.isSuccessful()) {
                                            Uri downloadUri = downloadUrlTask.getResult();

                                            // Save the image URL in the Realtime Database under "profileimages" with the user ID
                                            DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://healthhub-bf02c-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("profileimages");
                                            databaseReference.child(userId).setValue(downloadUri.toString())
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> databaseTask) {
                                                            if (databaseTask.isSuccessful()) {
                                                                dialog.dismiss();
                                                                // Image URL saved successfully
                                                                MotionToast motionToast = new MotionToast(getContext(),
                                                                        0,
                                                                        MotionStyle.LIGHT,
                                                                        MotionStyle.SUCCESS,
                                                                        MotionStyle.BOTTOM,
                                                                        "Success",
                                                                        "Profile Image updated successfully.",
                                                                        MotionStyle.LENGTH_SHORT)
                                                                        .show();
//                                                                Toast.makeText(context, "Profile image uploaded and URL saved", Toast.LENGTH_SHORT).show();
                                                                // Display the uploaded image in an ImageView
                                                                Glide.with(context)
                                                                        .load(downloadUri)
                                                                        .centerCrop()
                                                                        .into(imageView);
                                                            } else {
                                                                dialog.dismiss();
                                                                // Error saving image URL
                                                                MotionToast motionToast = new MotionToast(getContext(),
                                                                        0,
                                                                        MotionStyle.LIGHT,
                                                                        MotionStyle.ERROR,
                                                                        MotionStyle.BOTTOM,
                                                                        "Error",
                                                                        "Error saving image URL",
                                                                        MotionStyle.LENGTH_SHORT)
                                                                        .show();
//                                                                Toast.makeText(context, "Error saving image URL", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                        } else {
                                            dialog.dismiss();
                                            // Error getting download URL
                                            MotionToast motionToast = new MotionToast(getContext(),
                                                    0,
                                                    MotionStyle.LIGHT,
                                                    MotionStyle.ERROR,
                                                    MotionStyle.BOTTOM,
                                                    "Error",
                                                    "Error getting download URL",
                                                    MotionStyle.LENGTH_SHORT)
                                                    .show();
//                                            Toast.makeText(context, "Error getting download URL", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                dialog.dismiss();
                                // Error uploading image
                                MotionToast motionToast = new MotionToast(getContext(),
                                        0,
                                        MotionStyle.LIGHT,
                                        MotionStyle.ERROR,
                                        MotionStyle.BOTTOM,
                                        "Error",
                                        "Error uploading image",
                                        MotionStyle.LENGTH_SHORT)
                                        .show();
//                                Toast.makeText(context, "Error uploading image", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }


}