package com.example.esraa.fitpass.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.example.esraa.fitpass.R;
import com.example.esraa.fitpass.presenter.ILoginPresenter;
import com.example.esraa.fitpass.presenter.LoginPresenter;
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

public class LoginActivity extends BaseActivity {
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
        presenter.checkIfUserLoggedIn();
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
                Log.d(TAG,exception.getMessage());
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
        if (TextUtils.isEmpty(mail)) {
            emailTextInput.setError(getString(R.string.mandatory_field));
            emailTextInput.setBackgroundColor(getResources().getColor(R.color.white));
        }
        if (TextUtils.isEmpty(pass)) {
            passwordTextInput.setError(getString(R.string.mandatory_field));
            passwordTextInput.setBackgroundColor(getResources().getColor(R.color.white));

        } else if (pass.length() < 6) {
            passwordTextInput.setError(getString(R.string.validation_pass_6_char));
            passwordTextInput.setBackgroundColor(getResources().getColor(R.color.white));

        }
        if (!TextUtils.isEmpty(mail) && !TextUtils.isEmpty(pass) && pass.length() >= 6) {
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

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        presenter.hideProgressDialog();
        super.onDestroy();
        presenter.deleteEventListenerOfDatabase();
    }
}
