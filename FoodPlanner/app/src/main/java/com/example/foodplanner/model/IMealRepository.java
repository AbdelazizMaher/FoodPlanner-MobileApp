package com.example.foodplanner.model;

import androidx.lifecycle.LiveData;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import retrofit2.Call;

public interface IMealRepository {
    public LiveData<List<MealResponseModel.MealsDTO>> getFavouriteMeals();
    public LiveData<List<MealResponseModel.MealsDTO>> gePlannedMeals();
    public void insertMeal(MealResponseModel.MealsDTO meal);
    public void deleteMeal(MealResponseModel.MealsDTO meal);
    public Single<MealResponseModel> getRandomMeal() ;
    public Single<CategoryResponseModel> getMealCategories();
    public Single<AreaResponseModel> getAreas();
    public Single<IngredientResponseModel> getIngredients();
    public Single<MealResponseModel> filterByCategory(String category);
    public Single<MealResponseModel> filterByArea(String area);
    Single<MealResponseModel> filterByIngredient(String ingredient);
    public Single<MealResponseModel> getMealDetailsById(int mealId);
}
