package com.example.foodplanner.database;

import androidx.room.TypeConverter;

import com.example.foodplanner.model.MealResponseModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class MealTypeConverter {
    private static final Gson gson = new Gson();

    @TypeConverter
    public static String fromMeal(MealResponseModel.MealsDTO meal) {
        return gson.toJson(meal);
    }

    @TypeConverter
    public static MealResponseModel.MealsDTO toMeal(String mealString) {
        return gson.fromJson(mealString, new TypeToken<MealResponseModel.MealsDTO>() {}.getType());
    }
}
