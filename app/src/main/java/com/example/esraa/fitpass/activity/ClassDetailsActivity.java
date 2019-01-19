package com.example.esraa.fitpass.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.esraa.fitpass.R;
import com.example.esraa.fitpass.model.ClassModel;
import com.example.esraa.fitpass.presenter.ClassDetailsActivityPresenter;
import com.example.esraa.fitpass.presenter.IClassDetailsActivityPresenter;
import com.example.esraa.fitpass.util.Constants;
import com.example.esraa.fitpass.util.GetLocationManager;
import com.example.esraa.fitpass.view.IClassDetailsActivityView;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ClassDetailsActivity extends AppCompatActivity implements IClassDetailsActivityView {
    private static final int REQUEST_PHONE_CALL = 1;
    @BindView(R.id.classDetailsDate)
    TextView classDetailsDateTextView;
    @BindView(R.id.classDetailsDuration)
    TextView classDetailsDurationTextView;
    @BindView(R.id.classDetailsInstructor)
    TextView classDetailsInstructorTextView;
    @BindView(R.id.classDetailsLocation)
    TextView classDetailsLocationTextView;
    @BindView(R.id.classDetailsDescription)
    TextView classDetailsDescriptionTextView;
    @BindView(R.id.class_details_fragment_imageView)
    ImageView classImageView;
    @BindView(R.id.classDetailsPhone)
    TextView classDetailsPhoneTextView;
    private ClassModel classModel;
    private IClassDetailsActivityPresenter presenter;
    private Button classSubscriberButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_details);
        ButterKnife.bind(this);
        classModel = (ClassModel) getIntent().getSerializableExtra(Constants.CLASS_MODEL);
        presenter = new ClassDetailsActivityPresenter(this, this);
        setUiData();
        presenter.isClassSubscribed(classModel);
        inflateSubscribeButton();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setUiData() {
        if (classModel == null) return;
        classDetailsDateTextView.setText(String.format("%s, %s", classModel.getDay(), classModel.getStartDate()));
        classDetailsDurationTextView.setText(classModel.getDuration());
        classDetailsInstructorTextView.setText(classModel.getInstructor());

        SpannableString address = new SpannableString(GetLocationManager.getAddressFromLatAndLon(
                this, Double.parseDouble(classModel.getLat())
                , Double.parseDouble(classModel.getLon())));
        address.setSpan(new UnderlineSpan(), 0, address.length(), 0);
        classDetailsLocationTextView.setText(address);

        SpannableString phone = new SpannableString(classModel.getPhone());
        phone.setSpan(new UnderlineSpan(), 0, phone.length(), 0);
        classDetailsPhoneTextView.setText(phone);
        classDetailsDescriptionTextView.setText(classModel.getDescription());
        Picasso.get().load(classModel.getImageUrl()).into(classImageView);
    }

    @OnClick(R.id.address_layout)
    public void navToMaps(View view) {
        String uri = String.format(Locale.ENGLISH, "geo:%s,%s", classModel.getLat(), classModel.getLon());
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(uri)));
    }


    @OnClick(R.id.phone_layout)
    public void navToDial(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
        } else {
            callNumber();
        }
    }

    private void callNumber() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + classModel.getPhone()));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
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

    private void onClassSubscribe() {
        presenter.userHasPackage(classModel);
    }

    @Override
    public void dimSubscribeButton() {
        classSubscriberButton.setEnabled(false);
        classSubscriberButton.setClickable(false);
        classSubscriberButton.setText(R.string.subscribed);
    }

    @Override
    public void showSuccessToast() {
        Toast.makeText(getApplicationContext(), R.string.class_subscribed_successfully, Toast.LENGTH_SHORT).show();
    }

    public void inflateSubscribeButton() {
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View view = LayoutInflater.from(this).inflate(R.layout.subscriber_class_button_layout, viewGroup);
        classSubscriberButton = view.findViewById(R.id.class_subscribe_button);
        classSubscriberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClassSubscribe();
            }
        });

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
    }
}
