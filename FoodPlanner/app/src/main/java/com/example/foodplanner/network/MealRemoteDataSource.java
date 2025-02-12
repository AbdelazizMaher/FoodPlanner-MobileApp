package com.example.foodplanner.network;


import com.example.foodplanner.model.AreaResponseModel;
import com.example.foodplanner.model.CategoryResponseModel;
import com.example.foodplanner.model.IngredientResponseModel;
import com.example.foodplanner.model.MealResponseModel;

import io.reactivex.rxjava3.core.Single;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Query;

public class MealRemoteDataSource implements IMealRemoteDataSource {
    private static String BASE_URL = "https://www.themealdb.com/api/json/v1/1/";
    private MealApiService service;
    private static MealRemoteDataSource instance = null;

    private MealRemoteDataSource() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
        service = retrofit.create(MealApiService.class);
    }

    public static MealRemoteDataSource getInstance() {
        if (instance == null) {
            instance = new MealRemoteDataSource();
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
}
