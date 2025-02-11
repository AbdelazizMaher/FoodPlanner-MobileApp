package com.example.foodplanner.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodplanner.R;
import com.example.foodplanner.model.IngredientResponseModel;

import java.util.ArrayList;

public class IngredientRecyclerAdapter extends RecyclerView.Adapter<IngredientRecyclerAdapter.MyViewHolder> {
    private ArrayList<IngredientResponseModel.MealsDTO> ingredients;
    private OnIngredientClickListener onIngredientClickListener;

    public IngredientRecyclerAdapter(ArrayList<IngredientResponseModel.MealsDTO> ingredients) {
        this.ingredients = ingredients;
    }

    public void setOnIngredientClickListener(OnIngredientClickListener listener) {
        this.onIngredientClickListener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ingredient_custom, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        IngredientResponseModel.MealsDTO ingredient = ingredients.get(position);
        holder.bind(ingredient);
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView ingredientImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientImage = itemView.findViewById(R.id.ingredientImage);
        }

        public void bind(IngredientResponseModel.MealsDTO ingredient) {
            Glide.with(ingredientImage.getContext())
                    .load(ingredient.getIngredientImageUrl())
                    .into(ingredientImage);

            itemView.setOnClickListener(v -> {
                if (onIngredientClickListener != null) {
                    onIngredientClickListener.onIngredientClick(ingredient);
                }
            });
        }
    }
}
