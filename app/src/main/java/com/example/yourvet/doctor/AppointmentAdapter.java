package com.example.yourvet.doctor;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yourvet.R;
import com.example.yourvet.model.Appointment;

import com.example.yourvet.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Appointment> appointments = new ArrayList<>();
    private HashMap<String, String> ownersMap = new HashMap<>();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://yourvet-fdaf2-default-rtdb.firebaseio.com/");

    public AppointmentAdapter(Context context, ArrayList<Appointment> appointments) {
        this.context = context;
        this.appointments = appointments;
        fetchOwnersData();
    }

    private void fetchOwnersData() {
        DatabaseReference ownersRef = databaseReference.child("users").child("patients");
        ownersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User owner = dataSnapshot.getValue(User.class);
                    if (owner != null) {
                        ownersMap.put(dataSnapshot.getKey(), owner.getFirstname() + " " + owner.getLastname());
                    }
                }
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointment_info, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Appointment appointment = appointments.get(position);
        holder.appointment_type.setText(appointment.getIntervention());
        holder.appointment_time.setText(appointment.getWorkDay().getStart_time() + "-" + appointment.getWorkDay().getEnd_time());

        String ownerId = appointment.getOwner_id();
        String ownerName = ownersMap.get(ownerId);
        if (ownerName != null) {
            holder.appointment_owner.setText(ownerName);
        }
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView appointment_owner;
        TextView appointment_type;
        TextView appointment_time;

        ViewHolder(View itemView) {
            super(itemView);
            appointment_owner = itemView.findViewById(R.id.appointment_owner);
            appointment_type = itemView.findViewById(R.id.appointment_type);
            appointment_time = itemView.findViewById(R.id.appointment_time);
        }
    }
}