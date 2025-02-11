package com.example.foodplanner.home;


import com.example.foodplanner.model.MealResponseModel;

public interface OnRandomMealClickListener {
    void showMealDetails(MealResponseModel.MealsDTO meal);
}
