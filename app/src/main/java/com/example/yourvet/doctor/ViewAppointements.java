package com.example.yourvet.doctor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yourvet.R;
import com.example.yourvet.model.Appointment;
import com.example.yourvet.model.Intervention;
import com.example.yourvet.patient.GridViewCustomAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ViewAppointements extends Fragment {
    private CalendarView calendarView;
    private GridView time_slots;
    private ArrayList<String> intervals = new ArrayList<>();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://yourvet-fdaf2-default-rtdb.firebaseio.com/");
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_appointements, container, false);
        calendarView = view.findViewById(R.id.date);
        time_slots = (GridView) view.findViewById(R.id.grid_view);
        Calendar calendar = Calendar.getInstance();
        long currentDate = calendar.getTimeInMillis();
        calendarView.setMinDate(currentDate);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                int day,month,year;
                day = i2;
                month = i1;
                year = i;

                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day);
                Date choose_date = calendar.getTime();
                databaseReference.child("appointments").child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        intervals.clear();
                        for(DataSnapshot d:snapshot.getChildren()){
                            Appointment a=d.getValue(Appointment.class);
                            if (a.getDate().equals(formatDate(choose_date))){
                                intervals.add(a.getWorkDay().getStart_time()+"-"+a.getWorkDay().getEnd_time());
                               System.out.println(choose_date);
                            }
                            System.out.println(a);
                        }
                        GridViewAppointmentAdapter adapter = new GridViewAppointmentAdapter(getActivity(), intervals);
                        time_slots.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        return view;
    }
    public String formatDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(date);
    }

}
