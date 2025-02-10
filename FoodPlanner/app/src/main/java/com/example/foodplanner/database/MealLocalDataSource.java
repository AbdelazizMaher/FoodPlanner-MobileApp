package com.example.foodplanner.database;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.foodplanner.model.MealResponseModel;

import java.util.List;

public class MealLocalDataSource implements IMealLocalDataSource {
    private Context context;
    private MealDAO dao;
    private LiveData<List<MealResponseModel.MealsDTO>> favouriteMeals;
    private LiveData<List<MealResponseModel.MealsDTO>> plannedMeals;
    private static MealLocalDataSource instance = null;

    public static MealLocalDataSource getInstance(Context context) {
        if (instance == null) {
            instance = new MealLocalDataSource(context);
        }
        return instance;
    }

    @Override
    public LiveData<List<MealResponseModel.MealsDTO>> getFavouriteMeals() {
        return favouriteMeals;
    }

    @Override
    public LiveData<List<MealResponseModel.MealsDTO>> gePlannedMeals() {
        return plannedMeals;
    }

    @Override
    public void insertMeal(MealResponseModel.MealsDTO meal) {
        new Thread(() -> {
            dao.insertMeal(meal);
        }).start();
    }
    @Override
    public void deleteMeal(MealResponseModel.MealsDTO meal) {
        new Thread(() -> {
            dao.deleteMeal(meal);
        }).start();
    }

    private MealLocalDataSource(Context context) {
        this.context = context;
        dao = AppDataBase.getInstance(context).getMealDao();
        //favouriteMeals = dao.getFavoriteMeals();
        //plannedMeals = dao.getPlannedMeals();
    }
}
