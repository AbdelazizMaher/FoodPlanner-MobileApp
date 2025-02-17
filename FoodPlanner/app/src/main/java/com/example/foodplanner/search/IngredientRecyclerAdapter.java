package com.example.foodplanner.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodplanner.R;
import com.example.foodplanner.model.IngredientResponseModel;

import java.util.ArrayList;
import java.util.List;

public class IngredientRecyclerAdapter extends RecyclerView.Adapter<IngredientRecyclerAdapter.MyViewHolder> {
    private ArrayList<IngredientResponseModel.MealsDTO> ingredients;
    private ArrayList<IngredientResponseModel.MealsDTO> fullList;
    private OnIngredientClickListener onIngredientClickListener;

    public IngredientRecyclerAdapter(ArrayList<IngredientResponseModel.MealsDTO> ingredients) {
        this.ingredients = ingredients;
        this.fullList = new ArrayList<>(ingredients);
    }

    public void setIngredients(ArrayList<IngredientResponseModel.MealsDTO> ingredients) {
        this.ingredients = ingredients;
        this.fullList = new ArrayList<>(ingredients);
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
        private TextView ingredientName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientImage = itemView.findViewById(R.id.ingredientImage);
            ingredientName = itemView.findViewById(R.id.ingredientQuantity);
        }

        public void bind(IngredientResponseModel.MealsDTO ingredient) {
            ingredientName.setText(ingredient.getStrIngredient());
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

    public void filter(String query) {
        List<IngredientResponseModel.MealsDTO> filteredList = new ArrayList<>();
        for (IngredientResponseModel.MealsDTO item : fullList) {
            if (item.getStrIngredient().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(item);
            }
        }
        ingredients.clear();
        ingredients.addAll(filteredList);
        notifyDataSetChanged();
    }
}
