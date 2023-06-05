package com.example.yourvet.doctor;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.yourvet.R;
import com.example.yourvet.model.Appointment;

import com.example.yourvet.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AppointmentAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Appointment> appointments=new ArrayList<>();
    private TextView appointment_owner,appointment_type,appointment_time;
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://yourvet-fdaf2-default-rtdb.firebaseio.com/");

    public AppointmentAdapter(Context context, ArrayList<Appointment> appointments) {
        this.context = context;
        this.appointments = appointments;
    }

    @Override
    public int getCount() {
        return appointments.size();
    }

    @Override
    public Object getItem(int i) {
        return appointments.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater=(LayoutInflater)  context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view=inflater.inflate(R.layout.appointment_info,viewGroup,false);
        appointment_owner=view.findViewById(R.id.appointment_owner);
        appointment_type=view.findViewById(R.id.appointment_type);
        appointment_time=view.findViewById(R.id.appointment_time);
        String id=appointments.get(i).getOwner_id();
        appointment_type.setText(appointments.get(i).getIntervention());
        appointment_time.setText(appointments.get(i).getWorkDay().getStart_time()+"-"+appointments.get(i).getWorkDay().getEnd_time());
        databaseReference.child("users").child("patients").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User p=snapshot.getValue(User.class);
                appointment_owner.setText(p.getFirstname()+" "+p.getLastname());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
}
