package com.example.foodplanner.database;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.foodplanner.model.MealDTO;
import com.example.foodplanner.model.MealResponseModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealLocalDataSource implements IMealLocalDataSource {
    private Context context;
    private MealDAO dao;
    private Observable<List<MealDTO>> favouriteMeals;
    private Observable<List<MealDTO>> plannedMeals;
    private static MealLocalDataSource instance = null;

    public static MealLocalDataSource getInstance(Context context) {
        if (instance == null) {
            instance = new MealLocalDataSource(context);
        }
        return instance;
    }

    @Override
    public Observable<List<MealDTO>> getFavouriteMeals(String idUser) {
        return dao.getFavoriteMeals(idUser);
    }

    @Override
    public Observable<List<MealDTO>> getPlannedMeals(String idUser, String date) {
        return dao.getPlannedMeals(idUser, date);
    }

    @Override
    public Completable insertMeal(MealDTO meal) {
        return dao.insertMeal(meal);
    }
    @Override
    public Completable deleteMeal(MealDTO meal) {
        return dao.deleteMeal(meal);
    }

    public Single<List<MealDTO>> getAllMeals() {
        return dao.getAllMeals();
    }
    private MealLocalDataSource(Context context) {
        this.context = context;
        dao = AppDataBase.getInstance(context).getMealDao();
        //favouriteMeals = dao.getFavoriteMeals();
        //plannedMeals = dao.getPlannedMeals();
    }
}
