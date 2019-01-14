package com.example.esraa.fitpass.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.esraa.fitpass.model.GymModel;
import com.example.esraa.fitpass.util.Constants;
import com.example.esraa.fitpass.util.SharedPreferenceManager;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class GymDetailsActivityPresenter extends BasePresenter implements IGymDetailsActivityPresenter {

    private DatabaseReference databaseReference;
    private Context context;
    private String userId;

    public GymDetailsActivityPresenter(Context context) {
        this.context = context;
        databaseReference = FirebaseDatabase.getInstance().getReference();
        userId = SharedPreferenceManager.getInstance().getString(Constants.USER_ID);
    }

    @Override
    public void addFavoriteGymToUser(final GymModel gymModel) {
        showProgressDialog(context);
        getUserKeyAndSave(gymModel);
    }

    private void getUserKeyAndSave(final GymModel gymModel) {
        Query mQueryRef = databaseReference.child(Constants.USERS)
                .orderByChild(Constants.USER_ID)
                .equalTo(userId);
        mQueryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                saveFavoriteGym(dataSnapshot.getKey(), gymModel);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                showErrorAlert(context, databaseError.getMessage());
            }

        });
    }

    private void saveFavoriteGym(String key, GymModel gymModel) {
        databaseReference.child(Constants.USERS).child(key).child("favouriteGyms").
                push().setValue(gymModel,
                new DatabaseReference.CompletionListener() {
                    public void onComplete(DatabaseError error,
                                           @NonNull DatabaseReference databaseReference) {
                        hideProgressDialog();
                        if (error != null) {
                            showErrorAlert(context, error.getMessage());
                        }
                    }
                });
    }


    public void deleteGymFromFavoriteList(String gymId) {
        showProgressDialog(context);
        getUserKeyAndDelete(gymId);
    }

    private void getUserKeyAndDelete(final String gymId) {
        Query mQueryRef = databaseReference.child(Constants.USERS)
                .orderByChild(Constants.USER_ID)
                .equalTo(userId);
        mQueryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                removeFavoriteGym(dataSnapshot.getKey(), gymId);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                showErrorAlert(context, databaseError.getMessage());
            }

        });
    }

    private void removeFavoriteGym(String key, String gymId) {
        Query query = databaseReference.child(Constants.USERS).child(key).
                child(Constants.FAV_GYMS).
                orderByChild(Constants.GYM_ID).equalTo(gymId);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                hideProgressDialog();
                for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                showErrorAlert(context, databaseError.getMessage());
            }
        });
    }
}
