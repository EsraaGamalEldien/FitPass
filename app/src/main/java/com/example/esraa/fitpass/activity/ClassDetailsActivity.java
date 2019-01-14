package com.example.esraa.fitpass.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.esraa.fitpass.R;
import com.example.esraa.fitpass.model.ClassModel;
import com.example.esraa.fitpass.presenter.ClassDetailsActivityPresenter;
import com.example.esraa.fitpass.presenter.IClassDetailsActivityPresenter;
import com.example.esraa.fitpass.util.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ClassDetailsActivity extends AppCompatActivity {
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
    private ClassModel classModel;
    private IClassDetailsActivityPresenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_details);
        ButterKnife.bind(this);
        classModel = (ClassModel) getIntent().getSerializableExtra(Constants.CLASS_MODEL);
        presenter = new ClassDetailsActivityPresenter(this);
        setUiData();
    }

    private void setUiData() {
        if (classModel == null) return;
        classDetailsDateTextView.setText(classModel.getStartDate());
        classDetailsDurationTextView.setText(classModel.getDuration());
        classDetailsInstructorTextView.setText(classModel.getInstructor());
        classDetailsLocationTextView.setText(classModel.getLat() + classModel.getLon());
        classDetailsDescriptionTextView.setText(classModel.getDescription());
    }

    @OnClick(R.id.class_subscribe_button)
    public void onClassSubscribe(View view) {
        presenter.userHasPackage(classModel);
    }

}
