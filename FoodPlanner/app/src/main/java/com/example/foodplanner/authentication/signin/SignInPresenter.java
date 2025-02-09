package com.example.foodplanner.authentication.signin;

import com.example.foodplanner.authentication.repository.AuthenticationCallback;
import com.example.foodplanner.authentication.repository.AuthenticationRepository;
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
            }

            @Override
            public void onFailure(String errorMessage) {
                view.hideProgress();
                view.showSignInError(errorMessage);
            }
        });
    }
}
