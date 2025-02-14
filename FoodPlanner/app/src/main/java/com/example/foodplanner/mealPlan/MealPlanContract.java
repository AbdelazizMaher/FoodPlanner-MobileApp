package com.example.foodplanner.mealPlan;

import com.example.foodplanner.model.MealDTO;

import java.util.List;

public interface MealPlanContract {
    interface IView {
        void showPlannedMeals(List<MealDTO> meals);
    }

    interface IPresenter {
        void fetchPlannedMeals(String idUser, String date);
        void removeMealFromPlan(MealDTO meal);
    }
}
