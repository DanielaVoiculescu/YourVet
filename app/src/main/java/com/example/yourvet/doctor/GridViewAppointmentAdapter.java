package com.example.yourvet.doctor;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.example.yourvet.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class GridViewAppointmentAdapter extends BaseAdapter {
    private ArrayList<String> items;

    private static Activity mActivity;
    private static LayoutInflater inflater = null;
    public GridViewAppointmentAdapter(Activity activity, ArrayList<String> tempTitle) {
        mActivity = activity;
        items = tempTitle;

        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = null;

        v = inflater.inflate(R.layout.time_slot_item, null);

        Button tv = (Button) v.findViewById(R.id.button);
        try {
            tv.setText("    "+convertTo24HourFormat(parseTimeRange(items.get(i))[0])+"-"+convertTo24HourFormat(parseTimeRange(items.get(i))[1]));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return v;
    }
    public String[] parseTimeRange(String timeString) throws ParseException {
        SimpleDateFormat inputFormatter = new SimpleDateFormat("hh:mm a");
        SimpleDateFormat outputFormatter = new SimpleDateFormat("hh:mm a");
        String[] timeParts = timeString.split("-");
        Date startTime = inputFormatter.parse(timeParts[0]);
        Date endTime = inputFormatter.parse(timeParts[1]);
        String startString = outputFormatter.format(startTime);
        String endString = outputFormatter.format(endTime);
        return new String[] { startString, endString };
    }
    public String convertTo24HourFormat(String time) {
        DateFormat inputFormat = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
        DateFormat outputFormat = new SimpleDateFormat("HH:mm");

        try {
            Date date = inputFormat.parse(time);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
