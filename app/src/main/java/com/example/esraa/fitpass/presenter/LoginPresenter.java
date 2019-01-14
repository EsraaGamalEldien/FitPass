package com.example.esraa.fitpass.presenter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.example.esraa.fitpass.activity.MainActivity;
import com.example.esraa.fitpass.model.UserModel;
import com.example.esraa.fitpass.util.Constants;
import com.example.esraa.fitpass.util.SharedPreferenceManager;
import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginPresenter extends BasePresenter implements ILoginPresenter {


    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private Activity activity;


    public LoginPresenter(Activity activity) {
        this.activity = activity;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        firebaseAuth = FirebaseAuth.getInstance();
    }


    public void saveUserAndNavigate(final String userId) {
        showProgressDialog(activity);
        final UserModel user = new UserModel();
        user.setUserId(userId);
        Query query = databaseReference.child("users").orderByChild("userId").equalTo(userId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    navigateToMainActivity(userId, null);
                } else {
                    databaseReference.child("users").push().setValue(user,
                            new DatabaseReference.CompletionListener() {
                                public void onComplete(DatabaseError error,
                                                       @NonNull DatabaseReference databaseReference) {
                                    if (error != null) {
                                        showErrorAlert(activity, error.getMessage());
                                    } else {
                                        navigateToMainActivity(userId, databaseReference.getKey());
                                    }
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            if (account != null) {
                saveUserAndNavigate(account.getId());
            }
        } catch (ApiException e) {
            showErrorAlert(activity, e.getMessage());
        }
    }

    @Override
    public void checkIfUserFaceBookLoggedIn() {
        // TODO: 1/7/2019 move to splash activity
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        if (isLoggedIn) {
            navigateToMainActivity(accessToken.getUserId(), null);
        }
    }


    @Override
    public void signInWithMailAndPass(String mail, String pass) {
        showProgressDialog(activity);
        firebaseAuth.signInWithEmailAndPassword(mail, pass)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            if (task.getException() != null) {
                                showErrorAlert(activity, task.getException().getMessage());
                            }
                        } else {
                            if (null != task.getResult()) {
                                navigateToMainActivity(task.getResult().getUser().getUid(), null);
                            }
                        }
                    }
                });
    }


    private void navigateToMainActivity(String userId, String userPushedKey) {
        SharedPreferenceManager.getInstance().saveString(Constants.USER_ID, userId);
        if (userPushedKey != null) {
            SharedPreferenceManager.getInstance().saveString(Constants.USER_PUSHED_KEY, userPushedKey);
        }
        hideProgressDialog();
        Intent intent = new Intent(activity, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }
}
