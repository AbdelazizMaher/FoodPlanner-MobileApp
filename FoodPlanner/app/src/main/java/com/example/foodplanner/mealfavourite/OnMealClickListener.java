package com.example.foodplanner.mealfavourite;


import com.example.foodplanner.model.MealResponseModel;

public interface OnMealClickListener {
    void showMealDetails(MealResponseModel.MealsDTO meal);
}
