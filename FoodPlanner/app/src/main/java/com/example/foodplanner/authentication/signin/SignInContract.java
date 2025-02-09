package com.example.foodplanner.authentication.signin;

public interface SignInContract {
    interface IView {
        void showProgress();
        void hideProgress();
        void showSignInSuccess();
        void showSignInError(String message);
    }
    interface IPresenter {
        void signIn(String email, String password);
    }
}
