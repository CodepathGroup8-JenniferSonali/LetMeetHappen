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

/**
 * Created by skarwa on 10/14/17.
 */

public class EventViewHolder extends RecyclerView.ViewHolder {

    public ImageView ivProfileImage;
    public TextView tvDate;
    public TextView tvEventName;
    public ImageButton imgAlert;
    public ProgressBar pbStatus;
    View view;


    public EventViewHolder(final View itemView) {
        super(itemView);
        view = itemView;
        ivProfileImage = (ImageView)itemView.findViewById(R.id.ivProfileImage);
        tvDate = (TextView)itemView.findViewById(R.id.tvDate);
        tvEventName = (TextView)itemView.findViewById(R.id.tvEventName);
        imgAlert = (ImageButton)itemView.findViewById(R.id.imgAlert);
        // pbStatus = itemView.findViewById(R.id.pbStatus);
    }

    public void bindToEvent(Event event, Context context) {
        tvEventName.setText(event.getEventName());
        tvDate.setText(event.getAcceptByDate().toString());

        Glide.with(context)
                .load(event.getHostProfileImage())
                .placeholder(R.drawable.ic_host_placeholder)
                .into(ivProfileImage);


        /*if (event.getEventStatus().equals(EventStatus.NEW)) {
            imgAlert.setVisibility(View.VISIBLE);

        } else {
            imgAlert.setVisibility(View.GONE);
        }*/
    }
}