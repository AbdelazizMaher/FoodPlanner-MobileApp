package com.example.foodplanner.network.sync;

import com.example.foodplanner.model.MealDTO;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public class MealRemoteSyncDataSource {
    private DatabaseReference databaseReference;
    private static MealRemoteSyncDataSource instance = null;

    private MealRemoteSyncDataSource() {
        databaseReference = FirebaseDatabase.getInstance().getReference("meals");
    }

    public static MealRemoteSyncDataSource getInstance() {
        if (instance == null) {
            instance = new MealRemoteSyncDataSource();
        }
        return instance;
    }

    public Completable insertMeal(MealDTO meal) {
        String key = generateKey(meal);
        return Completable.create(emitter ->
                databaseReference.child(key).setValue(meal)
                        .addOnSuccessListener(aVoid -> emitter.onComplete())
                        .addOnFailureListener(emitter::onError)
        );
    }

    public Completable deleteMeal(MealDTO meal) {
        String key = generateKey(meal);
        return Completable.create(emitter ->
                databaseReference.child(key).removeValue()
                        .addOnSuccessListener(aVoid -> emitter.onComplete())
                        .addOnFailureListener(emitter::onError)
        );
    }

    public Single<List<MealDTO>> getAllMeals()  {
        return Single.create(emitter ->
                databaseReference.addListenerForSingleValueEvent(new OnSimpleValueEventListener(dataSnapshot -> {
                    List<MealDTO> meals = new ArrayList<>();
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        MealDTO meal = data.getValue(MealDTO.class);
                        if (meal != null) {
                            meals.add(meal);
                        }
                    }
                    emitter.onSuccess(meals);
                }, emitter::onError))
        );
    }

    private String generateKey(MealDTO meal) {
        return meal.getIdUser() + "_" + meal.getIdMeal() + "_" + meal.getDate();
    }
}
