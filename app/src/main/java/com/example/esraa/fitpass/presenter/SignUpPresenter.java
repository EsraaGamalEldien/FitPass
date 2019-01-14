package com.example.esraa.fitpass.presenter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.example.esraa.fitpass.activity.MainActivity;
import com.example.esraa.fitpass.model.UserModel;
import com.example.esraa.fitpass.util.Constants;
import com.example.esraa.fitpass.util.SharedPreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpPresenter extends BasePresenter implements ISignUpPresenter {

    private Activity activity;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;


    public SignUpPresenter(Activity activity) {
        this.activity = activity;
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();

    }

    @Override
    public void createUserAccount(String email, String pass) {
        showProgressDialog(activity);
        firebaseAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            if (null != task.getException()) {
                                showErrorAlert(activity, task.getException().getMessage());
                            }
                        } else {
                            if (null != task.getResult()) {
                                saveUserAndNavigate(task.getResult().getUser().getUid());
                            }
                        }
                    }
                });

    }

    private void saveUserAndNavigate(final String userId) {
        final UserModel user = new UserModel();
        user.setUserId(userId);
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

    private void navigateToMainActivity(String userId, String userPushedKey) {
        SharedPreferenceManager.getInstance().saveString(Constants.USER_ID, userId);
        SharedPreferenceManager.getInstance().saveString(Constants.USER_PUSHED_KEY, userPushedKey);
        hideProgressDialog();
        Intent intent = new Intent(activity, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }
}
