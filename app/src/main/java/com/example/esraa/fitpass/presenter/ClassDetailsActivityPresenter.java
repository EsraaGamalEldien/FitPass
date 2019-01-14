package com.example.esraa.fitpass.presenter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

import com.example.esraa.fitpass.model.ClassModel;
import com.example.esraa.fitpass.model.UserModel;
import com.example.esraa.fitpass.util.Constants;
import com.example.esraa.fitpass.util.SharedPreferenceManager;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;

public class ClassDetailsActivityPresenter extends BasePresenter implements IClassDetailsActivityPresenter {

    private Context context;
    private String userId;
    private DatabaseReference databaseReference;
    private int numOfClasses = 12;

    public ClassDetailsActivityPresenter(Context context) {
        this.context = context;
        userId = SharedPreferenceManager.getInstance().getString(Constants.USER_ID);
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void userHasPackage(final ClassModel classModel) {
        showProgressDialog(context);
        Query mQueryRef = databaseReference.child(Constants.USERS)
                .orderByChild(Constants.USER_ID)
                .equalTo(userId);
        mQueryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                handleUserPackage(dataSnapshot, classModel);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                showErrorAlert(context, databaseError.getMessage());
            }

        });

    }

    private void handleUserPackage(DataSnapshot dataSnapshot, ClassModel classModel) {
        GenericTypeIndicator<UserModel> genericTypeIndicator =
                new GenericTypeIndicator<UserModel>() {
                };

        UserModel userModel = dataSnapshot.getValue(genericTypeIndicator);
        if (userModel != null && userModel.isHasPackage() && userModel.getUserClasses().size() <= numOfClasses) {
            subscribeToClass(classModel, dataSnapshot.getKey());
        } else {
            showSelectingPackageDialog(dataSnapshot.getKey(), classModel);
        }
    }

    private void subscribeToClass(ClassModel classModel, String key) {
        hideProgressDialog();
        databaseReference.child(Constants.USERS).child(key).child(Constants.USER_CLASSES).
                push().setValue(classModel,
                new DatabaseReference.CompletionListener() {
                    public void onComplete(DatabaseError error,
                                           @NonNull DatabaseReference databaseReference) {
                        if (error != null) {
                            showErrorAlert(context, error.getMessage());
                        }
                    }
                });
    }

    private void showSelectingPackageDialog(final String key, final ClassModel classModel) {
        hideProgressDialog();
        final AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(context);
        builder.setTitle("packages")
                .setMessage("do you want to subscribe to package  12 class with 2 $")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        setUserPackage(key, true, classModel);
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setCancelable(true)
                .show();
    }

    private void setUserPackage(final String key, boolean hasPackage, final ClassModel classModel) {
        showProgressDialog(context);
        databaseReference.child(Constants.USERS).child(key).child(Constants.HAS_PACKAGE).
                push().setValue(hasPackage,
                new DatabaseReference.CompletionListener() {
                    public void onComplete(DatabaseError error,
                                           @NonNull DatabaseReference databaseReference) {
                        hideProgressDialog();
                        if (error != null) {
                            showErrorAlert(context, error.getMessage());
                        } else {
                            subscribeToClass(classModel, key);
                        }
                    }
                });

    }
}
