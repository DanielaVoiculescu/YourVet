package com.example.yourvet.admin;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.yourvet.R;
import com.example.yourvet.model.Breed;
import com.example.yourvet.model.Request;
import com.example.yourvet.model.Specialization;
import com.example.yourvet.model.Species;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AppAdmin extends Fragment {
    private DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://yourvet-fdaf2-default-rtdb.firebaseio.com/");

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    private Button add_specialization,add_species,add_breed,add_doctor_s,add_intervention;

    private EditText specializationName,breedName,interventionName;
    private AutoCompleteTextView autoCompleteTextViewBreed,autoCompleteTextViewSpecialization;
    private ArrayAdapter adapterItemSpecies,adapterItemSpecialization;

    private ArrayList<String> species=new ArrayList<>();
    private ArrayList<String> specialization=new ArrayList<>();
    private String pet_species,d_specialization;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_app_admin, container, false);
        add_specialization=view.findViewById(R.id.add_specialization);
        add_specialization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewAddSpecialization();
            }
        });
        add_species=view.findViewById(R.id.add_species);
        add_species.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewASpecies();
            }
        });
        add_breed=view.findViewById(R.id.add_breed);
        add_breed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewBreed();
            }
        });
        add_intervention=view.findViewById(R.id.add_intervention);
        add_intervention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewIntervention();
            }
        });
        return view;
    }

    private void createNewIntervention() {
        dialogBuilder=new AlertDialog.Builder(getContext());
        final View addInterventionView=getLayoutInflater().inflate(R.layout.add_intervention,null);
        interventionName=(EditText) addInterventionView.findViewById(R.id.new_intervention);
        EditText interventioDuration= addInterventionView.findViewById(R.id.duration);
        autoCompleteTextViewSpecialization=addInterventionView.findViewById(R.id.doctor_specialization);

        databaseReference.child("specialization").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                specialization.clear();
                for(DataSnapshot dataSnapshot1:snapshot.getChildren()){
                    Specialization species1=dataSnapshot1.getValue(Specialization.class);

                    specialization.add(species1.getName());
                    adapterItemSpecialization.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        adapterItemSpecialization= new ArrayAdapter(getContext(),R.layout.list_breed,specialization);
        autoCompleteTextViewSpecialization.setAdapter(adapterItemSpecialization);
        autoCompleteTextViewSpecialization.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                d_specialization = adapterView.getItemAtPosition(i).toString();
            }
        });
        add_intervention=(Button) addInterventionView.findViewById(R.id.add_intervention);
        add_intervention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child("intervention_duration").child(d_specialization).child(interventionName.getText().toString()).setValue(interventioDuration.getText().toString());
                Toast.makeText(getContext(), "Interventia a fost adaugata cu succes!", Toast.LENGTH_SHORT).show();

            }
        });
        dialogBuilder.setView(addInterventionView);
        dialog=dialogBuilder.create();
        dialog.show();
    }

    public void createNewAddSpecialization(){

        dialogBuilder=new AlertDialog.Builder(getContext());
        final View addSpecializationView=getLayoutInflater().inflate(R.layout.add_specialization,null);
        specializationName=(EditText) addSpecializationView.findViewById(R.id.new_specialization);
        add_doctor_s=(Button) addSpecializationView.findViewById(R.id.add_doctor_s);
        add_doctor_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Specialization s=new Specialization(specializationName.getText().toString());
                databaseReference.child("specialization").child(s.getId()).setValue(s);
                Toast.makeText(getContext(), "Specializare adaugata cu succes!", Toast.LENGTH_SHORT).show();

            }
        });
        dialogBuilder.setView(addSpecializationView);
        dialog=dialogBuilder.create();
        dialog.show();
    }
    public void createNewASpecies(){

        dialogBuilder=new AlertDialog.Builder(getContext());
        final View addSpecializationView=getLayoutInflater().inflate(R.layout.add_species,null);
        specializationName=(EditText) addSpecializationView.findViewById(R.id.new_species);
        add_doctor_s=(Button) addSpecializationView.findViewById(R.id.add_species);
        add_doctor_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Species s=new Species(specializationName.getText().toString());
                DatabaseReference dr=databaseReference.child("species");
                dr.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        boolean exist=false;
                        for(DataSnapshot d1: snapshot.getChildren()){
                            if(d1.child("name").getValue().equals(s.getName())){
                                Toast.makeText(getContext(), "Specia exista deja!", Toast.LENGTH_SHORT).show();
                                exist=true;
                                break;
                            }
                        }
                        if(exist==false){
                            dr.child(s.getId()).setValue(s);
                            Toast.makeText(getContext(), "Specia a fost adaugata cu succes!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });
        dialogBuilder.setView(addSpecializationView);
        dialog=dialogBuilder.create();
        dialog.show();
    }
    public void createNewBreed(){

        dialogBuilder=new AlertDialog.Builder(getContext());
        final View addBreedView=getLayoutInflater().inflate(R.layout.add_bread,null);
        breedName=(EditText) addBreedView.findViewById(R.id.new_breed);
        autoCompleteTextViewBreed=addBreedView.findViewById(R.id.pet_species);

        databaseReference.child("species").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                species.clear();
                for(DataSnapshot dataSnapshot1:snapshot.getChildren()){
                    Species species1=dataSnapshot1.getValue(Species.class);

                    species.add(species1.getName());
                    adapterItemSpecies.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        adapterItemSpecies= new ArrayAdapter(getContext(),R.layout.list_breed,species);
        autoCompleteTextViewBreed.setAdapter(adapterItemSpecies);
        autoCompleteTextViewBreed.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                pet_species = adapterView.getItemAtPosition(i).toString();
            }
        });
        add_doctor_s=(Button) addBreedView.findViewById(R.id.add_breed);
        add_doctor_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Breed b=new Breed(pet_species,breedName.getText().toString());
                databaseReference.child("breeds").child(b.getId()).setValue(b);
                Toast.makeText(getContext(), "Rasa adaugata cu succes!", Toast.LENGTH_SHORT).show();

            }
        });
        dialogBuilder.setView(addBreedView);
        dialog=dialogBuilder.create();
        dialog.show();
    }
}
