package com.example.skarwa.letmeethappen.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.example.skarwa.letmeethappen.R;
import com.squareup.timessquare.CalendarPickerView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 */

public class SelectDatesFragment extends DialogFragment {

    List<Date> dates;

    public interface OnDatePass {
        void onDatePass(List<Date> dates);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(true);

        return inflater.inflate(R.layout.fragment_selectdates, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final CalendarPickerView calendar_view = (CalendarPickerView)view.findViewById(R.id.calendar_view);
//getting current
        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);
        Date today = new Date();

//add one year to calendar from todays date
        calendar_view.init(today, nextYear.getTime())
                .inMode(CalendarPickerView.SelectionMode.MULTIPLE);

        //Displaying all selected dates while clicking on a button
        Button btn_show_dates = (Button) view.findViewById(R.id.btn_show_dates);
        btn_show_dates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String datesStr= "";

                dates = calendar_view.getSelectedDates();

                NewEventFragment parentFrag = (NewEventFragment) getTargetFragment();
                parentFrag.onDatePass(dates);

                dismiss();
            }

        });
    }

}
