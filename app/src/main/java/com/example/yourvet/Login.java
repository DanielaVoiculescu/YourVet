package com.example.yourvet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yourvet.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://yourvet-fdaf2-default-rtdb.firebaseio.com/");
    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final EditText email= findViewById(R.id.email);
        final EditText password= findViewById(R.id.password);
        final Button loginButton= findViewById(R.id.loginBtn);
        final TextView registerNowButton= findViewById(R.id.registerNowBtn);

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
                                databaseReference.child("users").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        User user=new User();
                                        user=snapshot.getValue(User.class);
                                        if(user.getRole().equals("admin")){
                                            startActivity(new Intent(Login.this,AdminMainPage.class));
                                            finish();
                                        }
                                        else if (user.getRole().equals("doctor")){
                                            startActivity(new Intent(Login.this,DoctorMainPage.class));
                                            finish();
                                        }
                                        else
                                        {
                                            startActivity(new Intent(Login.this,UserMainPage.class));
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
                startActivity(new Intent(Login.this,Register.class));
                finish();
            }
        });
    }
}