package com.example.foodplanner.aboutmeal.view;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
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
import com.example.foodplanner.aboutmeal.AboutMealContract;
import com.example.foodplanner.aboutmeal.view.AboutMealFragmentArgs;
import com.example.foodplanner.aboutmeal.presenter.AboutMealPresenter;
import com.example.foodplanner.database.sharedpreference.SharedPreferenceCashing;
import com.example.foodplanner.database.localdatabase.MealLocalDataSource;
import com.example.foodplanner.model.MealDTO;
import com.example.foodplanner.repository.mealrepository.MealRepository;
import com.example.foodplanner.model.MealResponseModel;
import com.example.foodplanner.network.api.MealRemoteApiDataSource;
import com.example.foodplanner.network.sync.MealRemoteSyncDataSource;
import com.example.foodplanner.utils.connectionutil.ConnectionUtil;
import com.example.foodplanner.utils.dateutil.DateUtil;
import com.google.android.material.snackbar.Snackbar;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;

public class AboutMealFragment extends Fragment implements AboutMealContract.IView {

    private ImageView mealImage, backToHome;
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
        presenter = new AboutMealPresenter(this, MealRepository.getInstance(MealLocalDataSource.getInstance(requireContext()), MealRemoteApiDataSource.getInstance(), MealRemoteSyncDataSource.getInstance()));
        return inflater.inflate(R.layout.fragment_about_meal, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mealImage = view.findViewById(R.id.mealImage);
        backToHome = view.findViewById(R.id.backNav);
        mealName = view.findViewById(R.id.mealName);
        mealOrigin = view.findViewById(R.id.mealOrigin);
        mealSteps = view.findViewById(R.id.mealSteps);
        ingredientsRecyclerView = view.findViewById(R.id.ingredientsRecyclerView);
        youtubePlayer = view.findViewById(R.id.youtubePlayer);
        addToFavorites = view.findViewById(R.id.addToFavorites);
        addToPlan = view.findViewById(R.id.addToPlan);

        backToHome.setOnClickListener(v -> {
            Navigation.findNavController(requireView()).navigateUp();
        });

        addToFavorites.setOnClickListener(v -> {
            if(ConnectionUtil.isConnected(requireContext())) {
                if (SharedPreferenceCashing.getInstance().getUserId() != null) {
                    MealDTO favoriteMeal = new MealDTO(meal);
                    favoriteMeal.setFavorite(true);
                    favoriteMeal.setPlanned(false);
                    favoriteMeal.setIdUser(SharedPreferenceCashing.getInstance().getUserId());
                    favoriteMeal.setDate("0");
                    favoriteMeal.setIdMeal(meal.getIdMeal());
                    presenter.storeMeal(favoriteMeal);
                    Snackbar.make(view, "Meal added to favorites!", Snackbar.LENGTH_SHORT).show();
                } else {
                    new AlertDialog.Builder(requireContext())
                            .setTitle("Sign Up Required")
                            .setMessage("You need to sign up or log in to access your profile.")
                            .setPositiveButton("Sign Up", (dialog, which) -> {
                                Navigation.findNavController(requireView()).navigate(R.id.action_aboutMealFragment_to_registrationFragment);
                            })
                            .setNegativeButton("Cancel", (dialog, which) -> {
                                dialog.dismiss();
                            })
                            .show();
                }
            }else{
                Snackbar.make(view, "No internet connection", Snackbar.LENGTH_SHORT).show();
            }
        });


        addToPlan.setOnClickListener(v -> {
            if(ConnectionUtil.isConnected(requireContext())) {
                if (SharedPreferenceCashing.getInstance().getUserId() == null) {
                    new AlertDialog.Builder(requireContext())
                            .setTitle("Sign Up Required")
                            .setMessage("You need to sign up or log in to access your profile.")
                            .setPositiveButton("Sign Up", (dialog, which) -> {
                                Navigation.findNavController(requireView()).navigate(R.id.action_aboutMealFragment_to_registrationFragment);
                            })
                            .setNegativeButton("Cancel", (dialog, which) -> {
                                dialog.dismiss();
                            })
                            .show();
                } else {
                    long[] weekRange = DateUtil.getCurrentWeekRange();
                    long weekStart = weekRange[0];
                    long weekEnd = weekRange[1];

                    Calendar calendar = Calendar.getInstance();
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH);
                    int day = calendar.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(
                            requireContext(),
                            (views, selectedYear, selectedMonth, selectedDay) -> {
                                if (DateUtil.isWithinCurrentWeek(selectedYear, selectedMonth, selectedDay, weekStart, weekEnd)) {
                                    String formattedDate = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay;
                                    MealDTO planMeal = new MealDTO(meal);
                                    planMeal.setDate(formattedDate);
                                    planMeal.setPlanned(true);
                                    planMeal.setFavorite(false);
                                    planMeal.setIdUser(SharedPreferenceCashing.getInstance().getUserId());
                                    planMeal.setIdMeal(meal.getIdMeal());
                                    presenter.storeMeal(planMeal);
                                    Snackbar.make(view, "Meal added to plan!", Snackbar.LENGTH_SHORT).show();

                                }
                            },
                            year, month, day
                    );

                    datePickerDialog.getDatePicker().setMinDate(weekStart);
                    datePickerDialog.getDatePicker().setMaxDate(weekEnd);

                    datePickerDialog.show();
                }
            }else{
                Snackbar.make(view, "No internet connection", Snackbar.LENGTH_SHORT).show();
            }
        });

        if (getArguments() != null) {
            AboutMealFragmentArgs args = AboutMealFragmentArgs.fromBundle(getArguments());
            meal = args.getMealInfo();
            mealID = args.getMealID();
        }
        if (meal != null && mealID != 0) {
            presenter.getMealById(mealID);
        }else {
            displayMealDetails(meal);
            addToFavorites.setVisibility(View.GONE);
            addToPlan.setVisibility(View.GONE);
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