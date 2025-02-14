package com.example.foodplanner.aboutmeal;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.foodplanner.R;
import com.example.foodplanner.database.MealLocalDataSource;
import com.example.foodplanner.home.HomePresenter;
import com.example.foodplanner.model.MealDTO;
import com.example.foodplanner.model.MealRepository;
import com.example.foodplanner.model.MealResponseModel;
import com.example.foodplanner.network.MealRemoteDataSource;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AboutMealFragment extends Fragment implements AboutMealContract.IView {

    private ImageView mealImage;
    private TextView mealName, mealOrigin, mealSteps;
    private RecyclerView ingredientsRecyclerView;
    private WebView youtubePlayer;
    private Button addToFavorites, addToPlan;
    private MealResponseModel.MealsDTO meal;
    private int mealID;
    private AboutMealPresenter presenter;

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
        presenter = new AboutMealPresenter(this, MealRepository.getInstance(MealLocalDataSource.getInstance(requireContext()), MealRemoteDataSource.getInstance()));
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
        addToPlan = view.findViewById(R.id.addToPlan);

        addToPlan.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();

            int today = calendar.get(Calendar.DAY_OF_WEEK);

            if (today != Calendar.SATURDAY) {
                calendar.add(Calendar.DAY_OF_WEEK, Calendar.SATURDAY - today);
            }
            long weekStart = calendar.getTimeInMillis();

            calendar.add(Calendar.DAY_OF_WEEK, 6);
            long weekEnd = calendar.getTimeInMillis();

            calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    requireContext(),
                    (views, selectedYear, selectedMonth, selectedDay) -> {
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(selectedYear, selectedMonth, selectedDay);

                        if (selectedDate.getTimeInMillis() >= weekStart && selectedDate.getTimeInMillis() <= weekEnd) {
                            String formattedDate = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay;
                            MealDTO planMeal = new MealDTO(meal);
                            planMeal.setDate(formattedDate);
                            planMeal.setPlanned(true);
                            planMeal.setFavorite(false);
                            planMeal.setIdUser("1");
                            planMeal.setIdMeal(meal.getIdMeal());
                            presenter.storeMeal(planMeal);
                        } else {
                            Toast.makeText(requireContext(), "Please select a date within this week (Saturday - Friday)", Toast.LENGTH_SHORT).show();
                        }
                    },
                    year, month, day
            );

            // Restrict selectable dates to the current week
            datePickerDialog.getDatePicker().setMinDate(weekStart);
            datePickerDialog.getDatePicker().setMaxDate(weekEnd);

            datePickerDialog.show();
        });

        if (getArguments() != null) {
            AboutMealFragmentArgs args = AboutMealFragmentArgs.fromBundle(getArguments());
            meal = args.getMealInfo();
            mealID = args.getMealID();
        }
        if (meal != null && mealID != 0) {
            presenter.getMealById(mealID);
        }
    }

    @SuppressLint("SetTextI18n")
    private void displayMealDetails(MealResponseModel.MealsDTO meal) {
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

    @Override
    public void showMealDetails(MealResponseModel.MealsDTO meal) {
        displayMealDetails(meal);
    }

    @Override
    public void showError(String error) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setMessage(error).setTitle("An Error Occurred");
        AlertDialog dialog = builder.create();
        dialog.show();
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