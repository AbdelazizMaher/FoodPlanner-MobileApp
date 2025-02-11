package com.example.foodplanner.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodplanner.R;
import com.example.foodplanner.model.AreaResponseModel;

import java.util.ArrayList;

public class AreaRecyclerAdapter extends RecyclerView.Adapter<AreaRecyclerAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<AreaResponseModel.MealsDTO> countries;
    private OnAreaClickListener onAreaClickListener;

    public AreaRecyclerAdapter(Context context, ArrayList<AreaResponseModel.MealsDTO> countries) {
        this.countries = countries;
    }

    public void setOnCountryClickListener(OnAreaClickListener listener) {
        this.onAreaClickListener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_area_custom, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        AreaResponseModel.MealsDTO country = countries.get(position);
        holder.bind(country);
    }

    @Override
    public int getItemCount() {
        return countries.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView countryImage;
        private TextView countryName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            countryImage = itemView.findViewById(R.id.countryImage);
            countryName = itemView.findViewById(R.id.countryName);
        }

        public void bind(AreaResponseModel.MealsDTO country) {
            countryName.setText(country.getStrArea());

            Glide.with(countryImage.getContext())
                    .load(R.drawable.profile)
                    .into(countryImage);

            itemView.setOnClickListener(v -> {
                if (onAreaClickListener != null) {
                    onAreaClickListener.onAreaClick(country);
                }
            });
        }
    }
}
