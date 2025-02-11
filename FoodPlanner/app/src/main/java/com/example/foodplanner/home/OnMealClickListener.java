package com.example.foodplanner.home;


import com.example.foodplanner.model.MealResponseModel;

public interface OnMealClickListener {
    void showMealDetails(MealResponseModel.MealsDTO meal);
}
