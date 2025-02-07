package com.example.foodplanner.view;

import android.content.Context;
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
import com.example.foodplanner.network.MealResponseModel;

import java.util.ArrayList;

public class RandomRecyclerAdapter extends RecyclerView.Adapter<RandomRecyclerAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<MealResponseModel> meals;
    private OnRandomMealClickListener onMealClickListener;

    public RandomRecyclerAdapter(Context context, ArrayList<MealResponseModel> meals) {
        this.context = context;
        this.meals = meals;
    }

    public void setOnMealClickListener(OnRandomMealClickListener onMealClickListener) {
        this.onMealClickListener = onMealClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meal_custom, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MealResponseModel meal = meals.get(position);
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
            addIngredientsBtn = itemView.findViewById(R.id.addIngredientsBtn);
        }

        public void bind(MealResponseModel meal) {
            mealName.setText(meal.getMeals().get(0).getStrMeal());

            Glide.with(mealImage.getContext())
                    .load(meal.getMeals().get(0).getStrMealThumb())
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(mealImage);

            addIngredientsBtn.setOnClickListener(v -> {
                if (onMealClickListener != null) {
                    onMealClickListener.onMealClick(meal);
                }
            });
        }
    }
}
