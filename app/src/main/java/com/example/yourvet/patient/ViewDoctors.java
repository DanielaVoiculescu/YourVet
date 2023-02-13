package com.example.yourvet.patient;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.yourvet.R;
import com.example.yourvet.model.Doctor;
import com.example.yourvet.model.Pet;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewDoctors extends Fragment {
    private ListView doctorList;
    private ArrayList<Doctor> doctors=new ArrayList<>();
    private DoctorAdapter doctorAdapter;
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://yourvet-fdaf2-default-rtdb.firebaseio.com/");
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_view_doctors,container,false);
        doctorList=(ListView) view.findViewById(R.id.doctor_list);

        databaseReference.child("users").child("doctors").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot1:snapshot.getChildren()){
                    Doctor pet=dataSnapshot1.getValue(Doctor.class);

                    doctors.add(pet);
                    doctorAdapter.notifyDataSetChanged();
                    System.out.println(pet);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        doctorAdapter= new DoctorAdapter(doctors,getContext());

        doctorList.setAdapter(doctorAdapter);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
