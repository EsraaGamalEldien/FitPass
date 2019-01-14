package com.example.esraa.fitpass.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.esraa.fitpass.R;
import com.example.esraa.fitpass.model.GymModel;
import com.example.esraa.fitpass.util.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GymDetailsFragment extends Fragment {

    @BindView(R.id.gym_details_about_textView)
    TextView gymAboutTextView;
    @BindView(R.id.gym_details_activities_textView)
    TextView gymActivitiesTextView;
    @BindView(R.id.gym_details_amenities_textView)
    TextView gymAmenitiesTextView;
    @BindView(R.id.gym_details_website_textView)
    TextView gymWebSiteTextView;
    @BindView(R.id.gym_details_phone_textView)
    TextView gymPhoneTextView;
    @BindView(R.id.gym_details_address_textView)
    TextView gymAddressTextView;
    private String userId;
    private GymModel gymModel;

    public static GymDetailsFragment newInstance(GymModel gymModel) {
        GymDetailsFragment fragment = new GymDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.GYM_MODEL, gymModel);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gym_details, container, false);
        if (getArguments() != null && getArguments().containsKey(Constants.GYM_MODEL)) {
            gymModel = (GymModel) getArguments().getSerializable(Constants.GYM_MODEL);
        }
        ButterKnife.bind(this, view);
        setUiValues();
        return view;
    }

    private void setUiValues() {
        if (gymModel == null) return;
        gymAboutTextView.setText(gymModel.getAbout());
//        gymAboutTextView.setText(gymModel.getAbout());
//        gymAboutTextView.setText(gymModel.getAbout());
//        gymAboutTextView.setText(gymModel.getAbout());
//        gymAboutTextView.setText(gymModel.getAbout());
//        gymAboutTextView.setText(gymModel.getAbout());
//        gymAboutTextView.setText(gymModel.getAbout());
    }

}
