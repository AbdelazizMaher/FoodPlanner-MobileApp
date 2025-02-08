package com.example.foodplanner.authentication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.foodplanner.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpFragment extends Fragment {

    private FirebaseAuth mAuth;
    private EditText etDisplayName, etEmail, etPassword, etConfirmPassword;
    private CheckBox checkBoxSubscribe;
    private Button btnNext;
    RelativeLayout progressOverlay;

    public SignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        mAuth = FirebaseAuth.getInstance();

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

        etDisplayName = view.findViewById(R.id.et_display_name);
        etEmail = view.findViewById(R.id.et_email);
        etPassword = view.findViewById(R.id.et_password);
        etConfirmPassword = view.findViewById(R.id.et_confirm_password);
        checkBoxSubscribe = view.findViewById(R.id.checkbox_subscribe);
        btnNext = view.findViewById(R.id.btn_next);
        progressOverlay = view.findViewById(R.id.progress_overlay);

        etDisplayName.addTextChangedListener(textWatcher);
        etEmail.addTextChangedListener(textWatcher);
        etPassword.addTextChangedListener(textWatcher);
        etConfirmPassword.addTextChangedListener(textWatcher);

        btnNext.setOnClickListener(v -> registerUser());
    }

    private void createAccount(String email, String password) {
        progressOverlay.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressOverlay.setVisibility(View.GONE);
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            progressOverlay.setVisibility(View.GONE);
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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

        createAccount(email,password);
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

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
    }
}