package com.example.esraa.fitpass.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.esraa.fitpass.R;
import com.example.esraa.fitpass.model.GymModel;
import com.example.esraa.fitpass.util.Constants;
import com.example.esraa.fitpass.util.GetLocationManager;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GymDetailsFragment extends Fragment {

    private static final String BULLET = "\u2022 ";
    private static final String COMMA = ",";
    private static final String NEW_LINE = "\n";
    private static final int REQUEST_PHONE_CALL = 1;
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
        String activities = gymModel.getActivities().replace(COMMA, NEW_LINE + BULLET);
        gymActivitiesTextView.setText(String.format("%s%s", BULLET, activities));
        String amenities = gymModel.getAmenities().replace(COMMA, NEW_LINE + BULLET);
        gymAmenitiesTextView.setText(String.format("%s%s", BULLET, amenities));
        SpannableString website = new SpannableString(gymModel.getWebsite());
        website.setSpan(new UnderlineSpan(), 0, website.length(), 0);
        gymWebSiteTextView.setText(website);
        SpannableString phone = new SpannableString(gymModel.getPhone());
        phone.setSpan(new UnderlineSpan(), 0, phone.length(), 0);
        gymPhoneTextView.setText(phone);
        SpannableString address = new SpannableString(GetLocationManager.getAddressFromLatAndLon(
                getContext(), Double.parseDouble(gymModel.getLat())
                , Double.parseDouble(gymModel.getLon())));
        address.setSpan(new UnderlineSpan(), 0, address.length(), 0);
        gymAddressTextView.setText(address);
    }

    @OnClick(R.id.gym_details_website_textView)
    public void openWebsite(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(gymModel.getWebsite()));
        startActivity(intent);
    }

    @OnClick(R.id.gym_details_phone_textView)
    public void callDialNumber(View view) {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]
                    {Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
        } else {
            callNumber();
        }
    }

    private void callNumber() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + gymModel.getPhone()));
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) !=
                PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(callIntent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PHONE_CALL:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    callNumber();
                }
                break;
        }
    }

    @OnClick(R.id.gym_details_address_textView)
    public void navToMap(View view) {
        String uri = String.format(Locale.ENGLISH, "geo:%s,%s", gymModel.getLat(), gymModel.getLon());
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(uri)));
    }

}
