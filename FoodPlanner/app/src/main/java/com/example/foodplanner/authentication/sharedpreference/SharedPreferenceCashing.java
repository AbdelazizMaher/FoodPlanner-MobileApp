package com.example.foodplanner.authentication.sharedpreference;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceCashing {
    private static SharedPreferenceCashing instance = null;
    private SharedPreferences sharedPreferences;
    private static final String SHARED_PREFS_NAME = "USER_PREFS";
    private static final String KEY_USER_ID = "USER_ID";
    private static final String KEY_USER_NAME = "USERNAME";
    private static final String KEY_USER_PHOTO = "USER_PHOTO";

    private SharedPreferenceCashing(Context context) {
        this.sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static void setInstance(Context context) {
        if (instance == null) {
            instance = new SharedPreferenceCashing(context);
        }
    }
    public static SharedPreferenceCashing getInstance() {
        return instance;
    }

    public void cacheUser(String userId, String userName, String userPhoto){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER_ID, userId);
        editor.putString(KEY_USER_NAME, userName);
        editor.putString(KEY_USER_PHOTO, userPhoto);
        editor.apply();
    }

    public void clearUserCache() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public String getUserId() {
        return sharedPreferences.getString(KEY_USER_ID, null);
    }
    public String getUserName() {
        return sharedPreferences.getString(KEY_USER_NAME, null);
    }
    public String getUserPhoto() {
        return sharedPreferences.getString(KEY_USER_PHOTO, null);
    }
}
