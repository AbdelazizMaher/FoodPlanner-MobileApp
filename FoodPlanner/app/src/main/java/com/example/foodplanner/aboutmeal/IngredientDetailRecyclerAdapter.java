package com.example.foodplanner.aboutmeal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodplanner.R;
import com.example.foodplanner.model.MealResponseModel;

import java.util.ArrayList;

public class IngredientDetailRecyclerAdapter extends RecyclerView.Adapter<IngredientDetailRecyclerAdapter.MyViewHolder> {
    private ArrayList<AboutMealFragment.IngredientModel> ingredients;

    public IngredientDetailRecyclerAdapter(ArrayList<AboutMealFragment.IngredientModel> ingredients) {
        this.ingredients = ingredients;
    }

    @NonNull
    @Override
    public IngredientDetailRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ingredients_details_custom, parent, false);
        return new IngredientDetailRecyclerAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientDetailRecyclerAdapter.MyViewHolder holder, int position) {
        AboutMealFragment.IngredientModel ingredient = ingredients.get(position);
        holder.bind(ingredient);
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView ingredientImage;
        private TextView ingredientQuantity;
        private TextView ingredientName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientImage = itemView.findViewById(R.id.ingredientImage);
            ingredientQuantity = itemView.findViewById(R.id.ingredientQuantity);
            ingredientName = itemView.findViewById(R.id.ingredientName);
        }

        public void bind(AboutMealFragment.IngredientModel ingredient) {
            ingredientName.setText(ingredient.getName());
            ingredientQuantity.setText(ingredient.getQuantity());
            Glide.with(itemView.getContext())
                    .load(ingredient.getImageUrl())
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(ingredientImage);

        }
    }
}
