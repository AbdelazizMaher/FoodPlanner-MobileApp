package com.example.foodplanner.profile;


import com.example.foodplanner.authentication.repository.AuthenticationRepository;
import com.example.foodplanner.authentication.sharedpreference.SharedPreferenceCashing;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

public class ProfilePresenter implements ProfileContract.IPresenter {
    private ProfileContract.IView view;
    private AuthenticationRepository repo;
    private FirebaseAuth mAuth;
    private GoogleSignInClient googleSignInClient;

    public ProfilePresenter(ProfileContract.IView view, AuthenticationRepository repo, String clientID) {
        this.view = view;
        this.repo = repo;
        mAuth = FirebaseAuth.getInstance();
        initGoogleSignIn(clientID);
    }

    @Override
    public void loadUserData() {

    }

    @Override
    public void onFoodPreferencesClicked() {

    }

    @Override
    public void onSettingsClicked() {

    }

    @Override
    public void onEditProfileClicked() {

    }

    @Override
    public void onAboutClicked() {
        view.showAboutDialog();
    }

    @Override
    public void onLogoutClicked() {
        view.showLogoutDialog();
    }

    public void confirmLogout() {
        mAuth.signOut();
        googleSignInClient.signOut();
        SharedPreferenceCashing.getInstance().clearUserCache();
        view.navigateToRegistration();
    }

    private void initGoogleSignIn(String clientID) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(clientID)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(FirebaseAuth.getInstance().getApp().getApplicationContext(), gso);
    }
}
