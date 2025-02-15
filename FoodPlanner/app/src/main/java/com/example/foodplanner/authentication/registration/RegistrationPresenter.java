package com.example.foodplanner.authentication.registration;


import com.example.foodplanner.authentication.repository.AuthenticationCallback;
import com.example.foodplanner.authentication.repository.AuthenticationRepository;
import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegistrationPresenter implements RegistrationContract.IPresenter {
    private RegistrationContract.IView view;
    private AuthenticationRepository repository;
    private FirebaseAuth mAuth;
    private GoogleSignInClient googleSignInClient;
    public static String userID;

    public RegistrationPresenter(RegistrationContract.IView view) {
        this.view = view;
        this.repository = new AuthenticationRepository();
        this.mAuth = FirebaseAuth.getInstance();
        initGoogleSignIn();
    }

    private void initGoogleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("YOUR_CLIENT_ID")
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(FirebaseAuth.getInstance().getApp().getApplicationContext(), gso);
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
        view.showGoogleSignInIntent(googleSignInClient);
    }

    @Override
    public void handleGoogleSignInResult(String idToken) {
        repository.signInWithGoogle(idToken, new AuthenticationCallback() {
            @Override
            public void onSuccess(FirebaseUser user) {
                view.navigateToHome();
            }

            @Override
            public void onFailure(String errorMessage) {
                view.showLoginError(errorMessage);
            }
        });
    }

    @Override
    public void handleFacebookSignIn(AccessToken token) {
        repository.signInWithFacebook(token, new AuthenticationCallback() {
            @Override
            public void onSuccess(FirebaseUser user) {
                view.navigateToHome();
            }

            @Override
            public void onFailure(String errorMessage) {
                view.showLoginError(errorMessage);
            }
        });
    }

    @Override
    public void checkIfUserIsLoggedIn() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            userID = currentUser.getUid();
            view.navigateToHome();
        }
    }
}
