package com.example.foodplanner.model;

import androidx.lifecycle.LiveData;

import com.example.foodplanner.database.MealLocalDataSource;
import com.example.foodplanner.network.MealRemoteDataSource;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import retrofit2.Call;

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
    public LiveData<List<MealResponseModel.MealsDTO>> getFavouriteMeals() {
        return localDataSource.getFavouriteMeals();
    }

    @Override
    public LiveData<List<MealResponseModel.MealsDTO>> gePlannedMeals() {
        return localDataSource.gePlannedMeals();
    }

    @Override
    public void insertMeal(MealResponseModel.MealsDTO meal) {
        localDataSource.insertMeal(meal);
    }

    @Override
    public void deleteMeal(MealResponseModel.MealsDTO meal) {
        localDataSource.deleteMeal(meal);
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
}
