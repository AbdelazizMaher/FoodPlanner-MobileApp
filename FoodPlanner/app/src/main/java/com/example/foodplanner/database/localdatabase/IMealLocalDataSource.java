package com.example.foodplanner.database.localdatabase;

import com.example.foodplanner.model.MealDTO;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;

public interface IMealLocalDataSource {
    public Observable<List<MealDTO>> getFavouriteMeals(String idUser);
    public Observable<List<MealDTO>> getPlannedMeals(String idUser, String date);
    public Completable insertMeal(MealDTO meal);
    public Completable deleteMeal(MealDTO meal);
}
