package com.example.yourvet.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.yourvet.R;
import com.example.yourvet.authentification.Login;
import com.example.yourvet.model.Breed;
import com.example.yourvet.model.InterventionType;
import com.example.yourvet.model.Specialization;
import com.example.yourvet.model.Species;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainPageAdmin extends AppCompatActivity {
    private CardView add_specialization, add_species,breed,intervention, requests;
    private EditText specializationName,breedName,interventionName;
    private AutoCompleteTextView autoCompleteTextViewBreed,autoCompleteTextViewSpecialization;
    private ArrayAdapter adapterItemSpecies,adapterItemSpecialization;
    private DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://yourvet-fdaf2-default-rtdb.firebaseio.com/");
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private Button logout_button;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private ArrayList<String> species=new ArrayList<>();
    private ArrayList<String> specialization=new ArrayList<>();
    private String pet_species,d_specialization;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page_admin);
        add_specialization=findViewById(R.id.add_specialization);
        add_species=findViewById(R.id.add_species);
        breed=findViewById(R.id.add_breed);
        intervention=findViewById(R.id.add_intervention);
        requests=findViewById(R.id.requests);

        logout_button=findViewById(R.id.logOut);
        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                androidx.appcompat.app.AlertDialog.Builder builder= new androidx.appcompat.app.AlertDialog.Builder(MainPageAdmin.this);
                builder.setTitle("Logout");
                builder.setMessage("Sunteti sigur ca vreti sa va deconectati?");
                builder.setPositiveButton("Da", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mAuth.signOut();
                        startActivity(new Intent(MainPageAdmin.this, Login.class));

                        finish();
                    }
                });
                builder.setNegativeButton("Nu", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        });
        requests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainPageAdmin.this, ViewRequests.class));
                finish();
            }
        });

        add_specialization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewAddSpecialization();
            }
        });
        add_species.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewASpecies();
            }
        });
        breed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewBreed();
            }
        });
        intervention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewIntervention();
            }
        });
    }
    private void createNewIntervention() {
        dialogBuilder=new AlertDialog.Builder(this);
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
        adapterItemSpecialization= new ArrayAdapter(this,R.layout.list_breed,specialization);
        autoCompleteTextViewSpecialization.setAdapter(adapterItemSpecialization);
        autoCompleteTextViewSpecialization.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                d_specialization = adapterView.getItemAtPosition(i).toString();
            }
        });
       Button add_intervention=(Button) addInterventionView.findViewById(R.id.add_intervention);
        add_intervention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InterventionType interventionType = new InterventionType(interventionName.getText().toString(),Integer.parseInt(interventioDuration.getText().toString()));
                databaseReference.child("intervention_duration").child(d_specialization).child(interventionType.getName()).setValue(interventionType);
                Toast.makeText(MainPageAdmin.this, "Interventia a fost adaugata cu succes!", Toast.LENGTH_SHORT).show();

            }
        });
        dialogBuilder.setView(addInterventionView);
        dialog=dialogBuilder.create();
        dialog.show();
    }

    public void createNewAddSpecialization(){

        dialogBuilder=new AlertDialog.Builder(MainPageAdmin.this);
        final View addSpecializationView=getLayoutInflater().inflate(R.layout.add_specialization,null);
        specializationName=(EditText) addSpecializationView.findViewById(R.id.new_specialization);
        Button add_doctor_s=(Button) addSpecializationView.findViewById(R.id.add_doctor_s);
        add_doctor_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Specialization s=new Specialization(specializationName.getText().toString());
                databaseReference.child("specialization").child(s.getId()).setValue(s);
                Toast.makeText(MainPageAdmin.this, "Specializare adaugata cu succes!", Toast.LENGTH_SHORT).show();

            }
        });
        dialogBuilder.setView(addSpecializationView);
        dialog=dialogBuilder.create();
        dialog.show();
    }
    public void createNewASpecies(){

        dialogBuilder=new AlertDialog.Builder(this);
        final View addSpecializationView=getLayoutInflater().inflate(R.layout.add_species,null);
        specializationName=(EditText) addSpecializationView.findViewById(R.id.new_species);
        Button add_doctor_s=(Button) addSpecializationView.findViewById(R.id.add_species);
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
                                Toast.makeText(MainPageAdmin.this, "Specia exista deja!", Toast.LENGTH_SHORT).show();
                                exist=true;
                                break;
                            }
                        }
                        if(exist==false){
                            dr.child(s.getId()).setValue(s);
                            Toast.makeText(MainPageAdmin.this, "Specia a fost adaugata cu succes!", Toast.LENGTH_SHORT).show();
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

        dialogBuilder=new AlertDialog.Builder(this);
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
        adapterItemSpecies= new ArrayAdapter(MainPageAdmin.this,R.layout.list_breed,species);
        autoCompleteTextViewBreed.setAdapter(adapterItemSpecies);
        autoCompleteTextViewBreed.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                pet_species = adapterView.getItemAtPosition(i).toString();
            }
        });
        Button add_doctor_s=(Button) addBreedView.findViewById(R.id.add_breed);
        add_doctor_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Breed b=new Breed(pet_species,breedName.getText().toString());
                databaseReference.child("breeds").child(b.getId()).setValue(b);
                Toast.makeText(MainPageAdmin.this, "Rasa adaugata cu succes!", Toast.LENGTH_SHORT).show();

            }
        });
        dialogBuilder.setView(addBreedView);
        dialog=dialogBuilder.create();
        dialog.show();
    }
}