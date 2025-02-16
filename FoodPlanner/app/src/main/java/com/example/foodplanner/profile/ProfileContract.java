package com.example.foodplanner.profile;

public interface ProfileContract {
    interface IView {
        void showUserName(String name);
        void showProfileImage(int imageRes);
        void navigateToFoodPreferences();
        void navigateToSettings();
        void navigateToEditProfile();
        void navigateToRegistration();
        void showAboutDialog();
        void showLogoutDialog();
    }

    interface IPresenter {
        void loadUserData();
        void onFoodPreferencesClicked();
        void onSettingsClicked();
        void onEditProfileClicked();
        void onAboutClicked();
        void onLogoutClicked();
        void confirmLogout();
    }
}
