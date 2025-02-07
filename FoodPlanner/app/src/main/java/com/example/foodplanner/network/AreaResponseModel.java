package com.example.foodplanner.network;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AreaResponseModel {

    @SerializedName("meals")
    private List<MealsDTO> meals;

    public List<MealsDTO> getMeals() {
        return meals;
    }

    public void setMeals(List<MealsDTO> meals) {
        this.meals = meals;
    }

    public static class MealsDTO {
        @SerializedName("strArea")
        private String strArea;

        public String getStrArea() {
            return strArea;
        }

        public void setStrArea(String strArea) {
            this.strArea = strArea;
        }
    }
}
