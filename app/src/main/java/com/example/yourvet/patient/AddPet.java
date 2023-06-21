package com.example.yourvet.patient;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.yourvet.R;
import com.example.yourvet.model.Breed;
import com.example.yourvet.model.Date;
import com.example.yourvet.model.Pet;
import com.example.yourvet.model.Species;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.UUID;

public class AddPet extends Fragment {
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://yourvet-fdaf2-default-rtdb.firebaseio.com/");

    private AutoCompleteTextView autoCompleteTextViewBreed;
    private ArrayAdapter adapterItemBreed;

    private AutoCompleteTextView autoCompleteTextViewSpecies;
    private ArrayAdapter adapterItemSpecies;
    private AutoCompleteTextView autoCompleteTextViewSex;
    private ArrayAdapter<CharSequence> adapterItemSex;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private CalendarView birthdateC;


    private EditText pet_name;
    AutoCompleteTextView date_of_birth;

    private String pet_species;
    private String pet_breed, pet_sex;
    private Date date;
    private int day;
    private int month;
    private int year;
    private Button add_button, save_date, close_calendar, open_calendar;
    private TextView text;
    private final ArrayList<String> species = new ArrayList<>();
    private final ArrayList<String> breeds = new ArrayList<>();
    private ImageView imageView;
    private final StorageReference storageReference = FirebaseStorage.getInstance().getReference("petProfile");
    private Uri imgUri;

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
        add_button = view.findViewById(R.id.add_pet);
        autoCompleteTextViewBreed = view.findViewById(R.id.pet_breed);
        autoCompleteTextViewSpecies = view.findViewById(R.id.pet_species);
        autoCompleteTextViewSex = view.findViewById(R.id.pet_sex);
        adapterItemSex = ArrayAdapter.createFromResource(getContext(), R.array.sex, R.layout.list_breed);
        imageView = view.findViewById(R.id.photo_pet);
        date_of_birth = view.findViewById(R.id.date_of_birth);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iGallery = new Intent(Intent.ACTION_PICK);
                iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                mGetContent.launch("image/*");
            }
        });
        autoCompleteTextViewSex.setAdapter(adapterItemSex);
        databaseReference.child("species").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                species.clear();
                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                    Species species1 = dataSnapshot1.getValue(Species.class);
                    species.add(species1.getName());
                    adapterItemSpecies.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        adapterItemSpecies = new ArrayAdapter(getContext(), R.layout.list_breed, species);

        autoCompleteTextViewSpecies.setAdapter(adapterItemSpecies);
        autoCompleteTextViewSpecies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                pet_species = adapterView.getItemAtPosition(i).toString();
                databaseReference.child("breeds").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        breeds.clear();
                        for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                            Breed breed = dataSnapshot1.getValue(Breed.class);
                            if (breed.getSpecies().equals(pet_species)) {
                                breeds.add(breed.getName());
                                adapterItemBreed.notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
                adapterItemBreed = new ArrayAdapter(getContext(), R.layout.list_breed, breeds);
                autoCompleteTextViewBreed.setAdapter(adapterItemBreed);
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
        autoCompleteTextViewSex.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                pet_sex = adapterView.getItemAtPosition(i).toString();
            }
        });
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uuid = UUID.randomUUID().toString();
                StorageReference fileReference = storageReference.child(uuid + "." + getFileExtension(imgUri));

                UploadTask uploadTask = fileReference.putFile(imgUri);
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        // Continue with the task to get the download URL

                        return fileReference.getDownloadUrl();

                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            Date date = new Date(day, month, year);
                            Pet pet = new Pet(uuid, pet_name.getText().toString(), mAuth.getCurrentUser().getUid(), pet_breed, pet_species, date, downloadUri.toString(), pet_sex);
                            databaseReference.child("pets").child(pet.getId()).setValue(pet);
                            Toast.makeText(getContext(), "Animalul a fost adaugat cu succes", Toast.LENGTH_SHORT).show();
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ViewPets()).commit();


                        } else {

                        }
                    }
                });


            }
        });
        open_calendar = view.findViewById(R.id.open_calendar);
        open_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewCalendar();
            }
        });
    }

    public void createNewCalendar() {

        dialogBuilder = new AlertDialog.Builder(getContext());
        final View calendarView = getLayoutInflater().inflate(R.layout.calendar, null);
        birthdateC = (CalendarView) calendarView.findViewById(R.id.calendar_birthdate);
        birthdateC.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                day = i2;
                month = i1;
                year = i;
                String date = i2 + "/" + i1 + "/" + i;
                date_of_birth.setText(date);
            }
        });
        dialogBuilder.setView(calendarView);
        dialog = dialogBuilder.create();
        dialog.show();
    }

    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    Picasso.get().load(uri).into(imageView);
                    imgUri = uri;
                }
            });

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
}
