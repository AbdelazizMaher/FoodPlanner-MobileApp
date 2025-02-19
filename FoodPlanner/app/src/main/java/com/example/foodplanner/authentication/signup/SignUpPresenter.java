package com.example.foodplanner.authentication.signup;

import com.example.foodplanner.repository.authrepository.AuthenticationCallback;
import com.example.foodplanner.repository.authrepository.AuthenticationRepository;
import com.example.foodplanner.database.sharedpreference.SharedPreferenceCashing;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignUpPresenter implements SignUpContract.IPresenter {
    private SignUpContract.IView view;
    private AuthenticationRepository repository;

    public SignUpPresenter(SignUpContract.IView view, AuthenticationRepository repository) {
        this.view = view;
        this.repository = repository;
    }

    @Override
    public void signUp(String email, String password, String displayName) {
        view.showProgress();
        repository.signUpWithEmail(email, password, new AuthenticationCallback() {
            @Override
            public void onSuccess(FirebaseUser user) {
                if (user != null) {
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(displayName)
                            .build();

                    user.updateProfile(profileUpdates).addOnCompleteListener(task -> {
                        view.hideProgress();

                        if (task.isSuccessful()) {
                            casheUser(user, displayName);
                            view.showSignUpSuccess();
                        } else {
                            view.showSignUpError("Sign-up successful, but failed to update display name.");
                        }
                    });
                } else {
                    view.hideProgress();
                    view.showSignUpError("User creation failed.");
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                view.hideProgress();
                view.showSignUpError(errorMessage);
            }
        });
    }


    private void casheUser(FirebaseUser user, String displayName) {
        SharedPreferenceCashing.getInstance().cacheUser(user.getUid(),displayName);
    }
}
