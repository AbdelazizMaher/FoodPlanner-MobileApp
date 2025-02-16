package com.example.foodplanner.authentication.signup;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextUtils;
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

public class SignUpFragment extends Fragment implements SignUpContract.IView {

    private SignUpPresenter presenter;
    private FirebaseAuth mAuth;
    private EditText etDisplayName, etEmail, etPassword, etConfirmPassword;
    private CheckBox checkBoxSubscribe;
    private Button btnNext;
    private ImageView backArrow;
    RelativeLayout progressOverlay;

    public SignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new SignUpPresenter(this, new AuthenticationRepository());
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initUI(view);
        setupWatcher();

        btnNext.setOnClickListener(v -> registerUser());
        backArrow.setOnClickListener(v -> Navigation.findNavController(requireView()).navigate(R.id.action_signUpFragment_to_registrationFragment));
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
    public void showSignUpSuccess() {
        Toast.makeText(getContext(), "Sign-up successful", Toast.LENGTH_SHORT).show();
        Navigation.findNavController(requireView()).navigate(R.id.action_signInFragment_to_homeFragment2);

    }

    @Override
    public void showSignUpError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void initUI(View view) {
        etDisplayName = view.findViewById(R.id.et_display_name);
        etEmail = view.findViewById(R.id.et_email);
        etPassword = view.findViewById(R.id.et_password);
        etConfirmPassword = view.findViewById(R.id.et_confirm_password);
        checkBoxSubscribe = view.findViewById(R.id.checkbox_subscribe);
        btnNext = view.findViewById(R.id.btn_next);
        progressOverlay = view.findViewById(R.id.progress_overlay);
        backArrow = view.findViewById(R.id.back_arrow);
    }

    private void setupWatcher() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };
        etDisplayName.addTextChangedListener(textWatcher);
        etEmail.addTextChangedListener(textWatcher);
        etPassword.addTextChangedListener(textWatcher);
        etConfirmPassword.addTextChangedListener(textWatcher);
    }

    private void registerUser() {
        String displayName = etDisplayName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(displayName) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(getContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        presenter.signUp(email, password);
    }

    private void checkInputs() {
        String displayName = etDisplayName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        boolean isEnabled = !TextUtils.isEmpty(displayName) &&
                !TextUtils.isEmpty(email) &&
                !TextUtils.isEmpty(password) &&
                !TextUtils.isEmpty(confirmPassword) &&
                password.equals(confirmPassword);

        btnNext.setEnabled(isEnabled);

        if (isEnabled) {
            btnNext.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.primaryColor));
        } else {
            btnNext.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.gray));
        }
    }

}