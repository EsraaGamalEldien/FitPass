package com.example.esraa.fitpass.presenter;

import com.example.esraa.fitpass.model.GymModel;

import java.util.List;

public interface IGymsFragmentPresenter extends IBasePresenter {
    void getAllGyms();

    void getAllFavoriteGyms(List<GymModel>allGyms);
}
