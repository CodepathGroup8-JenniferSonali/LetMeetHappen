package com.example.skarwa.letmeethappen.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.skarwa.letmeethappen.models.Event;
import com.example.skarwa.letmeethappen.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by jennifergodinez on 9/25/17.
 */

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    private List<Event> mTweets;
    private Context context;

    public EventAdapter(List<Event> mTweets) {
        this.mTweets = mTweets;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View tweetView = inflater.inflate(R.layout.item_event, parent, false);

        ViewHolder viewHolder = new ViewHolder(tweetView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Event tweet = mTweets.get(position);


    }

    private String changeHashTagColor(String s) {
        String colorCodeStart = "<font color='#2DB7EF'>";  // use any color as  your want
        String colorCodeEnd = "</font>";

        // change color of tags, this was before I found out API has offset values

        int start = s.indexOf("#") + 1;
        String prefix = s.substring(0, start);
        String suffix = s.substring(start);
        return prefix.replace("#", colorCodeStart+"#") + suffix.replaceFirst(" ", colorCodeEnd+" ");
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
        return mTweets.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivProfileImage;
        public TextView tvUserName;
        public TextView tvBody;
        public TextView tvTimeStamp;
        public TextView tvScreenName;
        public ImageView ivDisplay;


        public ViewHolder (View itemView) {
            super(itemView);

            
        }

    }
}
