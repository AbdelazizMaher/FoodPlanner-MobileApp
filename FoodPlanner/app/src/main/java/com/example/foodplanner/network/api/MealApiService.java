package com.example.foodplanner.network.api;

import com.example.foodplanner.model.AreaResponseModel;
import com.example.foodplanner.model.CategoryResponseModel;
import com.example.foodplanner.model.IngredientResponseModel;
import com.example.foodplanner.model.MealResponseModel;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MealApiService {
    Single<MealResponseModel> searchMealByName(@Query("s") String mealName);

    @GET("search.php")
    Single<MealResponseModel> listMealsByFirstLetter(@Query("f") String firstLetter);

    @GET("lookup.php")
    Single<MealResponseModel> getMealDetailsById(@Query("i") int mealId);

    @GET("random.php")
    Single<MealResponseModel> getRandomMeal();

    @GET("categories.php")
    Single<CategoryResponseModel> getMealCategories();

    @GET("list.php")
    Single<CategoryResponseModel> getCategories(@Query("c") String list);

    @GET("list.php")
    Single<AreaResponseModel> getAreas(@Query("a") String list);

    @GET("list.php")
    Single<IngredientResponseModel> getIngredients(@Query("i") String list);

    @GET("filter.php")
    Single<MealResponseModel> filterByIngredient(@Query("i") String ingredient);

    @GET("filter.php")
    Single<MealResponseModel> filterByCategory(@Query("c") String category);

    @GET("filter.php")
    Single<MealResponseModel> filterByArea(@Query("a") String area);
}
