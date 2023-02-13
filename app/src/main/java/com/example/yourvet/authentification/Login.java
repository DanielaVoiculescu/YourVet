package com.example.yourvet.authentification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yourvet.admin.AdminMainPage;
import com.example.yourvet.doctor.DoctorMainPage;
import com.example.yourvet.R;
import com.example.yourvet.patient.UserMainPage;
import com.example.yourvet.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    private DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://yourvet-fdaf2-default-rtdb.firebaseio.com/");
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private EditText email, password;
    private Button loginButton;
    private TextView registerNowButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email= findViewById(R.id.email);
        password= findViewById(R.id.password);
        loginButton= findViewById(R.id.loginBtn);
        registerNowButton= findViewById(R.id.registerNowBtn);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String emailText= email.getText().toString();
                final String passwordText=password.getText().toString();
                if(emailText.isEmpty()||passwordText.isEmpty()){
                    Toast.makeText(Login.this,"Te rugam sa introduci mail-ul sau parola",Toast.LENGTH_SHORT).show();
                }
                else {
                    mAuth.signInWithEmailAndPassword(emailText,passwordText).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){

                                String userID=mAuth.getCurrentUser().getUid();
                                FirebaseUser firebaseUser=mAuth.getCurrentUser();


                                        databaseReference.child("roles").child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if(snapshot.getValue(String.class).equals("doctor")){
                                                    startActivity(new Intent(Login.this, DoctorMainPage.class));
                                                    finish();
                                                }
                                                else
                                                if (snapshot.getValue(String.class).equals("patient")){
                                                    startActivity(new Intent(Login.this, UserMainPage.class));
                                                    finish();
                                                }
                                                else
                                                {
                                                    startActivity(new Intent(Login.this, AdminMainPage.class));
                                                    finish();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });




                            }
                            else{
                                Toast.makeText(Login.this,"Email sau parola incorecte",Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                }
            }
        });
        registerNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, Register.class));
                finish();
            }
        });
    }
}