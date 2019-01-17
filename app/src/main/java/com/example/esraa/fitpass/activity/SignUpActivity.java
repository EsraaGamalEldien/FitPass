package com.example.esraa.fitpass.activity;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.esraa.fitpass.R;
import com.example.esraa.fitpass.presenter.ISignUpPresenter;
import com.example.esraa.fitpass.presenter.SignUpPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpActivity extends AppCompatActivity {

    private final String TAG = SignUpActivity.this.getClass().getSimpleName();
    @BindView(R.id.email_editText)
    TextInputEditText emailEditText;
    @BindView(R.id.password_editText)
    TextInputEditText passEditText;
    @BindView(R.id.password_layout_text_input)
    TextInputLayout passwordTextInput;
    @BindView(R.id.email_layout_text_input)
    TextInputLayout emailTextInput;
    private ISignUpPresenter signUpPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        signUpPresenter = new SignUpPresenter(this);
    }

    @OnClick(R.id.signUp_button)
    public void signUp(View view) {
        String email = emailEditText.getText().toString();
        String pass = passEditText.getText().toString();

        if (email.isEmpty()) {
            emailTextInput.setError(getString(R.string.mandatory_field));
            emailTextInput.setBackgroundColor(getResources().getColor(R.color.white));
        }
        if (pass.isEmpty()) {
            passwordTextInput.setError(getString(R.string.mandatory_field));
            passwordTextInput.setBackgroundColor(getResources().getColor(R.color.white));

        } else if (pass.length() < 6) {
            passwordTextInput.setError(getString(R.string.validation_pass_6_char));
            passwordTextInput.setBackgroundColor(getResources().getColor(R.color.white));

        }
        if (!email.isEmpty() && !pass.isEmpty() && pass.length() >= 6) {
            signUpPresenter.createUserAccount(email, pass);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
