package com.example.foodplanner.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.foodplanner.model.MealResponseModel;

import java.util.List;

@Dao
public interface MealDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertMeal(MealResponseModel.MealsDTO meal);

    @Delete
    void deleteMeal(MealResponseModel.MealsDTO meal);

    @Query("SELECT * FROM meals_table WHERE isFavorite = 1")
    LiveData<List<MealResponseModel.MealsDTO>> getFavoriteMeals();

    @Query("SELECT * FROM meals_table WHERE isPlanned = 1")
    LiveData<List<MealResponseModel.MealsDTO>> getPlannedMeals();

    @Query("SELECT * FROM meals_table WHERE idMeal = :idMeal")
    LiveData<MealResponseModel.MealsDTO> getMealById(String idMeal);
}
