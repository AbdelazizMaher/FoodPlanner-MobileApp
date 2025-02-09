package com.example.foodplanner.authentication;

import com.google.firebase.auth.FirebaseUser;

public interface AuthenticationCallback {
    void onSuccess(FirebaseUser user);
    void onFailure(String errorMessage);
}
