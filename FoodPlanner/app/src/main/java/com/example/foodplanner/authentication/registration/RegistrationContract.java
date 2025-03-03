package com.example.foodplanner.authentication.registration;

import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;

public interface RegistrationContract {
    interface IView {
        void showProgress();
        void hideProgress();
        void showToast(String message);
        void navigateToSignUp();
        void navigateToSignIn();
        void navigateToHome();
        void showLoginError(String error);
        void showGoogleSignInIntent(GoogleSignInClient client);
    }

    interface IPresenter {
        void onSkipClicked();
        void onEmailSignUpClicked();
        void onSignInClicked();
        void onGoogleSignInClicked();
        void handleGoogleSignInResult(String idToken);
        void handleFacebookSignIn(AccessToken token);
    }
}
