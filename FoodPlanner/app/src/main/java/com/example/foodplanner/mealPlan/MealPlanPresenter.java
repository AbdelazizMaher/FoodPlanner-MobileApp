package com.example.foodplanner.mealPlan;

import com.example.foodplanner.model.MealRepository;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealPlanPresenter implements MealPlanContract.IPresenter {
    private MealPlanContract.IView view;
    private MealRepository repo;

    public MealPlanPresenter(MealPlanContract.IView view, MealRepository repo) {
        this.view = view;
        this.repo = repo;
    }

    @Override
    public void fetchPlannedMeals(String idUser, String date) {
        repo.getPlannedMeals(idUser, date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(meals -> {
                    view.showPlannedMeals(meals);
                });
    }
}
