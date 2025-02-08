package com.example.foodplanner.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class IngredientResponseModel {

    @SerializedName("meals")
    private List<MealsDTO> meals;

    public List<MealsDTO> getMeals() {
        return meals;
    }

    public void setMeals(List<MealsDTO> meals) {
        this.meals = meals;
    }

    public static class MealsDTO {
        @SerializedName("idIngredient")
        private String idIngredient;
        @SerializedName("strIngredient")
        private String strIngredient;
        @SerializedName("strDescription")
        private String strDescription;
        @SerializedName("strType")
        private Object strType;

        public String getIdIngredient() {
            return idIngredient;
        }

        public void setIdIngredient(String idIngredient) {
            this.idIngredient = idIngredient;
        }

        public String getStrIngredient() {
            return strIngredient;
        }

        public void setStrIngredient(String strIngredient) {
            this.strIngredient = strIngredient;
        }

        public String getStrDescription() {
            return strDescription;
        }

        public void setStrDescription(String strDescription) {
            this.strDescription = strDescription;
        }

        public Object getStrType() {
            return strType;
        }

        public void setStrType(Object strType) {
            this.strType = strType;
        }

        public String getIngredientImageUrl() {
            return "https://www.themealdb.com/images/ingredients/" + strIngredient + "-Small.png";
        }
    }
}
