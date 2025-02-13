package com.example.foodplanner.network;

import com.example.foodplanner.model.AreaResponseModel;
import com.example.foodplanner.model.CategoryResponseModel;
import com.example.foodplanner.model.IngredientResponseModel;
import com.example.foodplanner.model.MealResponseModel;

import io.reactivex.rxjava3.core.Single;
import retrofit2.Call;

public interface IMealRemoteDataSource {
    public Single<MealResponseModel> getRandomMeal();
    public Single<CategoryResponseModel> getMealCategories();
    public Single<AreaResponseModel> getAreas();
    public Single<IngredientResponseModel> getIngredients();
    public Single<MealResponseModel> filterByCategory(String category);
    public Single<MealResponseModel> filterByArea(String area);
    public Single<MealResponseModel> filterByIngredient(String ingredient);
    public Single<MealResponseModel> getMealDetailsById(int mealId);
}
