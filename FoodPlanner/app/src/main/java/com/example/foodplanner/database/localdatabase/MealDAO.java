package com.example.foodplanner.database.localdatabase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.foodplanner.model.MealDTO;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface MealDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Completable insertMeal(MealDTO meal);

    @Delete
    Completable deleteMeal(MealDTO meal);

    @Query("SELECT * FROM meals_table WHERE idUser = :idUser AND isFavorite = 1")
    Observable<List<MealDTO>> getFavoriteMeals(String idUser);

    @Query("SELECT * FROM meals_table WHERE idUser = :idUser AND date = :date AND isPlanned = 1")
    Observable<List<MealDTO>> getPlannedMeals(String idUser, String date);

    @Query("SELECT * FROM meals_table")
    Single<List<MealDTO>> getAllMeals();

}
