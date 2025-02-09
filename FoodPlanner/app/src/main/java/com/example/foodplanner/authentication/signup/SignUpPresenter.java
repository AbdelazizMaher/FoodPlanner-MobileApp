package com.example.foodplanner.authentication.signup;

import com.example.foodplanner.authentication.repository.AuthenticationCallback;
import com.example.foodplanner.authentication.repository.AuthenticationRepository;
import com.google.firebase.auth.FirebaseUser;

public class SignUpPresenter implements SignUpContract.IPresenter {
    private SignUpContract.IView view;
    private AuthenticationRepository repository;

    public SignUpPresenter(SignUpContract.IView view, AuthenticationRepository repository) {
        this.view = view;
        this.repository = repository;
    }

    @Override
    public void signUp(String email, String password) {
        view.showProgress();
        repository.signUpWithEmail(email, password, new AuthenticationCallback() {
            @Override
            public void onSuccess(FirebaseUser user) {
                view.hideProgress();
                view.showSignUpSuccess();
            }

            @Override
            public void onFailure(String errorMessage) {
                view.hideProgress();
                view.showSignUpError(errorMessage);
            }
        });
    }
}
