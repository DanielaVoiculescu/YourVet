package com.example.yourvet.doctor;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.yourvet.R;
import com.example.yourvet.model.Date;
import com.example.yourvet.model.Doctor;
import com.example.yourvet.model.Intervention;
import com.example.yourvet.model.Notification;
import com.example.yourvet.model.Pet;
import com.example.yourvet.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

public class AddIntervention extends Fragment {
    private String petId;
    private Date data;
    private ImageView imageView;
    private AutoCompleteTextView autoCompleteTextView,owner,date;
    private EditText simptome,diagnostic,interventie,recomandari;
    private Button open_calendar, add_intervention;
    private DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://yourvet-fdaf2-default-rtdb.firebaseio.com/");
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private CalendarView birthdateC;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_intervention, container, false);
        SharedPreferences sharedPreferences =getContext().getSharedPreferences("pet", MODE_PRIVATE);
        petId = sharedPreferences.getString("petId","");
        imageView=view.findViewById(R.id.photo_pet);
        autoCompleteTextView=view.findViewById(R.id.pet_name);
        owner=view.findViewById(R.id.owner_name);
        date=view.findViewById(R.id.date);
        simptome=view.findViewById(R.id.simptome);
        diagnostic=view.findViewById(R.id.diagnostic);
        interventie=view.findViewById(R.id.interventie);
        recomandari=view.findViewById(R.id.prescriptii);
        open_calendar=view.findViewById(R.id.open_calendar);
        add_intervention=view.findViewById(R.id.add_intervention);

        databaseReference.child("pets").child(petId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Pet p=snapshot.getValue(Pet.class);
                String img=  p.getPhotoUrl();
                Picasso.get().load(img).into(imageView);
                autoCompleteTextView.setText(p.getName());
                databaseReference.child("users").child("patients").child(p.getOwnerId()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User u=snapshot.getValue(User.class);
                        owner.setText(u.getFirstname()+" "+u.getLastname());
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
        open_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewCalendar();
            }
        });
        add_intervention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intervention i=new Intervention(petId,data,simptome.getText().toString(),diagnostic.getText().toString(),interventie.getText().toString(),recomandari.getText().toString(), mAuth.getCurrentUser().getUid());
                databaseReference.child("interventions").child(i.getId()).setValue(i);
                databaseReference.child("pets").child(petId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Pet p=snapshot.getValue(Pet.class);
                        databaseReference.child("users").child("doctors").child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Doctor d= snapshot.getValue(Doctor.class);
                                String title="Interventie";
                                String message="Doctorul "+ d.getFirstname()+ " "+d.getLastname()+" a realizat o interventie, " +i.getIntervention()+ " in data de "+data+" pentru animalul dumneavoastra, "+p.getName()+ ". Pentru mai multe detalii accesati istoricul medical al animalului. Va rugam sa acordati o nota doctorului tinand cont de modul in care a fost tratat animalul";
                                Calendar calendar1= Calendar.getInstance();
                                String time= calendar1.getTime().toString();

                                Notification notification= new Notification(title, message,p.getOwnerId(),time);
                                databaseReference.child("notifications").child(p.getOwnerId()).child(notification.getId()).setValue(notification);
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
            }
        });
        return view;
    }
    public void createNewCalendar() {

        dialogBuilder = new AlertDialog.Builder(getContext());
        final View calendarView = getLayoutInflater().inflate(R.layout.calendar, null);
        birthdateC = (CalendarView) calendarView.findViewById(R.id.calendar_birthdate);


        birthdateC.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                date.setText(i2+"/"+i1+"/"+i);
                data=new Date(i2,i1,i);
            }
        });
        dialogBuilder.setView(calendarView);
        dialog = dialogBuilder.create();
        dialog.show();
    }
}
