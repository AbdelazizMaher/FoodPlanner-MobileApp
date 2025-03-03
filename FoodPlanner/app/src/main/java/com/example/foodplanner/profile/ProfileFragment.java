package com.example.foodplanner.profile;

import android.content.SharedPreferences;
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
import android.widget.TextView;

import com.example.foodplanner.R;
import com.example.foodplanner.database.sharedpreference.SharedPreferenceCashing;
import com.example.foodplanner.repository.authrepository.AuthenticationRepository;

public class ProfileFragment extends Fragment implements ProfileContract.IView {

    private ImageView favouritesNav, planNav, aboutNav, logoutNav, backToHome;
    private TextView profileName;
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

        favouritesNav = view.findViewById(R.id.favouritesNav);
        planNav = view.findViewById(R.id.planNav);
        aboutNav = view.findViewById(R.id.aboutNav);
        logoutNav = view.findViewById(R.id.logoutNav);
        backToHome = view.findViewById(R.id.backToHome);
        profileName = view.findViewById(R.id.profile_name);

        backToHome.setOnClickListener(v -> {
            Navigation.findNavController(requireView()).navigateUp();
        });

        planNav.setOnClickListener(v -> {
            presenter.onPlanClicked();
        });

        favouritesNav.setOnClickListener(v -> {
            presenter.onFavouriteClicked();
        });

        aboutNav.setOnClickListener(v -> {
            presenter.onAboutClicked();
        });

        logoutNav.setOnClickListener(v -> {
            presenter.onLogoutClicked();
        });

        presenter.loadUserData();
    }

    @Override
    public void showUserName(String name) {
        profileName.setText(name);
    }

    @Override
    public void navigateToPlanPage() {
        Navigation.findNavController(requireView()).navigate(R.id.action_profileFragment_to_mealPlanFragment);
    }

    @Override
    public void navigateToFavouritePage() {
        Navigation.findNavController(requireView()).navigate(R.id.action_profileFragment_to_mealFavouriteFragment);
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