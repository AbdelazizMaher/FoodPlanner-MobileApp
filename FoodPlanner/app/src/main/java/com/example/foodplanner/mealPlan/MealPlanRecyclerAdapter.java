package com.example.foodplanner.mealPlan;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodplanner.R;
import com.example.foodplanner.model.MealDTO;
import com.example.foodplanner.model.MealResponseModel;

import java.util.ArrayList;
import java.util.List;

public class MealPlanRecyclerAdapter extends RecyclerView.Adapter<MealPlanRecyclerAdapter.MyViewHolder> {
    private List<MealDTO> meals;
    private OnMealClickListener onMealClickListener;
    private OnRemoveButtonClickListener onRemoveButtonClickListener;

    public MealPlanRecyclerAdapter(List<MealDTO> meals) {
        this.meals = meals;
    }

    public void setOnMealClickListener(OnMealClickListener onMealClickListener) {
        this.onMealClickListener = onMealClickListener;
    }
    public void setOnRemoveButtonClickListener(OnRemoveButtonClickListener onRemoveButtonClickListener){
        this.onRemoveButtonClickListener = onRemoveButtonClickListener;
    }

    public void setMeals(List<MealDTO> meals) {
        this.meals = meals;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meal_small_custom, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MealDTO meal = meals.get(position);
        holder.bind(meal);
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView mealImage;
        private TextView mealName;
        private Button addIngredientsBtn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mealImage = itemView.findViewById(R.id.mealImage);
            mealName = itemView.findViewById(R.id.mealName);
            addIngredientsBtn = itemView.findViewById(R.id.addFavouritesBtn);
        }

        public void bind(MealDTO meal) {
            mealName.setText(meal.getMeal().getStrMeal());
            addIngredientsBtn.setText("Remove From Plan");

            Glide.with(mealImage.getContext())
                    .load(meal.getMeal().getStrMealThumb())
                    .into(mealImage);

            mealImage.setOnClickListener(v -> {
                onMealClickListener.showMealDetails(meal.getMeal());
            });

            addIngredientsBtn.setOnClickListener(v -> {
                onRemoveButtonClickListener.onRemoveButtonClick(meal);
            });
        }
    }
}
