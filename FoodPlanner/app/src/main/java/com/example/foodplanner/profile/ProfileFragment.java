package com.example.foodplanner.profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.foodplanner.R;
import com.example.foodplanner.authentication.repository.AuthenticationRepository;
import com.example.foodplanner.database.MealLocalDataSource;
import com.example.foodplanner.home.HomePresenter;
import com.example.foodplanner.model.MealRepository;
import com.example.foodplanner.network.api.MealRemoteApiDataSource;
import com.example.foodplanner.network.sync.MealRemoteSyncDataSource;

public class ProfileFragment extends Fragment implements ProfileContract.IView {

    private ImageView settingsNav, profileNav, aboutNav, logoutNav, backToHome;
    private ProfilePresenter presenter;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        presenter = new ProfilePresenter(this, new AuthenticationRepository(), getString(R.string.client_id));

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        settingsNav = view.findViewById(R.id.settingsNav);
        profileNav = view.findViewById(R.id.profileNav);
        aboutNav = view.findViewById(R.id.aboutNav);
        logoutNav = view.findViewById(R.id.logoutNav);
        backToHome = view.findViewById(R.id.backToHome);

        backToHome.setOnClickListener(v -> {
            Navigation.findNavController(requireView()).navigateUp();
        });

        settingsNav.setOnClickListener(v -> {

        });

        profileNav.setOnClickListener(v -> {

        });

        aboutNav.setOnClickListener(v -> {
            presenter.onAboutClicked();
        });

        logoutNav.setOnClickListener(v -> {
            presenter.onLogoutClicked();
        });

    }

    @Override
    public void showUserName(String name) {

    }

    @Override
    public void showProfileImage(int imageRes) {

    }

    @Override
    public void navigateToFoodPreferences() {

    }

    @Override
    public void navigateToSettings() {

    }

    @Override
    public void navigateToEditProfile() {

    }

    @Override
    public void navigateToRegistration() {
        Navigation.findNavController(requireView()).navigate(R.id.action_profileFragment_to_registrationFragment);
    }

    @Override
    public void showAboutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("About Food Planner")
                .setMessage("Food Planner is a smart meal management app designed to help you plan, search, and save meals efficiently.\n\nVersion: 1.0.0")
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }

    @Override
    public void showLogoutDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle("Logout")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton("Yes", (dialog, which) -> presenter.confirmLogout())
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }
}