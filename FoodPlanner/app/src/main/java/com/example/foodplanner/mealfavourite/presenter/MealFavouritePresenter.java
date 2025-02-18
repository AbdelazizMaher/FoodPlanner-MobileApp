package com.example.foodplanner.mealfavourite.presenter;

import com.example.foodplanner.mealfavourite.MealFavouriteContract;
import com.example.foodplanner.model.MealDTO;
import com.example.foodplanner.repository.mealrepository.MealRepository;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealFavouritePresenter implements MealFavouriteContract.IPresenter {
    private MealFavouriteContract.IView view;
    private MealRepository repo;

    public MealFavouritePresenter(MealFavouriteContract.IView view, MealRepository repo) {
        this.view = view;
        this.repo = repo;
    }


    @Override
    public void fetchFavouriteMeals(String idUser) {
        repo.getFavouriteMeals(idUser)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(meals -> {
                    view.showFavouriteMeals(meals);
                });
    }

    @Override
    public void removeMealFromFavourite(MealDTO meal) {
        repo.deleteMeal(meal)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }
}
