package com.example.skarwa.letmeethappen.activities;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.skarwa.letmeethappen.R;
import com.example.skarwa.letmeethappen.fragments.RespondFragment;
import com.example.skarwa.letmeethappen.models.Event;
import com.example.skarwa.letmeethappen.models.User;
import com.example.skarwa.letmeethappen.utils.Constants;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.skarwa.letmeethappen.utils.Constants.EVENT_OBJ;

/**
 * Created by skarwa on 10/16/17.
 */

public class ViewEventDetailActivity extends AppCompatActivity implements Constants {
    private static final String TAG = "ViewEventDetailActivity";
    Event mEvent;
    User user;

    @BindView(R.id.btnRespond)
    Button btnRespond;


    @BindView(R.id.toolbar)
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        mEvent = Parcels.unwrap(getIntent().getParcelableExtra(EVENT_OBJ));
        user = Parcels.unwrap(getIntent().getParcelableExtra(USER_OBJ));

        ButterKnife.bind(this);

        generateDetailEventView();
        getSupportActionBar().setTitle(mEvent.getEventName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void generateDetailEventView() {

        if(mEvent.getPlanner().getId() == user.getId()){
            btnRespond.setVisibility(View.INVISIBLE);
        } else {
            btnRespond.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    RespondFragment respondFragment = RespondFragment.newInstance(Parcels.wrap(mEvent));
                    respondFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
                    respondFragment.show(getSupportFragmentManager(), "fragment_respond");

                    Log.d("DEBUG", "display Respond dialog here");
                }
            });
        }
    }
}
