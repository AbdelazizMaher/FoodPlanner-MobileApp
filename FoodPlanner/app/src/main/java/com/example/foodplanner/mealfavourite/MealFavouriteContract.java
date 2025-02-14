package com.example.foodplanner.mealfavourite;

import com.example.foodplanner.model.MealDTO;

import java.util.List;

public interface MealFavouriteContract {
    interface IView {
        void showFavouriteMeals(List<MealDTO> meals);
    }

    interface IPresenter {
        void fetchFavouriteMeals(String idUser);
        void removeMealFromFavourite(MealDTO meal);
    }
}
