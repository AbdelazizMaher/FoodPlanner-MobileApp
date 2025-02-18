package com.example.foodplanner.home;

import com.example.foodplanner.model.MealResponseModel;

import java.util.List;

public interface HomeContract {
    interface IView {
        void showRandomMeals(MealResponseModel.MealsDTO meal);
        void showRecommendedMeals(List<MealResponseModel.MealsDTO> meals);
        void showDesserts(List<MealResponseModel.MealsDTO> meals);
        void showProgress(boolean show);
        void showError(String error);
    }

    interface IPresenter {
        void fetchRandomMeals();
        void fetchRecommendedMeals();
        void fetchDesserts();
    }
}
