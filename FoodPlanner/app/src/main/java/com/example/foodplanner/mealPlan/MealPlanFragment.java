package com.example.foodplanner.mealPlan;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import com.example.foodplanner.R;
import com.example.foodplanner.aboutmeal.AboutMealPresenter;
import com.example.foodplanner.database.MealLocalDataSource;
import com.example.foodplanner.model.MealDTO;
import com.example.foodplanner.model.MealRepository;
import com.example.foodplanner.network.MealRemoteDataSource;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MealPlanFragment extends Fragment implements MealPlanContract.IView {

    CalendarView calendarView;
    RecyclerView mealPlanRecyclerView;
    MealPlanRecyclerAdapter adapter;
    private MealPlanContract.IPresenter presenter;
    private long weekStart, weekEnd;

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
        presenter = new MealPlanPresenter(this, MealRepository.getInstance(MealLocalDataSource.getInstance(requireContext()), MealRemoteDataSource.getInstance()));
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_meal_plan, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        calendarView = view.findViewById(R.id.calendarView);
        mealPlanRecyclerView = view.findViewById(R.id.mealPlanRecyclerView);

        adapter = new MealPlanRecyclerAdapter(new ArrayList<>());
        mealPlanRecyclerView.setAdapter(adapter);

        setWeekRange();
        calendarView.setMinDate(weekStart);
        calendarView.setMaxDate(weekEnd);
        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            if (isWithinCurrentWeek(year, month, dayOfMonth)){
                String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                presenter.fetchPlannedMeals("1", selectedDate);
            }else {
                Toast.makeText(requireContext(), "Please select a date within this week (Saturday - Friday)", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void showPlannedMeals(List<MealDTO> meals) {
        adapter.setMeals(meals);
        adapter.notifyDataSetChanged();
    }

    private void setWeekRange() {
        Calendar calendar = Calendar.getInstance();
        int today = calendar.get(Calendar.DAY_OF_WEEK);

        if (today != Calendar.SATURDAY) {
            calendar.add(Calendar.DAY_OF_WEEK, Calendar.SATURDAY - today);
        }
        weekStart = calendar.getTimeInMillis();

        calendar.add(Calendar.DAY_OF_WEEK, 6);
        weekEnd = calendar.getTimeInMillis();
    }

    private boolean isWithinCurrentWeek(int year, int month, int day) {
        Calendar selectedDate = Calendar.getInstance();
        selectedDate.set(year, month, day);
        long selectedTime = selectedDate.getTimeInMillis();
        return selectedTime >= weekStart && selectedTime <= weekEnd;
    }
}