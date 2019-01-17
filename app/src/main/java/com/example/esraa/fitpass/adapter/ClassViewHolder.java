package com.example.esraa.fitpass.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.esraa.fitpass.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ClassViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.class_startTime_textView)
    TextView startTimeTextView;
    @BindView(R.id.class_duration_textView)
    TextView durationTextView;
    @BindView(R.id.class_title_textView)
    TextView titleTextView;
    @BindView(R.id.class_category_textView)
    TextView categoryTextView;
    @BindView(R.id.class_location_textView)
    TextView locationTextView;
    @BindView(R.id.class_card_view)
    LinearLayout classCardView;
    @BindView(R.id.class_icon_imageView)
    ImageView classIconImageView;


    public ClassViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
