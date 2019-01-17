package com.example.esraa.fitpass.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
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
import com.example.esraa.fitpass.util.GetLocationManager;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GymsAdapter extends RecyclerView.Adapter<GymViewHolder> {
    private List<GymModel> gymModelList;
    private Context context;
    private Activity activity;

    public GymsAdapter(List<GymModel> gymModelList, Activity activity) {
        this.gymModelList = gymModelList;
        this.activity = activity;
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
    public void onBindViewHolder(@NonNull final GymViewHolder gymViewHolder, int i) {
        final GymModel model = gymModelList.get(i);
        gymViewHolder.gymTitleTextView.setText(model.getName());
        gymViewHolder.gymLocationTextView.setText(GetLocationManager.getCityFromLatAndLon(
                context, Double.parseDouble(model.getLat())
                , Double.parseDouble(model.getLon())));
        gymViewHolder.gymCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, GymDetailsActivity.class);
                intent.putExtra(Constants.GYM_MODEL, model);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(activity,
                            gymViewHolder.gymImageView, gymViewHolder.gymImageView.getTransitionName()).toBundle();
                    context.startActivity(intent, bundle);
                } else {
                    context.startActivity(intent);
                }
            }
        });
        Picasso.get().load(model.getImagesUrls().get(0)).into(gymViewHolder.gymImageView);
    }

    @Override
    public int getItemCount() {
        return gymModelList.size();
    }

    public void setGymModelList(List<GymModel> gymModelList) {
        this.gymModelList = gymModelList;
    }
}
