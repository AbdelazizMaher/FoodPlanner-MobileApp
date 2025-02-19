package com.example.foodplanner.authentication.signup;

public interface SignUpContract {
    interface IView {
        void showProgress();
        void hideProgress();
        void showSignUpSuccess();
        void showSignUpError(String message);
    }
    interface IPresenter {
        void signUp(String email, String password,  String displayName);
    }
}
