package com.example.foodplanner.aboutmeal;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.foodplanner.R;
import com.example.foodplanner.model.MealResponseModel;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class AboutMealFragment extends Fragment {

    private ImageView mealImage;
    private TextView mealName, mealOrigin, mealSteps;
    private RecyclerView ingredientsRecyclerView;
    private WebView youtubePlayer;
    private Button addToFavorites, removeFromFavorites;

    private MealResponseModel.MealsDTO meal;

    public AboutMealFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about_meal, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mealImage = view.findViewById(R.id.mealImage);
        mealName = view.findViewById(R.id.mealName);
        mealOrigin = view.findViewById(R.id.mealOrigin);
        mealSteps = view.findViewById(R.id.mealSteps);
        ingredientsRecyclerView = view.findViewById(R.id.ingredientsRecyclerView);
        youtubePlayer = view.findViewById(R.id.youtubePlayer);
        addToFavorites = view.findViewById(R.id.addToFavorites);
        removeFromFavorites = view.findViewById(R.id.removeFromFavorites);

        if (getArguments() != null) {
            AboutMealFragmentArgs args = AboutMealFragmentArgs.fromBundle(getArguments());
            meal = args.getMealInfo();
        }
        if (meal != null) {
            displayMealDetails();
        }
    }

    @SuppressLint("SetTextI18n")
    private void displayMealDetails() {
        Glide.with(requireContext()).load(meal.getStrMealThumb()).into(mealImage);

        mealName.setText(meal.getStrMeal());
        mealOrigin.setText("Origin: " + meal.getStrArea());

        mealSteps.setText(meal.getStrInstructions());

        ArrayList<IngredientModel> ingredients = getIngredientsList(meal);
        Log.d("Ingredients", String.valueOf(ingredients.size()));
        IngredientDetailRecyclerAdapter adapter = new IngredientDetailRecyclerAdapter(ingredients);
        ingredientsRecyclerView.setAdapter(adapter);

        loadYouTubeVideo(meal.getStrYoutube());

    }
    public class IngredientModel {
        private String name;
        private String quantity;
        private String imageUrl;

        public IngredientModel(String name, String quantity) {
            this.name = name;
            this.quantity = quantity;
            this.imageUrl = "https://www.themealdb.com/images/ingredients/" + name + "-Small.png";
        }

        public String getName() { return name; }
        public String getQuantity() { return quantity; }
        public String getImageUrl() { return imageUrl; }
    }

    private ArrayList<IngredientModel> getIngredientsList(MealResponseModel.MealsDTO meal) {
        ArrayList<IngredientModel> ingredients = new ArrayList<>();

        for (int i = 1; i <= 20; i++) {
            try {
                Method ingredientMethod = meal.getClass().getMethod("getStrIngredient" + i);
                Method measureMethod = meal.getClass().getMethod("getStrMeasure" + i);

                String ingredient = (String) ingredientMethod.invoke(meal);
                String measure = (String) measureMethod.invoke(meal);

                if (ingredient != null && !ingredient.trim().isEmpty()) {
                    ingredients.add(new IngredientModel(ingredient, measure != null ? measure : "N/A"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ingredients;
    }

    private void loadYouTubeVideo(String url) {
        if (!TextUtils.isEmpty(url)) {
            String videoId = url.split("v=")[1];
            String embedUrl = "https://www.youtube.com/embed/" + videoId;

            youtubePlayer.getSettings().setJavaScriptEnabled(true);
            youtubePlayer.getSettings().setPluginState(WebSettings.PluginState.ON);
            youtubePlayer.setWebViewClient(new WebViewClient());
            youtubePlayer.loadUrl(embedUrl);
        }
    }
}