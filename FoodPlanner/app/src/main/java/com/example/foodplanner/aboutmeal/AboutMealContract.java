package com.example.foodplanner.aboutmeal;

import com.example.foodplanner.model.MealResponseModel;

public interface AboutMealContract {
    interface IView {
        void showMealDetails(MealResponseModel.MealsDTO meal);
        void showError(String error);
    }

    interface IPresenter {
        void getMealById(int mealID);
    }
}
