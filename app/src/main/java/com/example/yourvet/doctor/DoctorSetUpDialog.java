package com.example.yourvet.doctor;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.example.yourvet.R;
import com.example.yourvet.patient.PetProfileFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DoctorSetUpDialog extends Dialog {
    private Context context;
    public DoctorSetUpDialog(@NonNull Context context) {
        super(context);
        this.context=context;
    }
    EditText description;
    Button set_working_hours;
    private DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://yourvet-fdaf2-default-rtdb.firebaseio.com/");
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_add_details);
        description=findViewById(R.id.description);
        set_working_hours=findViewById(R.id.set_working);
        set_working_hours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child("users").child("doctors").child(mAuth.getCurrentUser().getUid()).child("description").setValue(description.getText().toString());
                FragmentActivity fragmentActivity=(FragmentActivity) context;
                fragmentActivity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new WorkingHours()).commit();
                dismiss();
            }
        });
    }
}
