package com.example.foodplanner.authentication.registration;


import com.example.foodplanner.repository.authrepository.AuthenticationCallback;
import com.example.foodplanner.repository.authrepository.AuthenticationRepository;
import com.example.foodplanner.database.sharedpreference.SharedPreferenceCashing;
import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegistrationPresenter implements RegistrationContract.IPresenter {
    private RegistrationContract.IView view;
    private AuthenticationRepository repository;
    private GoogleSignInClient googleSignInClient;

    public RegistrationPresenter(RegistrationContract.IView view, String clientID) {
        this.view = view;
        this.repository = new AuthenticationRepository();
        initGoogleSignIn(clientID);
    }

    private void initGoogleSignIn(String clientID) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(clientID)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(FirebaseAuth.getInstance().getApp().getApplicationContext(), gso);
    }

    @Override
    public void onSkipClicked() {
        view.navigateToHome();
    }

    @Override
    public void onEmailSignUpClicked() {
        view.navigateToSignUp();
    }

    @Override
    public void onSignInClicked() {
        view.navigateToSignIn();
    }

    @Override
    public void onGoogleSignInClicked() {
        view.showProgress();
        view.showGoogleSignInIntent(googleSignInClient);
    }

    @Override
    public void handleGoogleSignInResult(String idToken) {
        view.showProgress();
        repository.signInWithGoogle(idToken, new AuthenticationCallback() {
            @Override
            public void onSuccess(FirebaseUser user) {
                view.hideProgress();
                casheUser(user);
                view.navigateToHome();
            }

            @Override
            public void onFailure(String errorMessage) {
                view.hideProgress();
                view.showLoginError(errorMessage);
            }
        });
    }

    @Override
    public void handleFacebookSignIn(AccessToken token) {
        repository.signInWithFacebook(token, new AuthenticationCallback() {
            @Override
            public void onSuccess(FirebaseUser user) {
                casheUser(user);
                view.navigateToHome();
            }

            @Override
            public void onFailure(String errorMessage) {
                view.showLoginError(errorMessage);
            }
        });
    }


    private void casheUser(FirebaseUser user) {
        SharedPreferenceCashing.getInstance().cacheUser(user.getUid(), user.getDisplayName());
    }
}
