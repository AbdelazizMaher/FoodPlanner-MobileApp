package com.example.foodplanner.allMeals.presenter;

import com.example.foodplanner.allMeals.AllMealsContract;
import com.example.foodplanner.repository.mealrepository.MealRepository;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AllMealsPresenter implements AllMealsContract.IPresenter {
    private AllMealsContract.IView view;
    private MealRepository repo;

    public AllMealsPresenter(AllMealsContract.IView view, MealRepository repo) {
        this.view = view;
        this.repo = repo;
    }

    @Override
    public void fetchMealsByIngredients(String ingredient) {
        repo.filterByIngredient(ingredient)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> view.showMeals(response.getMeals()),
                        error -> view.showError(error.getMessage())
                );
    }

    @Override
    public void fetchMealsByAreas(String area) {
        repo.filterByArea(area)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> view.showMeals(response.getMeals()),
                        error -> view.showError(error.getMessage())
                );
    }

    @Override
    public void fetchMealsByCategories(String category) {
        repo.filterByCategory(category)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> view.showMeals(response.getMeals()),
                        error -> view.showError(error.getMessage())
                );
    }
}
