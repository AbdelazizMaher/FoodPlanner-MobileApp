package com.example.foodplanner.authentication.registration;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.foodplanner.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;

import java.util.Arrays;

public class RegistrationFragment extends Fragment implements RegistrationContract.IView {
    private RegistrationContract.IPresenter presenter;
    private CallbackManager callbackManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new RegistrationPresenter(this);
        callbackManager = CallbackManager.Factory.create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_registration, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.emailSignUpButton).setOnClickListener(v -> presenter.onEmailSignUpClicked());
        view.findViewById(R.id.loginText).setOnClickListener(v -> presenter.onSignInClicked());
        view.findViewById(R.id.googleSignInButton).setOnClickListener(v -> presenter.onGoogleSignInClicked());

        view.findViewById(R.id.facebookSignInButton).setOnClickListener(v -> {
            LoginManager.getInstance().logInWithReadPermissions(getActivity(), Arrays.asList("email", "public_profile"));
        });

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                presenter.handleFacebookSignIn(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {}

            @Override
            public void onError(FacebookException error) {
                showToast("Facebook Login Failed.");
            }
        });

        presenter.checkIfUserIsLoggedIn();
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToSignUp() {
        Navigation.findNavController(requireView()).navigate(R.id.action_registrationFragment_to_signUpFragment);
    }

    @Override
    public void navigateToSignIn() {
        Navigation.findNavController(requireView()).navigate(R.id.action_registrationFragment_to_signInFragment);
    }

    @Override
    public void navigateToHome() {
        Navigation.findNavController(requireView()).navigate(R.id.action_registrationFragment_to_homeFragment2);
    }

    @Override
    public void showLoginError(String error) {
        showToast(error);
    }

    @Override
    public void showGoogleSignInIntent(GoogleSignInClient client) {
        Intent signInIntent = client.getSignInIntent();
        startActivityForResult(signInIntent, 9001);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
