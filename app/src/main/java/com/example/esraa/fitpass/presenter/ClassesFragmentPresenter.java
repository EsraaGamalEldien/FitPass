package com.example.esraa.fitpass.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.esraa.fitpass.model.ClassModel;
import com.example.esraa.fitpass.util.Constants;
import com.example.esraa.fitpass.view.IClassesFragmentView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ClassesFragmentPresenter extends BasePresenter implements IClassesFragmentPresenter {

    private Context context;
    private DatabaseReference databaseReference;
    private IClassesFragmentView fragmentView;

    public ClassesFragmentPresenter(Context context, IClassesFragmentView fragmentView) {
        this.context = context;
        this.fragmentView = fragmentView;
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void getAllClassesFromFireBase() {
        showProgressDialog(context);
        databaseReference.child(Constants.CLASSES).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                hideProgressDialog();
                GenericTypeIndicator<List<ClassModel>> genericTypeIndicator =
                        new GenericTypeIndicator<List<ClassModel>>() {
                        };
                List<ClassModel> classesList = snapshot.getValue(genericTypeIndicator);
                fragmentView.notifyFragmentWithClassesList(classesList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                showErrorAlert(context, databaseError.getMessage());
            }
        });

    }
}
