package com.example.foodplanner.database;

import androidx.lifecycle.LiveData;

import com.example.foodplanner.model.MealDTO;
import com.example.foodplanner.model.MealResponseModel;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;

public interface IMealLocalDataSource {
    public Observable<List<MealDTO>> getFavouriteMeals(String idUser);
    public Observable<List<MealDTO>> getPlannedMeals(String idUser, String date);
    public Completable insertMeal(MealDTO meal);
    public Completable deleteMeal(MealDTO meal);
}
