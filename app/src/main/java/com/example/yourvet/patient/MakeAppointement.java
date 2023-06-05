package com.example.yourvet.patient;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.yourvet.AlarmReceiver;
import com.example.yourvet.R;
import com.example.yourvet.doctor.Profile;
import com.example.yourvet.model.Appointment;
import com.example.yourvet.model.Doctor;
import com.example.yourvet.model.InterventionType;
import com.example.yourvet.model.Notification;
import com.example.yourvet.model.TimeInterval;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class MakeAppointement extends Fragment {
    private CalendarView calendarView;
    private GridView time_slots;
    private ArrayList<String> data = new ArrayList<>();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://yourvet-fdaf2-default-rtdb.firebaseio.com/");
    private int duration;
    private ArrayList<TimeInterval> busy_timeIntervals = new ArrayList<>();
    private Date choose_date;
    private AutoCompleteTextView autoCompleteTextViewIntervention;
    private ArrayAdapter adapterItemIntervention;
    private ArrayList<String> interventions = new ArrayList<>();
    private String doctor_id, specialization, s_intervention, start_time, end_time;
    private HashMap<String, ArrayList<String>> doctorWorkingHours = new HashMap<>();
    private int day, month, year;
    private Button saveButton,back_button;
    private TimeInterval timeInterval;
    private FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_make_appointement, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        createNotificationChannel();
        calendarView = view.findViewById(R.id.date);
        time_slots = (GridView) view.findViewById(R.id.grid_view);
        back_button= view.findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new Profile()).commit();

            }
        });
        autoCompleteTextViewIntervention = view.findViewById(R.id.interventions);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("myKey", MODE_PRIVATE);
        doctor_id = sharedPreferences.getString("doctorId", "");
        Calendar calendar = Calendar.getInstance();
        long currentDate = calendar.getTimeInMillis();
        calendarView.setMinDate(currentDate);
        saveButton = view.findViewById(R.id.do_appointement);
        databaseReference.child("users").child("doctors").child(doctor_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Doctor d = snapshot.getValue(Doctor.class);
                specialization = d.getSpecialization();
                databaseReference.child("intervention_duration").child(specialization).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot d : snapshot.getChildren()) {
                            InterventionType interventionType = d.getValue(InterventionType.class);
                            String name = interventionType.getName();
                            interventions.add(name);
                            adapterItemIntervention.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        adapterItemIntervention = new ArrayAdapter(getContext(), R.layout.list_breed, interventions);

        autoCompleteTextViewIntervention.setAdapter(adapterItemIntervention);
        autoCompleteTextViewIntervention.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                s_intervention = adapterView.getItemAtPosition(i).toString();
                databaseReference.child("intervention_duration").child(specialization).child(s_intervention).child("time").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        duration = snapshot.getValue(Integer.class);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                }); }});
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                if (s_intervention == null) {
                    Toast.makeText(getContext(), "Va rugam selectati interventia dorita", Toast.LENGTH_SHORT).show();
                } else {
                    day = i2;
                    month = i1;
                    year = i;
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.YEAR,year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH,day);
                    choose_date = calendar.getTime();
                    int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                    String nameOfDay = getNameOfDay(dayOfWeek);
                    databaseReference.child("working_hours").child(doctor_id).child(nameOfDay).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            timeInterval = snapshot.getValue(TimeInterval.class);
                            databaseReference.child("appointments").child(doctor_id).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    busy_timeIntervals.clear();
                                    for (DataSnapshot d : snapshot.getChildren()) {
                                        Appointment a = d.getValue(Appointment.class);
                                        if (a.getDate().equals(formatDate(choose_date))) {
                                            TimeInterval timeInterval1 = a.getWorkDay();
                                            busy_timeIntervals.add(timeInterval1);
                                        }
                                    }
                                    if (busy_timeIntervals.isEmpty()) {
                                        ArrayList<String> intervals = timeInterval.getSubIntervals(duration);
                                        int numRows = (int) Math.ceil(intervals.size() / 5.0f);
                                        int rowHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());
                                        int gridViewHeight = rowHeight * numRows + (numRows - 1) * time_slots.getVerticalSpacing() + time_slots.getPaddingTop() + time_slots.getPaddingBottom();
                                        time_slots.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, gridViewHeight));
                                        GridViewCustomAdapter adapter = new GridViewCustomAdapter(getActivity(), intervals);
                                        time_slots.setAdapter(adapter); }
                                    else {
                                        try {
                                            ArrayList<TimeInterval> availableIntervals = timeInterval.getNonBusyIntervals(duration, busy_timeIntervals);
                                            ArrayList<String> nonBusyIntervals = new ArrayList<>();
                                            for (TimeInterval w : availableIntervals) {
                                                String time_slots = w.getStart_time() + " - " + w.getEnd_time();
                                                nonBusyIntervals.add(time_slots);
                                            }
                                            int numRows = (int) Math.ceil(nonBusyIntervals.size() / 5.0f);
                                            int rowHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());
                                            int gridViewHeight = rowHeight * numRows + (numRows - 1) * time_slots.getVerticalSpacing() + time_slots.getPaddingTop() + time_slots.getPaddingBottom();
                                            time_slots.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, gridViewHeight));
                                            GridViewCustomAdapter adapter = new GridViewCustomAdapter(getActivity(), nonBusyIntervals);
                                            time_slots.setAdapter(adapter);
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }

            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences2 = getContext().getSharedPreferences("interval", MODE_PRIVATE);
                String start_time = sharedPreferences2.getString("start_time", "");
                String end_time = sharedPreferences2.getString("end_time", "");
                TimeInterval w = new TimeInterval(start_time, end_time);
                Appointment appointment = new Appointment(doctor_id, s_intervention, formatDate(choose_date), w,firebaseAuth.getCurrentUser().getUid());
                databaseReference.child("appointments").child(doctor_id).child(appointment.getId()).setValue(appointment);
                databaseReference.child("users").child("doctors").child(doctor_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Doctor d= snapshot.getValue(Doctor.class);
                        String title="Setare programare";
                        String message="Ati efectuat cu succes o programare la doctorul "+ d.getFirstname()+ " "+d.getLastname()+" in data de "+formatDate(choose_date)+" pentru "+s_intervention;
                        Calendar calendar1= Calendar.getInstance();
                        String time= calendar1.getTime().toString();
                        DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
                        Calendar calendar2=Calendar.getInstance();
                        try {
                            Date date = dateFormat.parse(start_time);
                            DateFormat hourFormat = new SimpleDateFormat("HH");
                            DateFormat minuteFormat = new SimpleDateFormat("mm");
                            int hour = Integer.parseInt(hourFormat.format(date));
                            int minutes = Integer.parseInt(minuteFormat.format(date));
                            calendar2.set(Calendar.YEAR,year);
                            calendar2.set(Calendar.MONTH, month);
                            calendar2.set(Calendar.DAY_OF_MONTH,day);
                            calendar2.set(Calendar.HOUR_OF_DAY,hour-1);
                            calendar2.set(Calendar.MINUTE, minutes);
                            calendar2.set(Calendar.SECOND,0);
                            calendar2.set(Calendar.MILLISECOND,0);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        setAlarm(calendar2,d.getFirstname()+ " "+d.getLastname());
                        Notification notification= new Notification(title, message,firebaseAuth.getCurrentUser().getUid(),time);
                        databaseReference.child("notifications").child(firebaseAuth.getCurrentUser().getUid()).child(notification.getId()).setValue(notification);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                Toast.makeText(getContext(), "Programare realizata cu succes!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public String formatDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(date);
    }


    public String getNameOfDay(int dayOfWeek) {
        String name = "Sunday";
        switch (dayOfWeek) {
            case 1:
                name = "Sunday";
                break;
            case 2:
                name = "Monday";
                break;
            case 3:
                name = "Tuesday";
                break;
            case 4:
                name = "Wednesday";
                break;
            case 5:
                name = "Thursday";
                break;
            case 6:
                name = "Friday";
                break;
            case 7:
                name = "Saturday";
                break;
            default:
                break;
        }
        return name;
    }
    private void createNotificationChannel(){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            CharSequence name =" foxandroidReminderChannel";
            String description= "Channel for Alarm Manager";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel =new NotificationChannel("foxandroid",name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager= getContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    private void setAlarm(Calendar calendar, String doctor_name){
        alarmManager= (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getContext(), AlarmReceiver.class);
        intent.putExtra("appointment_doctor", doctor_name);
        pendingIntent= PendingIntent.getBroadcast(getContext(),0,intent,0);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
        Toast.makeText(getContext(), "A fost setata alarma la ora pentru medicul "+ doctor_name , Toast.LENGTH_SHORT).show();
    }
}



