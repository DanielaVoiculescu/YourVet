package com.example.yourvet.doctor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.yourvet.R;
import com.example.yourvet.model.Pet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CatsFragment extends Fragment {
    private ListView catsList;
    private ArrayList<Pet> cats = new ArrayList<>();
    private PetForDoctorAdapter catAdapter;

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://yourvet-fdaf2-default-rtdb.firebaseio.com/");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cats, container, false);
        catsList = view.findViewById(R.id.cats_list);
        databaseReference.child("pets").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cats.clear();
                for(DataSnapshot d:snapshot.getChildren()){
                    Pet p=d.getValue(Pet.class);
                    if (p.getSpecies().equals("Pisica")){
                        cats.add(p);
                        catAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        catAdapter=new PetForDoctorAdapter(cats,getContext());
        catsList.setAdapter(catAdapter);
        return view;
    }
}
