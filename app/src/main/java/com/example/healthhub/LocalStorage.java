package com.example.healthhub;

import android.content.Context;
import android.content.SharedPreferences;

public class LocalStorage {
    public static final String KEY_HASH = "hash";

    public static final String KEY_USER = "User";

    public static final String KEY_Phone_Number = null;

    private static final String IS_USER_LOGIN = "IsUserLoggedIn";

    public static final String dburl = "https://healthhub-bf02c-default-rtdb.asia-southeast1.firebasedatabase.app/";


    private static LocalStorage instance = null;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    int PRIVATE_MODE = 0;
    Context _context;

    public LocalStorage(Context context) {
        sharedPreferences = context.getSharedPreferences("Preferences", 0);
    }

    public static LocalStorage getInstance(Context context) {
        if (instance == null) {
            synchronized (LocalStorage.class) {
                if (instance == null) {
                    instance = new LocalStorage(context);
                }
            }
        }
        return instance;
    }

    public void createUserLoginSession(String user) {
        editor = sharedPreferences.edit();
        editor.putBoolean(IS_USER_LOGIN, true);
        editor.putString(KEY_USER, user);
        editor.commit();
    }

    public String getUserLogin() {
        return sharedPreferences.getString(KEY_USER, "");
    }


    public void logoutUser() {
        editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    public boolean checkLogin() {
        // Check login status
        return !this.isUserLoggedIn();
    }


    public boolean isUserLoggedIn() {
        return sharedPreferences.getBoolean(IS_USER_LOGIN, false);
    }


    public String getPhoneNumber() {
        if (sharedPreferences.contains(KEY_Phone_Number))
            return sharedPreferences.getString(KEY_Phone_Number, null);
        else return null;
    }
    public void setPhoneNumber(String phonenumber) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_Phone_Number, phonenumber);
        editor.commit();
    }

    public String geturl(){
        return dburl;
    }


}

