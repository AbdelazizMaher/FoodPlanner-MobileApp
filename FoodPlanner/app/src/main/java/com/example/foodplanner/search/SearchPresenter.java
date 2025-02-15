package com.example.foodplanner.search;

import com.example.foodplanner.model.MealRepository;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SearchPresenter implements SearchContract.IPresenter {

    private SearchContract.IView view;
    private MealRepository repo;

    public SearchPresenter(SearchContract.IView view, MealRepository repo) {
        this.view = view;
        this.repo = repo;
    }

    @Override
    public void fetchIngredients() {
        repo.getIngredients()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> view.showIngredients(response.getMeals()),
                        error -> view.showError(error.getMessage())
                );
    }

    @Override
    public void fetchAreas() {
        repo.getAreas()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> view.showAreas(response.getMeals()),
                        error -> view.showError(error.getMessage())
                );
    }

    @Override
    public void fetchCategories() {
        repo.getMealCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> view.showCategories(response.getCategories()),
                        error -> view.showError(error.getMessage())
                );
    }
}
