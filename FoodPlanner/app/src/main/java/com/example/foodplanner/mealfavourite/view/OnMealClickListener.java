package com.example.foodplanner.mealfavourite.view;


import com.example.foodplanner.model.MealResponseModel;

public interface OnMealClickListener {
    void showMealDetails(MealResponseModel.MealsDTO meal);
}
