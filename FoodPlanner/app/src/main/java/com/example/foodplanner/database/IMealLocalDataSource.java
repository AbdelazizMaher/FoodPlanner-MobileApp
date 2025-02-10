package com.example.foodplanner.database;

import androidx.lifecycle.LiveData;

import com.example.foodplanner.model.MealResponseModel;

import java.util.List;

public interface IMealLocalDataSource {
    public LiveData<List<MealResponseModel.MealsDTO>> getFavouriteMeals();
    public LiveData<List<MealResponseModel.MealsDTO>> gePlannedMeals();
    public void insertMeal(MealResponseModel.MealsDTO meal);
    public void deleteMeal(MealResponseModel.MealsDTO meal);
}
