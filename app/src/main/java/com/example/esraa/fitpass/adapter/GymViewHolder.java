package com.example.esraa.fitpass.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.esraa.fitpass.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GymViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.gym_title_textView)
    TextView gymTitleTextView;
    @BindView(R.id.gym_location_textView)
    TextView gymLocationTextView;
    @BindView(R.id.gym_card_view)
    CardView gymCardView;

    public GymViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
