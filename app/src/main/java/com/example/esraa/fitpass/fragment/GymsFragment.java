package com.example.esraa.fitpass.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.esraa.fitpass.R;
import com.example.esraa.fitpass.adapter.GymsAdapter;
import com.example.esraa.fitpass.model.GymModel;
import com.example.esraa.fitpass.presenter.GymsFragmentPresenter;
import com.example.esraa.fitpass.presenter.IGymsFragmentPresenter;
import com.example.esraa.fitpass.util.Constants;
import com.example.esraa.fitpass.view.IGymsFragmentView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GymsFragment extends Fragment implements IGymsFragmentView {
    @BindView(R.id.gyms_recycler_view)
    RecyclerView gymsRecyclerView;
    @BindView(R.id.no_favorite_textView)
    TextView noFavoritesTextView;
    private GymsAdapter gymsAdapter;
    private IGymsFragmentPresenter presenter;
    private boolean isFavorite;

    public static GymsFragment newInstance(boolean isFavorite) {
        GymsFragment fragment = new GymsFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constants.IS_FAV, isFavorite);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gyms, container, false);
        if (getArguments() != null && getArguments().containsKey(Constants.IS_FAV)) {
            isFavorite = getArguments().getBoolean(Constants.IS_FAV);
        }
        ButterKnife.bind(this, view);
        setupRecyclerAndAdapter();
        presenter = new GymsFragmentPresenter(getContext(), this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isFavorite) {
            presenter.getAllFavoriteGyms(null);
        } else {
            presenter.getAllGyms();
        }
    }

    private void setupRecyclerAndAdapter() {
        List<GymModel> gymModels = new ArrayList<>();
        gymsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        gymsAdapter = new GymsAdapter(gymModels,getActivity());
        gymsRecyclerView.setAdapter(gymsAdapter);
    }

    @Override
    public void notifyFragmentWithGymsList(List<GymModel> gymsList) {
        hideNoFavoritesLayoutAndShowRecycler();
        gymsAdapter.setGymModelList(gymsList);
        gymsAdapter.notifyDataSetChanged();
    }

    @Override
    public void showNoFavoriteGymsLayout() {
        gymsRecyclerView.setVisibility(View.GONE);
        noFavoritesTextView.setVisibility(View.VISIBLE);
    }

    private void hideNoFavoritesLayoutAndShowRecycler() {
        gymsRecyclerView.setVisibility(View.VISIBLE);
        noFavoritesTextView.setVisibility(View.GONE);
    }
}
