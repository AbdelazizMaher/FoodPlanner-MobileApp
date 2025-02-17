package com.example.foodplanner.home;

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
import com.example.foodplanner.model.MealResponseModel;

import java.util.ArrayList;

public class HomeRecyclerAdapter extends RecyclerView.Adapter<HomeRecyclerAdapter.MyViewHolder> {
    private ArrayList<MealResponseModel.MealsDTO> meals;
    private OnMealClickListener onMealClickListener;
    private int layoutType;
    public static final int LAYOUT_TYPE_RANDOM = 1;
    public static final int LAYOUT_TYPE_NORMAL = 2;

    public HomeRecyclerAdapter(ArrayList<MealResponseModel.MealsDTO> meals, int layoutType) {
        this.layoutType = layoutType;
        this.meals = meals;
    }

    public void setOnMealClickListener(OnMealClickListener onMealClickListener) {
        this.onMealClickListener = onMealClickListener;
    }

    public void setMeals(ArrayList<MealResponseModel.MealsDTO> meals) {
        this.meals = meals;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutType == LAYOUT_TYPE_NORMAL) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meal_small_custom, parent, false);
            return new MyViewHolder(view);
        }else if (layoutType == LAYOUT_TYPE_RANDOM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meal_custom, parent, false);
            return new MyViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MealResponseModel.MealsDTO meal = meals.get(position);
        holder.bind(meal);
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView mealImage;
        private TextView mealName;
        private Button addFavouritesBtn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mealImage = itemView.findViewById(R.id.mealImage);
            mealName = itemView.findViewById(R.id.mealName);
            addFavouritesBtn = itemView.findViewById(R.id.addFavouritesBtn);
        }

        public void bind(MealResponseModel.MealsDTO meal) {
            mealName.setText(meal.getStrMeal());
            mealName.setVisibility(View.GONE);
            addFavouritesBtn.setTextSize(20.0F);
            addFavouritesBtn.setText(meal.getStrMeal());

            Glide.with(mealImage.getContext())
                    .load(meal.getStrMealThumb())
                    .into(mealImage);

            mealImage.setOnClickListener(v -> {
                onMealClickListener.showMealDetails(meal);
            });

        }
    }
}
