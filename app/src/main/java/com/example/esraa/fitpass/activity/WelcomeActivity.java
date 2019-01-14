package com.example.esraa.fitpass.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.esraa.fitpass.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.getStarted_button)
    public void navigateToSignUp(View view) {
        startActivity(new Intent(this, SignUpActivity.class));
    }

    @OnClick(R.id.logIn_textView)
    public void navigateToLogIn(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }
}
