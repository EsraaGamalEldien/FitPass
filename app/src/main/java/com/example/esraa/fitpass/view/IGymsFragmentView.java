package com.example.esraa.fitpass.view;

import com.example.esraa.fitpass.model.GymModel;

import java.util.List;

public interface IGymsFragmentView {
    void notifyFragmentWithGymsList(List<GymModel> gymsList);

    void showNoFavoriteGymsLayout();
}
