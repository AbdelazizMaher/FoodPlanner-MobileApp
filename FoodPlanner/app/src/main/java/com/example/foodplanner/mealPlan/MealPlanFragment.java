package com.example.foodplanner.mealPlan;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.foodplanner.R;
import com.example.foodplanner.authentication.registration.RegistrationPresenter;
import com.example.foodplanner.authentication.sharedpreference.SharedPreferenceCashing;
import com.example.foodplanner.database.MealLocalDataSource;
import com.example.foodplanner.model.MealDTO;
import com.example.foodplanner.model.MealRepository;
import com.example.foodplanner.network.api.MealRemoteApiDataSource;
import com.example.foodplanner.network.sync.MealRemoteSyncDataSource;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MealPlanFragment extends Fragment implements MealPlanContract.IView {

    CalendarView calendarView;
    RecyclerView mealPlanRecyclerView;
    MealPlanRecyclerAdapter adapter;
    private MealPlanContract.IPresenter presenter;
    private long weekStart, weekEnd;
    LottieAnimationView lottieAnimationView;
    TextView infoText;

    public MealPlanFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        presenter = new MealPlanPresenter(this, MealRepository.getInstance(MealLocalDataSource.getInstance(requireContext()), MealRemoteApiDataSource.getInstance(), MealRemoteSyncDataSource.getInstance()));
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_meal_plan, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        infoText = view.findViewById(R.id.infoText);
        lottieAnimationView = view.findViewById(R.id.lottieAnimationView);
        calendarView = view.findViewById(R.id.calendarView);
        mealPlanRecyclerView = view.findViewById(R.id.mealPlanRecyclerView);

        if(SharedPreferenceCashing.getInstance().getUserId() == null) {
            mealPlanRecyclerView.setVisibility(View.GONE);
            lottieAnimationView.setVisibility(View.VISIBLE);
            infoText.setVisibility(View.VISIBLE);
            infoText.setText("You need to sign up or log in to access your Planned Meals");
        }else {
            mealPlanRecyclerView.setVisibility(View.VISIBLE);
            lottieAnimationView.setVisibility(View.GONE);
            infoText.setVisibility(View.GONE);
        }

        adapter = new MealPlanRecyclerAdapter(new ArrayList<>());
        mealPlanRecyclerView.setAdapter(adapter);
        adapter.setOnRemoveButtonClickListener(meal->{
            presenter.removeMealFromPlan(meal);
        });
        adapter.setOnMealClickListener(meal->{
            MealPlanFragmentDirections.ActionMealPlanFragmentToAboutMealFragment action = MealPlanFragmentDirections.actionMealPlanFragmentToAboutMealFragment(meal,0);
            Navigation.findNavController(requireView()).navigate(action);
        });

        setWeekRange();
        calendarView.setMinDate(weekStart);
        calendarView.setMaxDate(weekEnd);
        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            if (isWithinCurrentWeek(year, month, dayOfMonth)){
                String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                Log.d("Selected Date", selectedDate);
                if(SharedPreferenceCashing.getInstance().getUserId() != null) {
                    presenter.fetchPlannedMeals(SharedPreferenceCashing.getInstance().getUserId(), selectedDate);
                }
            }
        });
    }

    @Override
    public void showPlannedMeals(List<MealDTO> meals) {
        adapter.setMeals(meals);
        adapter.notifyDataSetChanged();

        lottieAnimationView.setVisibility(View.GONE);
        infoText.setVisibility(View.GONE);
        mealPlanRecyclerView.setVisibility(View.VISIBLE);
        if (meals.isEmpty()) {
            lottieAnimationView.setVisibility(View.VISIBLE);
            infoText.setVisibility(View.VISIBLE);
            mealPlanRecyclerView.setVisibility(View.GONE);
            infoText.setText("You don't have any planned meals");
        }
    }

        private void setWeekRange () {
            Calendar calendar = Calendar.getInstance();
            int today = calendar.get(Calendar.DAY_OF_WEEK);

            if (today != Calendar.SATURDAY) {
                calendar.add(Calendar.DAY_OF_WEEK, -(today - Calendar.SATURDAY + 7) % 7);
            }
            weekStart = calendar.getTimeInMillis();

            calendar.add(Calendar.DAY_OF_WEEK, 6);
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            calendar.set(Calendar.MILLISECOND, 999);
            weekEnd = calendar.getTimeInMillis();
        }

        private boolean isWithinCurrentWeek ( int year, int month, int day){
            Calendar selectedDate = Calendar.getInstance();
            selectedDate.set(year, month, day);
            long selectedTime = selectedDate.getTimeInMillis();
            return selectedTime >= weekStart && selectedTime <= weekEnd;
        }
}