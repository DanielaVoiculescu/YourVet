package com.example.yourvet;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Database {
    private static final DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://yourvet-fdaf2-default-rtdb.firebaseio.com/");
    private static boolean is;
    public static boolean isDoctorById(String id){
        is=false;
        databaseReference.child("users").child("doctors").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    is=true;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
        return is;
    }
    public static boolean isPatientById(String id){
        is=false;
        databaseReference.child("users").child("patients").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    is=true;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
        return is;
    }

}
