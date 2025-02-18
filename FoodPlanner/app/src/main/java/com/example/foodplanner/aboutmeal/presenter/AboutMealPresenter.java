package com.example.foodplanner.aboutmeal.presenter;

import com.example.foodplanner.aboutmeal.AboutMealContract;
import com.example.foodplanner.model.MealDTO;
import com.example.foodplanner.repository.mealrepository.MealRepository;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AboutMealPresenter implements AboutMealContract.IPresenter {
    private AboutMealContract.IView view;
    private MealRepository repo;

    public AboutMealPresenter(AboutMealContract.IView view, MealRepository repository) {
        this.view = view;
        this.repo = repository;
    }

    public void storeMeal(MealDTO meal) {
        repo.insertMeal(meal)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    @Override
    public void getMealById(int mealID) {
        repo.getMealDetailsById(mealID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> view.showMealDetails(response.getMeals().get(0)),
                        error -> view.showError(error.getMessage())
                );
    }
}
