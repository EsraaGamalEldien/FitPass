package com.example.esraa.fitpass.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.esraa.fitpass.R;
import com.example.esraa.fitpass.presenter.ILoginPresenter;
import com.example.esraa.fitpass.presenter.LoginPresenter;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {
    private static final int GOOGLE_SIGN_IN = 1;
    private final String TAG = LoginActivity.this.getClass().getSimpleName();
    @BindView(R.id.fb_login_button)
    LoginButton fbLoginButton;
    @BindView(R.id.email_editText)
    TextInputEditText userNameEditText;
    @BindView(R.id.password_editText)
    TextInputEditText passwordEditText;
    @BindView(R.id.password_layout_text_input)
    TextInputLayout passwordTextInput;
    @BindView(R.id.email_layout_text_input)
    TextInputLayout emailTextInput;
    private ILoginPresenter presenter;
    private CallbackManager callbackManager;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        presenter = new LoginPresenter(this);
        presenter.checkIfUserFaceBookLoggedIn();
        registerFacebookCallbackLogin();
        registerGoogleSignIn();
    }

    public void registerFacebookCallbackLogin() {
        callbackManager = CallbackManager.Factory.create();
        fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                presenter.saveUserAndNavigate(loginResult.getAccessToken().getUserId());
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException exception) {
            }
        });
    }

    private void registerGoogleSignIn() {
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder
                (GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
    }


    @OnClick(R.id.google_login_button)
    public void googleSignIn(View view) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN);
    }

    @OnClick(R.id.getStarted_textView)
    public void navigateToSignUp(View view) {
        startActivity(new Intent(this, SignUpActivity.class));
    }

    @OnClick(R.id.login_button)
    public void loginWithMail(View view) {
        String mail = userNameEditText.getText().toString();
        String pass = passwordEditText.getText().toString();
        if (mail.isEmpty()) {
            emailTextInput.setError("mandatory field");
            emailTextInput.setBackgroundColor(getResources().getColor(R.color.white));
        }
        if (pass.isEmpty()) {
            passwordTextInput.setError("mandatory field");
            passwordTextInput.setBackgroundColor(getResources().getColor(R.color.white));

        } else if (pass.length() < 6) {
            passwordTextInput.setError("password shouldn't be less than 6 characters");
            passwordTextInput.setBackgroundColor(getResources().getColor(R.color.white));

        }
        if (!mail.isEmpty() && !pass.isEmpty()) {
            presenter.signInWithMailAndPass(mail, pass);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GOOGLE_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            presenter.handleSignInResult(task);
        }
    }


}
