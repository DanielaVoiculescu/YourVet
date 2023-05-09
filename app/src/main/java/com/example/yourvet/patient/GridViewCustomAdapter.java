package com.example.yourvet.patient;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

import com.example.yourvet.R;
import com.example.yourvet.model.Appointment;
import com.example.yourvet.model.WorkDay;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class GridViewCustomAdapter extends BaseAdapter {

    private ArrayList<String> items;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://yourvet-fdaf2-default-rtdb.firebaseio.com/");
    private int mSelectedPosition = -1;
    private static Activity mActivity;

    private static LayoutInflater inflater = null;

    public GridViewCustomAdapter(Activity activity, ArrayList<String> tempTitle) {
        mActivity = activity;
        items = tempTitle;

        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public final int getCount() {

        return items.size();

    }

    @Override
    public final Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public final long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = null;

        v = inflater.inflate(R.layout.time_slot_item, null);

        Button tv = (Button) v.findViewById(R.id.button);
        try {
            tv.setText(convertTo24HourFormat(parseTimeRange(items.get(position))[0])+"-"+convertTo24HourFormat(parseTimeRange(items.get(position))[1]));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String start_time=parseTimeRange(items.get(position))[0];
                    String end_time=parseTimeRange(items.get(position))[1];
                    SharedPreferences sharedPref = mActivity.getSharedPreferences("interval", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("start_time", start_time);
                    editor.putString("end_time",end_time);
                    editor.apply();
                    int previousPosition = mSelectedPosition;
                    mSelectedPosition = position;
                    if (previousPosition != -1) {
                        View previousView = getViewByPosition(previousPosition, parent);
                        previousView.setBackgroundColor(Color.GREEN);
                    }
                    view.setBackgroundColor(Color.WHITE);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });
        return v;
    }
    private View getViewByPosition(int position, ViewGroup parent) {
        GridView gridView = (GridView) parent;
        int firstVisiblePosition = gridView.getFirstVisiblePosition();
        int lastVisiblePosition = gridView.getLastVisiblePosition();
        if (position < firstVisiblePosition || position > lastVisiblePosition) {
            return gridView.getAdapter().getView(position, null, gridView);
        } else {
            int childIndex = position - firstVisiblePosition;
            return gridView.getChildAt(childIndex);
        }
    }
    public String[] parseTimeRange(String timeString) throws ParseException {
        SimpleDateFormat inputFormatter = new SimpleDateFormat("hh:mm a");
        SimpleDateFormat outputFormatter = new SimpleDateFormat("hh:mm a");
        String[] timeParts = timeString.split(" - ");
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
