package com.example.foodplanner.database;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.foodplanner.model.MealResponseModel;

import java.util.List;

public class MealsLocalDataSource implements IMealLocalDataSource {
    private Context context;
    private MealDAO dao;
    private LiveData<List<MealResponseModel.MealsDTO>> observableProducts;
    private static MealsLocalDataSource instance = null;

    public static MealsLocalDataSource getInstance(Context context) {
        if (instance == null) {
            instance = new MealsLocalDataSource(context);
        }
        return instance;
    }
    @Override
    public LiveData<List<MealResponseModel.MealsDTO>> getObservableProducts() {
        return observableProducts;
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

    private MealsLocalDataSource(Context context) {
        this.context = context;
        dao = AppDataBase.getInstance(context).getMealDao();
        observableProducts = dao.getFavoriteMeals();
    }
}
