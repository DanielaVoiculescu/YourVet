package com.example.yourvet.patient;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.yourvet.R;
import com.example.yourvet.doctor.AddIntervention;
import com.example.yourvet.doctor.MedicalHistory;
import com.example.yourvet.doctor.ViewAllPets;
import com.example.yourvet.model.Pet;
import com.example.yourvet.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class PetProfileFragment extends Fragment {
    private ImageView imageView;
    private TextView name,species,breed,birthdate, owner_name,sex;
    private String petId;
    private Button medical_history, add_intervention,back_button;
    private DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://yourvet-fdaf2-default-rtdb.firebaseio.com/");
    private FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pet_profile, container, false);
        SharedPreferences sharedPreferences =getContext().getSharedPreferences("pet", MODE_PRIVATE);
        petId = sharedPreferences.getString("petId","");
        SharedPreferences sharedPreferences1 =getContext().getSharedPreferences("de_unde_vine", MODE_PRIVATE);
        String clasa= sharedPreferences1.getString("nume","");

        imageView= view.findViewById(R.id.profile_image);
        name=view.findViewById(R.id.profile_name_pet);
        species=view.findViewById(R.id.profile_species);
        breed=view.findViewById(R.id.profile_breed);
        birthdate=view.findViewById(R.id.birthdate);
        owner_name=view.findViewById(R.id.name_owner);
        sex=view.findViewById(R.id.sex_pet);
        medical_history=view.findViewById(R.id.medical_history);
        add_intervention=view.findViewById(R.id.add_intervention);
        back_button=view.findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(clasa.contains("PetForDoctorAdapter")){
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ViewAllPets()).commit();

                }
                else
                {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ViewPets()).commit();

                }
            }
        });
        //edit_button=view.findViewById(R.id.edit_button);
        databaseReference.child("roles").child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String role =snapshot.getValue(String.class);
                if(role.equals("patient")){
                    add_intervention.setVisibility(View.INVISIBLE);
                }
                if (role.equals("doctor")){
                  //  edit_button.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference.child("pets").child(petId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Pet pet= snapshot.getValue(Pet.class);
                String img=  pet.getPhotoUrl();
                Picasso.get().load(img).into(imageView);
                name.setText(pet.getName());
                species.setText(pet.getSpecies());
                breed.setText(pet.getBreed());
                sex.setText(pet.getSex());
                birthdate.setText(pet.getBirthdate().toString());
                databaseReference.child("users").child("patients").child(pet.getOwnerId()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User p=snapshot.getValue(User.class);
                        owner_name.setText(p.getFirstname()+" "+p.getLastname());
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

        add_intervention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPref = getContext().getSharedPreferences("pet", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("petId", petId);
                editor.apply();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new AddIntervention()).commit();
            }
        });
        medical_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPref = getContext().getSharedPreferences("pet", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("petId", petId);
                editor.apply();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new MedicalHistory()).commit();
            }
        });
        return view;
    }


}
