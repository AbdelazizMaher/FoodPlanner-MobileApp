package com.example.foodplanner.repository.mealrepository;

import com.example.foodplanner.model.AreaResponseModel;
import com.example.foodplanner.model.CategoryResponseModel;
import com.example.foodplanner.model.IngredientResponseModel;
import com.example.foodplanner.model.MealDTO;
import com.example.foodplanner.model.MealResponseModel;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

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
