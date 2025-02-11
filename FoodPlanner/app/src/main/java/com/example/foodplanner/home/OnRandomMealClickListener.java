package com.example.foodplanner.home;


import com.example.foodplanner.model.MealResponseModel;

public interface OnRandomMealClickListener {
    void onMealClick(MealResponseModel.MealsDTO meal);
}
