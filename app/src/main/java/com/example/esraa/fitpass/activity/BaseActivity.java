package com.example.esraa.fitpass.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.esraa.fitpass.R;
import com.example.esraa.fitpass.presenter.BasePresenter;
import com.example.esraa.fitpass.presenter.IBasePresenter;

public class BaseActivity extends AppCompatActivity {

    private IBasePresenter basePresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        basePresenter = new BasePresenter();
    }


    @Override
    protected void onPause() {
        super.onPause();
        basePresenter.hideProgressDialog();
    }
}
