package com.example.foodplanner.search.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodplanner.R;
import com.example.foodplanner.model.CategoryResponseModel;

import java.util.ArrayList;
import java.util.List;

public class CategoryRecyclerAdapter extends RecyclerView.Adapter<CategoryRecyclerAdapter.MyViewHolder> {
    private ArrayList<CategoryResponseModel.CategoriesDTO> categories;
    private ArrayList<CategoryResponseModel.CategoriesDTO> fullList;
    private OnCategoryClickListener onCategoryClickListener;

    public CategoryRecyclerAdapter(ArrayList<CategoryResponseModel.CategoriesDTO> categories) {
        this.categories = categories;
        this.fullList = new ArrayList<>(categories);
    }

    public void setCategories(ArrayList<CategoryResponseModel.CategoriesDTO> categories) {
        this.categories = categories;
        this.fullList = new ArrayList<>(categories);
    }

    public void setOnCategoryClickListener(OnCategoryClickListener listener) {
        this.onCategoryClickListener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_custom, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        CategoryResponseModel.CategoriesDTO category = categories.get(position);
        holder.bind(category);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView categoryImage;
        private TextView categoryName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryImage = itemView.findViewById(R.id.categoryImage);
            categoryName = itemView.findViewById(R.id.categoryName);
        }

        public void bind(CategoryResponseModel.CategoriesDTO category) {
            categoryName.setText(category.getStrCategory());

            Glide.with(categoryImage.getContext())
                    .load(category.getStrCategoryThumb())
                    .into(categoryImage);

            itemView.setOnClickListener(v -> {
                if (onCategoryClickListener != null) {
                    onCategoryClickListener.onCategoryClick(category);
                }
            });
        }
    }

    public void filter(String query) {
        List<CategoryResponseModel.CategoriesDTO> filteredList = new ArrayList<>();
        for (CategoryResponseModel.CategoriesDTO item : fullList) {
            if (item.getStrCategory().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(item);
            }
        }
        categories.clear();
        categories.addAll(filteredList);
        notifyDataSetChanged();
    }
}
