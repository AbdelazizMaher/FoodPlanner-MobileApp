package com.example.foodplanner.model;

import android.util.Log;

import com.example.foodplanner.database.MealLocalDataSource;
import com.example.foodplanner.network.api.MealRemoteApiDataSource;
import com.example.foodplanner.network.sync.MealRemoteSyncDataSource;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealRepository implements IMealRepository {
    private MealLocalDataSource localDataSource;
    private MealRemoteApiDataSource remoteDataSource;
    private MealRemoteSyncDataSource syncDataSource;
    private static MealRepository instance = null;

    private MealRepository(MealLocalDataSource localDataSource, MealRemoteApiDataSource remoteDataSource, MealRemoteSyncDataSource syncDataSource) {
        this.localDataSource = localDataSource;
        this.remoteDataSource = remoteDataSource;
        this.syncDataSource = syncDataSource;
    }

    public static MealRepository getInstance(MealLocalDataSource localDataSource, MealRemoteApiDataSource remoteDataSource, MealRemoteSyncDataSource syncDataSource) {
        if (instance == null) {
            instance = new MealRepository(localDataSource, remoteDataSource, syncDataSource);
        }
        return instance;
    }

    @Override
    public Observable<List<MealDTO>> getFavouriteMeals(String idUser) {
        return localDataSource.getFavouriteMeals(idUser);
    }

    @Override
    public Observable<List<MealDTO>> getPlannedMeals(String idUser, String date) {
        return localDataSource.getPlannedMeals(idUser, date);
    }

    @Override
    public Completable insertMeal(MealDTO meal) {
        return Completable.mergeArray(
                localDataSource.insertMeal(meal),
                syncDataSource.insertMeal(meal)
        );
    }

    @Override
    public Completable deleteMeal(MealDTO meal) {
        return Completable.mergeArray(
                localDataSource.deleteMeal(meal),
                syncDataSource.deleteMeal(meal)
        );
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

    public Single<List<MealDTO>> getAllMeals() {
        return localDataSource.getAllMeals();
    }

    public Completable syncMealsFromRemoteToLocal() {
        Single<List<MealDTO>> firebaseMealsSingle = syncDataSource.getAllMeals()
                .subscribeOn(Schedulers.io())
                .doOnSuccess(meals -> Log.d("DEBUG", "Firebase Meals: " + meals.size()));

        Single<List<MealDTO>> roomMealsSingle = localDataSource.getAllMeals()
                .subscribeOn(Schedulers.io())
                .doOnSuccess(meals -> Log.d("DEBUG", "Room Meals: " + meals.size()));

        return Single.concat(firebaseMealsSingle, roomMealsSingle)
                .toList()
                .flatMapCompletable(lists -> {
                    List<MealDTO> firebaseMeals = lists.get(0);
                    List<MealDTO> roomMeals = lists.get(1);

                    List<MealDTO> mealsToInsert = getMealsNotInRoom(firebaseMeals, roomMeals);
                    Log.d("DEBUG", "Meals to Insert: " + mealsToInsert.size());

                    return insertMealsIntoRoom(mealsToInsert);
                })
                .subscribeOn(Schedulers.io());
    }

    private Completable insertMealsIntoRoom(List<MealDTO> meals) {
        return Completable.fromAction(() -> {
            Log.d("DEBUG", "Inserting " + meals.size() + " meals into Room DB");
        }).andThen(Completable.mergeArray(
                meals.stream()
                        .peek(meal -> Log.d("DEBUG", "Inserting meal: " + meal.getIdMeal()))
                        .map(localDataSource::insertMeal)
                        .toArray(Completable[]::new)
        ));
    }
    private List<MealDTO> getMealsNotInRoom(List<MealDTO> firebaseMeals, List<MealDTO> localMeals) {
        List<MealDTO> missingMeals = new ArrayList<>();
        for (MealDTO meal : firebaseMeals) {
            if (!localMeals.contains(meal)) {
                missingMeals.add(meal);
            }
        }
        return missingMeals;
    }
}
