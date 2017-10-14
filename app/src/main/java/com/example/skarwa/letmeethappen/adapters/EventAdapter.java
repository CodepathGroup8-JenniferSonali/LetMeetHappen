package com.example.skarwa.letmeethappen.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.skarwa.letmeethappen.R;
import com.example.skarwa.letmeethappen.models.Event;
import com.example.skarwa.letmeethappen.models.EventStatus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by jennifergodinez on 9/25/17.
 */

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    private List<Event> mEvents;
    private Context context;
    OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    public EventAdapter(List<Event> events, OnItemClickListener listener) {
        this.mEvents = events;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View tweetView = inflater.inflate(R.layout.item_event, parent, false);

        ViewHolder viewHolder = new ViewHolder(tweetView, listener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Event event = mEvents.get(position);
        
        //holder.tvUserName.setText(tweet.user.profileImg); // profile image


        holder.tvEventName.setText(event.getEventName());
        holder.tvDate.setText(event.getEventCreatedDate().toString());

        Glide.with(context)
                .load(event.getHostProfileImage())
                .placeholder(R.drawable.ic_host_placeholder)
                .into(holder.ivProfileImage);


        if (event.getEventStatus().equals(EventStatus.NEW)) {
            holder.imgAlert.setVisibility(View.VISIBLE);

        } else {
            holder.imgAlert.setVisibility(View.GONE);
        }
    }

    

    private String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // shorten relative time
        String str = relativeDate;
        if ("Yesterday".equals(relativeDate)) {
            str = "1d";
        } else if (relativeDate.substring(0, 1).matches("[0-9]")) {
            int cutIndex = relativeDate.indexOf(' ') + 1;
            str = relativeDate.replace(" ", "").substring(0, cutIndex);
        } else if (!relativeDate.startsWith("in ")){
            int pos = relativeDate.indexOf(",");
            str = relativeDate.substring(0, pos);
        }

        return str;
    }


    @Override
    public int getItemCount() {
        return mEvents.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivProfileImage;
        public TextView tvDate;
        public TextView tvEventName;
        public ImageButton imgAlert;
        public ProgressBar pbStatus;


        public ViewHolder (final View itemView, final OnItemClickListener listener) {
            super(itemView);
            ivProfileImage = (ImageView)itemView.findViewById(R.id.ivProfileImage);
            tvDate = (TextView)itemView.findViewById(R.id.tvDate);
            tvEventName = (TextView)itemView.findViewById(R.id.tvEventName);
            imgAlert = (ImageButton)itemView.findViewById(R.id.imgAlert);
           // pbStatus = itemView.findViewById(R.id.pbStatus);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(itemView, position);
                        //Log.d("DEBUG", "itemClicked");

                    }
                }
            });

            
        }

    }
}
