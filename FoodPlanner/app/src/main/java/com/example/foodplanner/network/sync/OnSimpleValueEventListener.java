package com.example.foodplanner.network.sync;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class OnSimpleValueEventListener implements ValueEventListener {
    private final OnDataChangeListener onDataChange;
    private final OnErrorListener onError;

    public OnSimpleValueEventListener(OnDataChangeListener onDataChange, OnErrorListener onError) {
        this.onDataChange = onDataChange;
        this.onError = onError;
    }

    @Override
    public void onDataChange(DataSnapshot snapshot) {
        onDataChange.onDataChange(snapshot);
    }

    @Override
    public void onCancelled(DatabaseError error) {
        onError.onError(error.toException());
    }

    public interface OnDataChangeListener {
        void onDataChange(DataSnapshot snapshot);
    }

    public interface OnErrorListener {
        void onError(Exception e);
    }
}
