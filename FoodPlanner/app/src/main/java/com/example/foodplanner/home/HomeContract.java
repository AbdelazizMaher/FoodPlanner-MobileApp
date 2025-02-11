package com.example.foodplanner.home;

import com.example.foodplanner.model.MealResponseModel;

import java.util.List;

public interface HomeContract {
    interface IView {
        void showRandomMeals(MealResponseModel.MealsDTO meal);
        void showError(String error);
    }

    interface IPresenter {
        void fetchRandomMeals();
    }
}
