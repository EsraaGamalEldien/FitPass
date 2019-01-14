package com.example.esraa.fitpass.presenter;

import com.example.esraa.fitpass.model.GymModel;

public interface IGymDetailsActivityPresenter extends IBasePresenter {
    void addFavoriteGymToUser(GymModel gymModel);
    void deleteGymFromFavoriteList(String gymId);
}
