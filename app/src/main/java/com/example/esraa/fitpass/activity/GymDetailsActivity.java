package com.example.esraa.fitpass.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.example.esraa.fitpass.R;
import com.example.esraa.fitpass.adapter.ViewPagerAdapter;
import com.example.esraa.fitpass.fragment.ClassesFragment;
import com.example.esraa.fitpass.fragment.GymDetailsFragment;
import com.example.esraa.fitpass.model.GymModel;
import com.example.esraa.fitpass.presenter.GymDetailsActivityPresenter;
import com.example.esraa.fitpass.presenter.IGymDetailsActivityPresenter;
import com.example.esraa.fitpass.util.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GymDetailsActivity extends AppCompatActivity {
    @BindView(R.id.gym_tabs)
    TabLayout gymTabs;
    @BindView(R.id.gymDetails_viewpager)
    ViewPager viewPager;
    @BindView(R.id.favorite_toggle_button)
    ToggleButton favToggleButton;
    private GymModel gymModel;
    private IGymDetailsActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym_details);
        ButterKnife.bind(this);
        gymModel = (GymModel) getIntent().getSerializableExtra(Constants.GYM_MODEL);
        presenter = new GymDetailsActivityPresenter(this);
        setupViewPager(gymModel);
        gymTabs.setupWithViewPager(viewPager);
        setUiValues();
    }

    private void setUiValues() {
        favToggleButton.setOnCheckedChangeListener(null);
        if (gymModel.isFavourite()) {
            favToggleButton.setChecked(true);
        } else {
            favToggleButton.setChecked(false);
        }
        setFavToggleButtonListener();
    }

    private void setFavToggleButtonListener() {
        favToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    presenter.addFavoriteGymToUser(gymModel);
                } else {
                    presenter.deleteGymFromFavoriteList(gymModel.getGymId());
                }

            }
        });

    }

    private void setupViewPager(GymModel gymModel) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(GymDetailsFragment.newInstance(gymModel), "Info");
        adapter.addFragment(new ClassesFragment(), "Classes");
        viewPager.setAdapter(adapter);
    }
}
