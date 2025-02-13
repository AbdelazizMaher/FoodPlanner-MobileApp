package com.example.foodplanner.allMeals;


import com.example.foodplanner.model.MealResponseModel;

public interface OnMealClickListener {
    void showMealDetails(MealResponseModel.MealsDTO meal);
}
