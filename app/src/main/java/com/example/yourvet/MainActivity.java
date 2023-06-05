package com.example.yourvet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.yourvet.admin.MainPageAdmin;
import com.example.yourvet.authentification.Login;
import com.example.yourvet.doctor.DoctorMainPage;
import com.example.yourvet.patient.UserMainPage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth auth=FirebaseAuth.getInstance();
    private DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://yourvet-fdaf2-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startActivity(new Intent(MainActivity.this, Login.class));
        finish();
        /*if(auth.getCurrentUser()!=null){
            databaseReference.child("roles").child(auth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.getValue(String.class).equals("doctor")){
                        startActivity(new Intent(MainActivity.this, DoctorMainPage.class));
                        finish();
                    }
                    else
                    if (snapshot.getValue(String.class).equals("patient")){
                        startActivity(new Intent(MainActivity.this, UserMainPage.class));
                        finish();
                    }
                    else
                    {
                        startActivity(new Intent(MainActivity.this, MainPageAdmin.class));
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else {
            startActivity(new Intent(MainActivity.this, Login.class));
            finish();
        }*/
    }
}