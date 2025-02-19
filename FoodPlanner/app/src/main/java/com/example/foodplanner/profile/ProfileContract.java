package com.example.foodplanner.profile;

public interface ProfileContract {
    interface IView {
        void showUserName(String name);
        void navigateToPlanPage();
        void navigateToFavouritePage();
        void navigateToRegistration();
        void showAboutDialog();
        void showLogoutDialog();
    }

    interface IPresenter {
        void loadUserData();
        void onPlanClicked();
        void onFavouriteClicked();
        void onAboutClicked();
        void onLogoutClicked();
        void confirmLogout();
    }
}
