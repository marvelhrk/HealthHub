package com.example.healthhub.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.healthhub.R;
import com.example.healthhub.fragment.AIDoctor;
import com.example.healthhub.fragment.Blogs;
import com.example.healthhub.fragment.Diagnose;
import com.example.healthhub.fragment.Hospitals;
import com.example.healthhub.fragment.Profile;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainScreen extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    Hospitals hospitals = new Hospitals();
    Diagnose diagnose = new Diagnose();
    Blogs blogs = new Blogs();
    AIDoctor aiDoctor = new AIDoctor();
    Profile profile = new Profile();

    public static final int HOSPITALS_NAV_ID = R.id.hospitalsnav;
    public static final int DIAGNOSE_NAV_ID = R.id.diagnosenav;
    public static final int BLOGS_NAV_ID = R.id.blogsnav;
    public static final int AIDOC_NAV_ID = R.id.aidocnav;
    public static final int PROFILE_NAV_ID = R.id.profilenav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmnet_layout,blogs).commit();

        bottomNavigationView = findViewById(R.id.bottomnavbar);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == HOSPITALS_NAV_ID) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmnet_layout, hospitals).commit();
                    return true;
                } else if (item.getItemId() == DIAGNOSE_NAV_ID) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmnet_layout, diagnose).commit();
                    return true;
                } else if (item.getItemId() == BLOGS_NAV_ID) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmnet_layout, blogs).commit();
                    return true;
                } else if (item.getItemId() == AIDOC_NAV_ID) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmnet_layout, aiDoctor).commit();
                    return true;
                } else if (item.getItemId() == PROFILE_NAV_ID) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmnet_layout, profile).commit();
                    return true;
                }
                return false;
            }
        });
    }
}