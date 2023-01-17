package com.example.yourvet.patient;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.yourvet.R;
import com.example.yourvet.model.Date;
import com.example.yourvet.model.Pet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class AddPet extends Fragment {
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://yourvet-fdaf2-default-rtdb.firebaseio.com/");

    private AutoCompleteTextView autoCompleteTextViewBreed;
    private ArrayAdapter<CharSequence> adapterItemBreed;

    private AutoCompleteTextView autoCompleteTextViewSpecies;
    private ArrayAdapter<CharSequence> adapterItemSpecies;

    private Button autoCompleteTextViewData;

    private EditText pet_name;

    private String pet_species;
    private String pet_breed;
    private Date date;
    private EditText dayText;
    private EditText monthText;
    private EditText yeatText;
    private Button add_button;
    TextView text;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_pet, container, false);

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        pet_name = view.findViewById(R.id.pet_name);
        add_button=view.findViewById(R.id.add_pet);
        autoCompleteTextViewBreed = view.findViewById(R.id.pet_breed);
        autoCompleteTextViewSpecies = view.findViewById(R.id.pet_species);

        dayText=view.findViewById(R.id.day);
        monthText=view.findViewById(R.id.month);
        yeatText=view.findViewById(R.id.year);


        adapterItemSpecies = ArrayAdapter.createFromResource(getContext(), R.array.species, R.layout.list_breed);

        autoCompleteTextViewSpecies.setAdapter(adapterItemSpecies);
        autoCompleteTextViewSpecies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                pet_species = adapterView.getItemAtPosition(i).toString();
                switch (pet_species) {
                    case "Caine":
                        adapterItemBreed = ArrayAdapter.createFromResource(getContext(), R.array.dogs_array, R.layout.list_breed);
                        autoCompleteTextViewBreed.setAdapter(adapterItemBreed);
                        break;
                    case "Pisica":
                        adapterItemBreed = ArrayAdapter.createFromResource(getContext(), R.array.cats_array, R.layout.list_breed);
                        autoCompleteTextViewBreed.setAdapter(adapterItemBreed);
                        break;

                }
                if (pet_species == null) {
                    autoCompleteTextViewBreed.dismissDropDown();
                    Toast.makeText(getContext(), "Va rugam selectati rasa", Toast.LENGTH_SHORT).show();
                }
                autoCompleteTextViewBreed.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        pet_breed = adapterView.getItemAtPosition(i).toString();

                    }
                });
            }
        });

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date date=new Date(Integer.parseInt(dayText.getText().toString()),Integer.parseInt(monthText.getText().toString()),Integer.parseInt(yeatText.getText().toString()));
                Pet pet=new Pet(UUID.randomUUID().toString(),pet_name.getText().toString(),mAuth.getCurrentUser().getUid(),pet_breed,pet_species,date);
                databaseReference.child("pets").child(pet.getId()).setValue(pet);
            }
        });
    }


}
