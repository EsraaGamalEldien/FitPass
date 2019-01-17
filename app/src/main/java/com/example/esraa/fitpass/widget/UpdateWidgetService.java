package com.example.esraa.fitpass.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.widget.RemoteViews;

import com.example.esraa.fitpass.R;
import com.example.esraa.fitpass.model.ClassModel;
import com.example.esraa.fitpass.util.Constants;
import com.example.esraa.fitpass.util.SharedPreferenceManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Random;

public class UpdateWidgetService extends JobIntentService {

    public static final int JOB_ID = 1;

    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, UpdateWidgetService.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this
                .getApplicationContext());

        int[] allWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);

        for (int widgetId : allWidgetIds) {
            if (SharedPreferenceManager.getInstance().getString(Constants.USER_ID) != null) {
                getAllClassesFromFireBase();
            }

            // Register an onClickListener
//            Intent clickIntent = new Intent(this.getApplicationContext(),
//                    AppWidgetProvider.class);
//
//            clickIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
//            clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,
//                    allWidgetIds);
//
//            PendingIntent pendingIntent = PendingIntent.getBroadcast(
//                    getApplicationContext(), 0, clickIntent,
//                    PendingIntent.FLAG_UPDATE_CURRENT);
//            remoteViews.setOnClickPendingIntent(R.id.class_title_textView, pendingIntent);
//            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }

    private void getAllClassesFromFireBase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child(Constants.CLASSES).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                GenericTypeIndicator<List<ClassModel>> genericTypeIndicator =
                        new GenericTypeIndicator<List<ClassModel>>() {
                        };
                List<ClassModel> classesList = snapshot.getValue(genericTypeIndicator);
                int randomNumber;
                if (classesList != null) {
                    randomNumber = (new Random().nextInt(classesList.size()));
                    ClassModel classModel = classesList.get(randomNumber);
                    updateWidgetLayoutWithRandomClass(classModel);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    private void updateWidgetLayoutWithRandomClass(ClassModel classModel) {
        RemoteViews remoteViews = new RemoteViews(this.getApplicationContext().getPackageName(),
                R.layout.layout_widget);
//        int classIcon = R.drawable.ic_sport_shoes_icon;
//        if (classModel.getType().equalsIgnoreCase(String.valueOf(ClassModel.ClassType.CYCLING))) {
//            classIcon = R.drawable.ic_cycling_icon;
//        } else if (classModel.getType().equalsIgnoreCase(String.valueOf(ClassModel.ClassType.YOGA))) {
//            classIcon = R.drawable.ic_yoga_icon;
//        }
        remoteViews.setTextViewText(R.id.widget_class_title, classModel.getTitle());
//        remoteViews.setImageViewResource(R.id.widget_class_icon, classIcon);
    }

}
