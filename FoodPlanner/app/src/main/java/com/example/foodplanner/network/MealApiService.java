package com.example.foodplanner.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MealApiService {
    Call<MealResponseModel> searchMealByName(@Query("s") String mealName);

    @GET("search.php")
    Call<MealResponseModel> listMealsByFirstLetter(@Query("f") String firstLetter);

    @GET("lookup.php")
    Call<MealResponseModel> getMealDetailsById(@Query("i") int mealId);

    @GET("random.php")
    Call<MealResponseModel> getRandomMeal();

    @GET("categories.php")
    Call<CategoryResponseModel> getMealCategories();

    @GET("list.php")
    Call<CategoryResponseModel> getCategories(@Query("c") String list);

    @GET("list.php")
    Call<AreaResponseModel> getAreas(@Query("a") String list);

    @GET("list.php")
    Call<IngredientResponse> getIngredients(@Query("i") String list);

    @GET("filter.php")
    Call<MealResponseModel> filterByIngredient(@Query("i") String ingredient);

    @GET("filter.php")
    Call<MealResponseModel> filterByCategory(@Query("c") String category);

    @GET("filter.php")
    Call<MealResponseModel> filterByArea(@Query("a") String area);
}
