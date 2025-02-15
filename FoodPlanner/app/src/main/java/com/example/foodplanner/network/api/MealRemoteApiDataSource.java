package com.example.foodplanner.network.api;


import com.example.foodplanner.model.AreaResponseModel;
import com.example.foodplanner.model.CategoryResponseModel;
import com.example.foodplanner.model.IngredientResponseModel;
import com.example.foodplanner.model.MealResponseModel;

import io.reactivex.rxjava3.core.Single;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MealRemoteApiDataSource implements IMealRemoteApiDataSource {
    private static String BASE_URL = "https://www.themealdb.com/api/json/v1/1/";
    private MealApiService service;
    private static MealRemoteApiDataSource instance = null;

    private MealRemoteApiDataSource() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
        service = retrofit.create(MealApiService.class);
    }

    public static MealRemoteApiDataSource getInstance() {
        if (instance == null) {
            instance = new MealRemoteApiDataSource();
        }
        return instance;
    }

    public MealApiService getService() {
        return service;
    }

    @Override
    public Single<MealResponseModel> getRandomMeal() {
        return service.getRandomMeal();
    }

    @Override
    public Single<CategoryResponseModel> getMealCategories() {
        return service.getMealCategories();
    }

    @Override
    public Single<AreaResponseModel> getAreas() {
        return service.getAreas("list");
    }

    @Override
    public Single<IngredientResponseModel> getIngredients() {
        return service.getIngredients("list");
    }

    @Override
    public Single<MealResponseModel> filterByCategory(String category) {
        return service.filterByCategory(category);
    }

    @Override
    public Single<MealResponseModel> filterByArea(String area) {
        return service.filterByArea(area);
    }

    @Override
    public Single<MealResponseModel> filterByIngredient(String ingredient) {
        return service.filterByIngredient(ingredient);
    }

    @Override
    public Single<MealResponseModel> getMealDetailsById(int mealId) {
        return service.getMealDetailsById(mealId);
    }

}
