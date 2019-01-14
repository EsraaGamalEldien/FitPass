package com.example.esraa.fitpass.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.esraa.fitpass.R;
import com.example.esraa.fitpass.activity.ClassDetailsActivity;
import com.example.esraa.fitpass.model.ClassModel;
import com.example.esraa.fitpass.util.Constants;

import java.util.List;

public class ClassesAdapter extends RecyclerView.Adapter<ClassViewHolder> {
    private List<ClassModel> classModelList;
    private Context context;

    public ClassesAdapter(List<ClassModel> classModelList) {
        this.classModelList = classModelList;

    }

    @NonNull
    @Override
    public ClassViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.layout_class_row, viewGroup, false);
        context = viewGroup.getContext();
        return new ClassViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassViewHolder classViewHolder, int i) {
        final ClassModel model = classModelList.get(i);
        classViewHolder.startTimeTextView.setText(model.getStartDate());
        classViewHolder.durationTextView.setText(model.getDuration());
        classViewHolder.titleTextView.setText(model.getTitle());
        classViewHolder.categoryTextView.setText(model.getType());
        classViewHolder.locationTextView.setText(model.getLat() + model.getLon());
        classViewHolder.classCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToClassDetails(model);
            }
        });
    }

    private void navigateToClassDetails(ClassModel model) {
        Intent intent = new Intent(context, ClassDetailsActivity.class);
        intent.putExtra(Constants.CLASS_MODEL, model);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return classModelList.size();
    }

    public void setAdapter(List<ClassModel> classModelList) {
        this.classModelList = classModelList;
    }
}
