package com.example.foodplanner.authentication.signin;

import com.example.foodplanner.repository.authrepository.AuthenticationCallback;
import com.example.foodplanner.repository.authrepository.AuthenticationRepository;
import com.example.foodplanner.database.sharedpreference.SharedPreferenceCashing;
import com.google.firebase.auth.FirebaseUser;

public class SignInPresenter implements SignInContract.IPresenter {
    private SignInContract.IView view;
    private AuthenticationRepository repository;

    public SignInPresenter(SignInContract.IView view, AuthenticationRepository repository) {
        this.view = view;
        this.repository = repository;
    }

    @Override
    public void signIn(String email, String password) {
        view.showProgress();
        repository.signInWithEmail(email, password, new AuthenticationCallback() {
            @Override
            public void onSuccess(FirebaseUser user) {
                view.hideProgress();
                view.showSignInSuccess();
                casheUser(user);
            }

            @Override
            public void onFailure(String errorMessage) {
                view.hideProgress();
                view.showSignInError(errorMessage);
            }
        });
    }

    private void casheUser(FirebaseUser user) {
        SharedPreferenceCashing.getInstance().cacheUser(user.getUid(), user.getDisplayName(), user.getPhotoUrl().toString());
    }
}
