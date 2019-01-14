package com.example.esraa.fitpass.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.esraa.fitpass.model.GymModel;
import com.example.esraa.fitpass.model.UserModel;
import com.example.esraa.fitpass.util.Constants;
import com.example.esraa.fitpass.util.SharedPreferenceManager;
import com.example.esraa.fitpass.view.IGymsFragmentView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class GymsFragmentPresenter extends BasePresenter implements IGymsFragmentPresenter {

    private Context context;
    private DatabaseReference databaseReference;
    private IGymsFragmentView fragmentView;

    public GymsFragmentPresenter(Context context, IGymsFragmentView fragmentView) {
        this.context = context;
        this.fragmentView = fragmentView;
        databaseReference = FirebaseDatabase.getInstance().getReference();

    }

    @Override
    public void getAllGyms() {
        showProgressDialog(context);
        databaseReference.child(Constants.GYMS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                hideProgressDialog();
                GenericTypeIndicator<List<GymModel>> genericTypeIndicator =
                        new GenericTypeIndicator<List<GymModel>>() {
                        };
                List<GymModel> gymsList = snapshot.getValue(genericTypeIndicator);
                getAllFavoriteGyms(gymsList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                showErrorAlert(context, databaseError.getMessage());
            }
        });

    }

    public void getAllFavoriteGyms(final List<GymModel> allGyms) {
        String userId = SharedPreferenceManager.getInstance().getString(Constants.USER_ID);
        showProgressDialog(context);
        Query queryRef = databaseReference.child(Constants.USERS)
                .orderByChild(Constants.USER_ID)
                .equalTo(userId);
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                hideProgressDialog();

                GenericTypeIndicator<UserModel> genericTypeIndicator =
                        new GenericTypeIndicator<UserModel>() {
                        };

                UserModel user = dataSnapshot.getValue(genericTypeIndicator);
                if (allGyms == null) {
                    if (user != null && user.getFavouriteGyms() != null &&
                            user.getFavouriteGyms().size() > 0) {
                        handleFavoriteListAndNotifyAdapter(user.getFavouriteGyms());
                    } else {
                        fragmentView.showNoFavoriteGymsLayout();
                    }
                } else {
                    if (user != null) {
                        handleGymsListWithFavoriteList(allGyms, user.getFavouriteGyms());
                    }
                }
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

    private void handleGymsListWithFavoriteList(List<GymModel> allGyms,
                                                List<GymModel> favouriteGyms) {
        if (allGyms != null && favouriteGyms != null) {
            for (int i = 0; i < allGyms.size(); i++) {
                for (int j = 0; j < favouriteGyms.size(); j++) {
                    if (allGyms.get(i).getGymId().equalsIgnoreCase(favouriteGyms.get(j).getGymId())) {
                        allGyms.get(i).setFavourite(true);
                    }
                }
            }
        }
        fragmentView.notifyFragmentWithGymsList(allGyms);
    }

    private void handleFavoriteListAndNotifyAdapter(List<GymModel> favouriteGyms) {
        for (int i = 0; i < favouriteGyms.size(); i++) {
            favouriteGyms.get(i).setFavourite(true);
        }
        fragmentView.notifyFragmentWithGymsList(favouriteGyms);
    }

}
