package com.example.yourvet.doctor;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
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

import com.example.yourvet.Message.PrivateChat;
import com.example.yourvet.R;
import com.example.yourvet.model.Doctor;
import com.example.yourvet.model.User;
import com.example.yourvet.patient.MakeAppointement;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Profile extends Fragment {
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://yourvet-fdaf2-default-rtdb.firebaseio.com/");
    private TextView name, email, phone,specialization, description;
    private ImageView profile_photo;
    private User user;
    private Doctor doctor;
    private Button edit_button,appointment_button,message_button;
    private String value;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doctor_profile, container, false);

        name = view.findViewById(R.id.profile_name);
        email = view.findViewById(R.id.profile_email);
        phone = view.findViewById(R.id.profile_phone);
        profile_photo = view.findViewById(R.id.profile_image);
        specialization=view.findViewById(R.id.doctor_specialization);
        description=view.findViewById(R.id.doctor_desciption);
        edit_button=view.findViewById(R.id.edit_button);
        appointment_button=view.findViewById(R.id.do_appointement);
        message_button=view.findViewById(R.id.message_button);
        message_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPref = getContext().getSharedPreferences("reciver", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("reciverId", value);
                editor.apply();
               getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new PrivateChat()).commit();
            }
        });
        databaseReference.child("roles").child(auth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue(String.class).equals("doctor")){
                    message_button.setVisibility(View.INVISIBLE);
                    appointment_button.setVisibility(View.INVISIBLE);
                    databaseReference.child("users").child("doctors").child(auth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Doctor u1 = snapshot.getValue(Doctor.class);
                            System.out.println(u1);
                            name.setText(u1.getFirstname() + " " + u1.getLastname());
                            email.setText(u1.getEmail());
                            phone.setText(u1.getPhoneNr());
                            specialization.setText(u1.getSpecialization());
                            description.setText(u1.getDescription());
                            String img = u1.getPhotoUrl();
                            Picasso.get().load(img).into(profile_photo);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else {
                    edit_button.setVisibility(View.INVISIBLE);
                    SharedPreferences sharedPreferences =getContext().getSharedPreferences("myKey", MODE_PRIVATE);
                     value = sharedPreferences.getString("doctorId","");
                    databaseReference.child("users").child("doctors").child(value).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Doctor u1 = snapshot.getValue(Doctor.class);
                            System.out.println(u1);
                            name.setText(u1.getFirstname() + " " + u1.getLastname());
                            email.setText(u1.getEmail());
                            phone.setText(u1.getPhoneNr());
                            specialization.setText(u1.getSpecialization());
                            description.setText(u1.getDescription());
                            String img = u1.getPhotoUrl();
                            Picasso.get().load(img).into(profile_photo);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    /*name.setText(doctor.getFirstname() + " " + doctor.getLastname());
                    email.setText(doctor.getEmail());
                    phone.setText(doctor.getPhoneNr());
                    specialization.setText(doctor.getSpecialization());
                    description.setText(doctor.getDescription());
                    String img = doctor.getPhotoUrl();
                    Picasso.get().load(img).into(profile_photo);*/
//                    System.out.println(doctor.getEmail());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        appointment_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new MakeAppointement()).commit();
            }
        });
        return view;

    }
}

