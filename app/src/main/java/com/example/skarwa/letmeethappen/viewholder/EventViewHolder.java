package com.example.skarwa.letmeethappen.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.skarwa.letmeethappen.R;
import com.example.skarwa.letmeethappen.models.Event;
import com.example.skarwa.letmeethappen.models.EventStatus;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by skarwa on 10/14/17.
 */

public class EventViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.ivProfileImage)
    public ImageView ivProfileImage;

    @BindView(R.id.tvDate)
    public TextView tvDate;

    @BindView(R.id.tvDate2)
    public TextView tvDate2;

    @BindView(R.id.tvEventName)
    public TextView tvEventName;

    //@BindView(R.id.imgAlert)
    //public ImageButton imgAlert;
    public ProgressBar pbStatus;
    View view;


    public EventViewHolder(final View itemView) {
        super(itemView);
        view = itemView;
        ButterKnife.bind(this,itemView);
    }

    public void bindToEvent(Event event, Context context) {
        tvEventName.setText(event.getEventName());

        String dateOptions = event.getEventDateOptions().keySet().toString();

        if (dateOptions != null && event.getEventFinalDate() == null) {
            String[] dates = dateOptions.replaceAll("[\\[\\]]", "").split(",");
                tvDate.setText(dates[0].trim());
                if (dates.length>1) {
                    tvDate2.setVisibility(View.VISIBLE);
                    tvDate2.setText(dates[1].trim());
                }
        } else {
            tvDate.setText(event.getEventFinalDate().toString()); //TODO change this to confirmed date
        }

        Glide.with(context)
                .load(event.getHostProfileImage())
                .placeholder(R.drawable.ic_host_icon)
                .into(ivProfileImage);


        /*if (event.getEventStatus().equals(EventStatus.PENDING)) {
            imgAlert.setVisibility(View.VISIBLE);

        } else {
            imgAlert.setVisibility(View.GONE);
        }*/
    }
}