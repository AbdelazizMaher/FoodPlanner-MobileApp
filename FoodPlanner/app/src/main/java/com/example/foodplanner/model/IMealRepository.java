package com.example.foodplanner.model;

import androidx.lifecycle.LiveData;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import retrofit2.Call;

public interface IMealRepository {
    public Observable<List<MealDTO>> getFavouriteMeals(String idUser);
    public Observable<List<MealDTO>> getPlannedMeals(String idUser, String date);
    public Completable insertMeal(MealDTO meal);
    public Completable deleteMeal(MealDTO meal);
    public Single<MealResponseModel> getRandomMeal() ;
    public Single<CategoryResponseModel> getMealCategories();
    public Single<AreaResponseModel> getAreas();
    public Single<IngredientResponseModel> getIngredients();
    public Single<MealResponseModel> filterByCategory(String category);
    public Single<MealResponseModel> filterByArea(String area);
    Single<MealResponseModel> filterByIngredient(String ingredient);
    public Single<MealResponseModel> getMealDetailsById(int mealId);
}
