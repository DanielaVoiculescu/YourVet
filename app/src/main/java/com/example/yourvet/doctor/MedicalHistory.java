package com.example.yourvet.doctor;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yourvet.R;
import com.example.yourvet.model.Intervention;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MedicalHistory extends Fragment {
    private RecyclerView recyclerView;
    private List<Intervention> interventionList;
    private InterventionAdapter interventionAdapter;
    private DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://yourvet-fdaf2-default-rtdb.firebaseio.com/");
    private String petId;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_medicalhistory,container,false);
        recyclerView=view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        interventionList=new ArrayList<>();
        SharedPreferences sharedPreferences =getContext().getSharedPreferences("pet", MODE_PRIVATE);
        petId = sharedPreferences.getString("petId","");
        databaseReference.child("interventions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                interventionList.clear();
                for (DataSnapshot s: snapshot.getChildren()){
                    Intervention i=s.getValue(Intervention.class);
                    if(i.getPetId().equals(petId)){
                        interventionList.add(i);

                    }
                    System.out.println(i);
                    interventionAdapter=new InterventionAdapter(getContext(), interventionList);
                    recyclerView.setAdapter(interventionAdapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }
}
