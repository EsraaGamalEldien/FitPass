package com.example.esraa.fitpass.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ToggleButton;

import com.example.esraa.fitpass.R;
import com.example.esraa.fitpass.adapter.ViewPagerAdapter;
import com.example.esraa.fitpass.fragment.ClassesFragment;
import com.example.esraa.fitpass.fragment.GymDetailsFragment;
import com.example.esraa.fitpass.model.GymModel;
import com.example.esraa.fitpass.presenter.GymDetailsActivityPresenter;
import com.example.esraa.fitpass.presenter.IGymDetailsActivityPresenter;
import com.example.esraa.fitpass.util.Constants;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GymDetailsActivity extends AppCompatActivity {
    @BindView(R.id.gym_tabs)
    TabLayout gymTabs;
    @BindView(R.id.gymDetails_viewpager)
    ViewPager viewPager;
    @BindView(R.id.favorite_toggle_button)
    ToggleButton favToggleButton;
    @BindView(R.id.gym_details_photo)
    ImageView gymPhotoImageView;
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
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setUiValues() {
        Picasso.get().load(gymModel.getImagesUrls().get(0)).into(gymPhotoImageView);
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
        adapter.addFragment(GymDetailsFragment.newInstance(gymModel), getString(R.string.info));
        adapter.addFragment(ClassesFragment.newInstance(gymModel.getGymClasses()), getString(R.string.classes));
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
