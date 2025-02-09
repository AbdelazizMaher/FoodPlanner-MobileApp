package com.example.foodplanner.authentication;

import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class AuthenticationRepository {
    private final FirebaseAuth mAuth;

    public AuthenticationRepository() {
        this.mAuth = FirebaseAuth.getInstance();
    }

    public void signInWithGoogle(String token, AuthenticationCallback callback) {
        AuthCredential credential = GoogleAuthProvider.getCredential(token, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess(mAuth.getCurrentUser());
                    } else {
                        callback.onFailure("Google sign-in failed.");
                    }
                });
    }

    public void signInWithFacebook(AccessToken token, AuthenticationCallback callback) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess(mAuth.getCurrentUser());
                    } else {
                        callback.onFailure("Facebook authentication failed.");
                    }
                });
    }
}
