package com.example.esraa.fitpass.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.esraa.fitpass.R;
import com.example.esraa.fitpass.activity.GymDetailsActivity;
import com.example.esraa.fitpass.model.GymModel;
import com.example.esraa.fitpass.util.Constants;

import java.util.List;

public class GymsAdapter extends RecyclerView.Adapter<GymViewHolder> {
    private List<GymModel> gymModelList;
    private Context context;

    public GymsAdapter(List<GymModel> gymModelList) {
        this.gymModelList = gymModelList;
    }

    @NonNull
    @Override
    public GymViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_gym_row,
                viewGroup, false);
        context = viewGroup.getContext();
        return new GymViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GymViewHolder gymViewHolder, int i) {
        final GymModel model = gymModelList.get(i);
        gymViewHolder.gymTitleTextView.setText(model.getName());
        gymViewHolder.gymLocationTextView.setText(model.getLat() + model.getLon());
        gymViewHolder.gymCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, GymDetailsActivity.class);
                intent.putExtra(Constants.GYM_MODEL, model);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return gymModelList.size();
    }

    public void setGymModelList(List<GymModel> gymModelList) {
        this.gymModelList = gymModelList;
    }
}
