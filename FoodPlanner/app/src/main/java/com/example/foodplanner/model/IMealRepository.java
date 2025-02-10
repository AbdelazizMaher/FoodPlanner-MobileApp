package com.example.foodplanner.model;

import androidx.lifecycle.LiveData;

import com.example.foodplanner.network.NetworkCallback;

import java.util.List;

import retrofit2.Call;

public interface IMealRepository {
    public LiveData<List<MealResponseModel.MealsDTO>> getFavouriteMeals();
    public LiveData<List<MealResponseModel.MealsDTO>> gePlannedMeals();
    public void insertMeal(MealResponseModel.MealsDTO meal);
    public void deleteMeal(MealResponseModel.MealsDTO meal);
    public <T> void makeNetworkCall(Call<T> call, final NetworkCallback<T> callback);
}
