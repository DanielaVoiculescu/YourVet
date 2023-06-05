package com.example.yourvet.doctor;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.yourvet.R;
import com.example.yourvet.model.TimeInterval;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class WorkingHours extends Fragment {
    private EditText startTimeEditTextMonday, endTimeEditTextMonday,startTimeEditTextTuesday, endTimeEditTextTuesday,startTimeEditTextWednesday, endTimeEditTextWednesday,startTimeEditTextThursday, endTimeEditTextThursday,startTimeEditTextFriday, endTimeEditTextFriday;

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://yourvet-fdaf2-default-rtdb.firebaseio.com/");
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    private Calendar startCalendar, endCalendar;
    private Button back_button;
    private SimpleDateFormat timeFormat;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_working_hours, container, false);
        startTimeEditTextMonday=view.findViewById(R.id.start_time_monday);
        endTimeEditTextMonday=view.findViewById(R.id.end_time_edit_monday);
        startTimeEditTextTuesday=view.findViewById(R.id.start_time_tuesday);
        endTimeEditTextTuesday=view.findViewById(R.id.end_time_edit_tuesday);
        startTimeEditTextWednesday=view.findViewById(R.id.start_time_wednesday);
        endTimeEditTextWednesday=view.findViewById(R.id.end_time_edit_wednesday);
        startTimeEditTextThursday=view.findViewById(R.id.start_time_thursday);
        endTimeEditTextThursday=view.findViewById(R.id.end_time_edit_thursday);
        startTimeEditTextFriday=view.findViewById(R.id.start_time_friday);
        endTimeEditTextFriday=view.findViewById(R.id.end_time_edit_friday);
        timeFormat = new SimpleDateFormat("hh:mm a", Locale.US);
        back_button = view.findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new Profile()).commit();

            }
        });
        startCalendar = Calendar.getInstance();
        endCalendar = Calendar.getInstance();

        showIntervals("Monday");
        showIntervals("Tuesday");
        showIntervals("Wednesday");
        showIntervals("Thursday");
        showIntervals("Friday");
        startTimeEditTextMonday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(startTimeEditTextMonday, startCalendar);
            }
        });

        endTimeEditTextMonday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(endTimeEditTextMonday, endCalendar);
            }
        });
        startTimeEditTextTuesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(startTimeEditTextTuesday, startCalendar);
            }
        });

        endTimeEditTextTuesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(endTimeEditTextTuesday, endCalendar);
            }
        });
        startTimeEditTextWednesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(startTimeEditTextWednesday, startCalendar);
            }
        });

        endTimeEditTextWednesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(endTimeEditTextWednesday, endCalendar);
            }
        });
        startTimeEditTextThursday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(startTimeEditTextThursday, startCalendar);
            }
        });

        endTimeEditTextThursday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(endTimeEditTextThursday, endCalendar);
            }
        });
        startTimeEditTextFriday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(startTimeEditTextFriday, startCalendar);
            }
        });

        endTimeEditTextFriday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(endTimeEditTextFriday, endCalendar);
            }
        });
        MaterialButton saveButton = view.findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference workingHoursRef = database.getReference("working_hours").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                // Get the start and end times
                String startTimeMonday = startTimeEditTextMonday.getText().toString();
                String endTimeMonday = endTimeEditTextMonday.getText().toString();

                // Save the working hours to the database

                workingHoursRef.child("Monday").child("start_time").setValue(startTimeMonday);
                workingHoursRef.child("Monday").child("end_time").setValue(endTimeMonday);


                String startTimeTuesday = startTimeEditTextTuesday.getText().toString();
                String endTimeTuesday = endTimeEditTextTuesday.getText().toString();

                // Save the working hours to the database

                workingHoursRef.child("Tuesday").child("start_time").setValue(startTimeTuesday);
                workingHoursRef.child("Tuesday").child("end_time").setValue(endTimeTuesday);

                String startTimeWednesday = startTimeEditTextWednesday.getText().toString();
                String endTimeWednesday = endTimeEditTextWednesday.getText().toString();

                // Save the working hours to the database

                workingHoursRef.child("Wednesday").child("start_time").setValue(startTimeWednesday);
                workingHoursRef.child("Wednesday").child("end_time").setValue(endTimeWednesday);

                String startTimeThursday = startTimeEditTextThursday.getText().toString();
                String endTimeThursday = endTimeEditTextThursday.getText().toString();

                // Save the working hours to the database

                workingHoursRef.child("Thursday").child("start_time").setValue(startTimeThursday);
                workingHoursRef.child("Thursday").child("end_time").setValue(endTimeThursday);

                String startTimeFriday = startTimeEditTextFriday.getText().toString();
                String endTimeFriday = endTimeEditTextFriday.getText().toString();

                // Save the working hours to the database

                workingHoursRef.child("Friday").child("start_time").setValue(startTimeFriday);
                workingHoursRef.child("Friday").child("end_time").setValue(endTimeFriday);
                // Show a toast to indicate that the working hours were saved
                Toast.makeText(getContext(), "Program modificat cu succes!", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    private void showTimePickerDialog(final TextView timeTextView, final Calendar calendar) {
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);

                        timeTextView.setText(timeFormat.format(calendar.getTime()));
                    }
                }, hour, minute, false);
        timePickerDialog.show();
    }
    private void showIntervals(String day){
        databaseReference.child("working_hours").child(auth.getCurrentUser().getUid()).child(day).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                TimeInterval timeInterval = snapshot.getValue(TimeInterval.class);
                if (timeInterval !=null){
                    switch (day){
                        case "Monday":
                            startTimeEditTextMonday.setText(timeInterval.getStart_time());
                            endTimeEditTextMonday.setText(timeInterval.getEnd_time());
                            break;
                        case "Tuesday":
                            startTimeEditTextTuesday.setText(timeInterval.getStart_time());
                            endTimeEditTextTuesday.setText(timeInterval.getEnd_time());
                            break;
                        case "Wednesday":
                            startTimeEditTextWednesday.setText(timeInterval.getStart_time());
                            endTimeEditTextWednesday.setText(timeInterval.getEnd_time());
                            break;
                        case "Thursday":
                            startTimeEditTextThursday.setText(timeInterval.getStart_time());
                            endTimeEditTextThursday.setText(timeInterval.getEnd_time());
                            break;
                        case "Friday":
                            startTimeEditTextFriday.setText(timeInterval.getStart_time());
                            endTimeEditTextFriday.setText(timeInterval.getEnd_time());
                            break;
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
