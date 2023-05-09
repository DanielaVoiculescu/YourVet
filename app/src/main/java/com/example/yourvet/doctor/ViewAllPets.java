package com.example.yourvet.doctor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.yourvet.R;
import com.example.yourvet.model.Pet;
import com.example.yourvet.patient.AddPet;
import com.example.yourvet.patient.PetAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewAllPets extends Fragment {
    private ListView petsList;
    private ArrayList<Pet> pets=new ArrayList<>();
    private PetForDoctorAdapter petAdapter;
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://yourvet-fdaf2-default-rtdb.firebaseio.com/");
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_view_all_pets,container,false);
        /*petsList=(ListView) view.findViewById(R.id.view_all_pets);

        databaseReference.child("pets").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot1:snapshot.getChildren()){
                    Pet pet=dataSnapshot1.getValue(Pet.class);
                    System.out.println(pet.getName());
                    pets.add(pet);
                    petAdapter.notifyDataSetChanged();
                    System.out.println(pets);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        petAdapter= new PetForDoctorAdapter(pets,getContext());

        petsList.setAdapter(petAdapter);*/
        ViewPager2 viewPager = view.findViewById(R.id.view_pager);
        TabLayout tabLayout = view.findViewById(R.id.tab_layout);

        PetsPagerAdapter pagerAdapter = new PetsPagerAdapter(requireActivity());
        viewPager.setAdapter(pagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText(R.string.dogs);
                            break;
                        case 1:
                            tab.setText(R.string.cats);
                            break;
                        case 2:
                            tab.setText(R.string.others);
                            break;
                    }
                }).attach();

        return view;
    }
}
