package com.example.foodplanner.authentication.signin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.foodplanner.R;
import com.example.foodplanner.authentication.repository.AuthenticationRepository;
import com.google.firebase.auth.FirebaseAuth;


public class SignInFragment extends Fragment implements SignInContract.IView {

    private SignInPresenter presenter;
    private FirebaseAuth mAuth;
    private EditText etEmail, etPassword;
    private Button btnNext;
    private CheckBox checkBoxSubscribe;
    private ImageView backArrow;
    RelativeLayout progressOverlay;

    public SignInFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new SignInPresenter(this, new AuthenticationRepository());
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etEmail = view.findViewById(R.id.et_email);
        etPassword = view.findViewById(R.id.et_password);
        btnNext = view.findViewById(R.id.btn_next);
        checkBoxSubscribe = view.findViewById(R.id.checkbox_subscribe);
        progressOverlay = view.findViewById(R.id.progress_overlay);
        backArrow = view.findViewById(R.id.back_arrow);

        backArrow.setOnClickListener(v -> {
            Navigation.findNavController(requireView()).navigate(R.id.action_signInFragment_to_registrationFragment);
        });

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        };

        etEmail.addTextChangedListener(textWatcher);
        etPassword.addTextChangedListener(textWatcher);

        btnNext.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            presenter.signIn(email, password);
        });
    }


    @Override
    public void showProgress() {
        progressOverlay.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressOverlay.setVisibility(View.GONE);
    }

    @Override
    public void showSignInSuccess() {
        Toast.makeText(getContext(), "Sign-in successful", Toast.LENGTH_SHORT).show();
        Navigation.findNavController(requireView()).navigate(R.id.action_signInFragment_to_homeFragment2);
    }

    @Override
    public void showSignInError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void checkInputs() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (!email.isEmpty() && !password.isEmpty()) {
            btnNext.setEnabled(true);
            btnNext.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.primaryColor));
        } else {
            btnNext.setEnabled(false);
            btnNext.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.gray));
        }
    }
}