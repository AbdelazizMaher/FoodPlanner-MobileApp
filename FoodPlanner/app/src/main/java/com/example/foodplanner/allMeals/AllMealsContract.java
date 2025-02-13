package com.example.foodplanner.allMeals;

import com.example.foodplanner.model.MealResponseModel;

import java.util.List;

public interface AllMealsContract {
    public interface IView {
        void showMeals(List<MealResponseModel.MealsDTO> meals);
        void showError(String error);
    }
    public interface IPresenter{
        void fetchMealsByIngredients(String ingredient);
        void fetchMealsByAreas(String area);
        void fetchMealsByCategories(String category);
    }
}
