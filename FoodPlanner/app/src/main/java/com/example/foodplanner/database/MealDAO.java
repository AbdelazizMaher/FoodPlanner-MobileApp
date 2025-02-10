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

    @Query("SELECT * FROM meals_table WHERE idUser = :idUser AND isFavorite = 1")
    LiveData<List<MealResponseModel.MealsDTO>> getFavoriteMeals(String idUser);

    @Query("SELECT * FROM meals_table WHERE idUser = :idUser AND idMeal = :idMeal AND isPlanned = 1")
    LiveData<MealResponseModel.MealsDTO> getPlannedMeal(String idUser, String idMeal);

    @Query("SELECT * FROM meals_table WHERE idMeal = :idMeal")
    LiveData<MealResponseModel.MealsDTO> getMealById(String idMeal);

}
