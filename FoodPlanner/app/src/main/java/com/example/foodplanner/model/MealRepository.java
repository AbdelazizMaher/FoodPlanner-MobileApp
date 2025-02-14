package com.example.foodplanner.model;

import androidx.lifecycle.LiveData;

import com.example.foodplanner.database.MealLocalDataSource;
import com.example.foodplanner.network.MealRemoteDataSource;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import retrofit2.Call;
import retrofit2.http.Query;

public class MealRepository implements IMealRepository {
    private MealLocalDataSource localDataSource;
    private MealRemoteDataSource remoteDataSource;
    private static MealRepository instance = null;

    private MealRepository(MealLocalDataSource localDataSource, MealRemoteDataSource remoteDataSource) {
        this.localDataSource = localDataSource;
        this.remoteDataSource = remoteDataSource;
    }

    public static MealRepository getInstance(MealLocalDataSource localDataSource, MealRemoteDataSource remoteDataSource) {
        if (instance == null) {
            instance = new MealRepository(localDataSource, remoteDataSource);
        }
        return instance;
    }

    @Override
    public Observable<List<MealDTO>> getFavouriteMeals(String idUser) {
        return localDataSource.getFavouriteMeals(idUser);
    }

    @Override
    public Observable<List<MealDTO>> gePlannedMeals(String idUser, String date) {
        return localDataSource.gePlannedMeals(idUser, date);
    }

    @Override
    public Completable insertMeal(MealDTO meal) {
        return localDataSource.insertMeal(meal);
    }

    @Override
    public Completable deleteMeal(MealDTO meal) {
        return localDataSource.deleteMeal(meal);
    }

    @Override
    public Single<MealResponseModel> getRandomMeal() {
        return remoteDataSource.getRandomMeal();
    }

    @Override
    public Single<CategoryResponseModel> getMealCategories() {
        return remoteDataSource.getMealCategories();
    }

    @Override
    public Single<AreaResponseModel> getAreas() {
        return remoteDataSource.getAreas();
    }

    @Override
    public Single<IngredientResponseModel> getIngredients() {
        return remoteDataSource.getIngredients();
    }

    @Override
    public Single<MealResponseModel> filterByCategory(String category) {
        return remoteDataSource.filterByCategory(category);
    }

    @Override
    public Single<MealResponseModel> filterByArea(String area) {
        return remoteDataSource.filterByArea(area);
    }

    @Override
    public Single<MealResponseModel> filterByIngredient(String ingredient) {
        return remoteDataSource.filterByIngredient(ingredient);
    }
    @Override
    public Single<MealResponseModel> getMealDetailsById(int mealId) {
        return remoteDataSource.getMealDetailsById(mealId);
    }
}
