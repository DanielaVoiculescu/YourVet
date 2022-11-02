package com.example.yourvet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Register extends AppCompatActivity {
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://yourvet-fdaf2-default-rtdb.firebaseio.com/");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        final EditText lastname=findViewById(R.id.lastname);
        final EditText firstname=findViewById(R.id.firstname);
        final EditText email=findViewById(R.id.mail);
        final EditText phone=findViewById(R.id.phone);
        final EditText username=findViewById(R.id.username);
        final EditText password=findViewById(R.id.password);
        final EditText conf_password=findViewById(R.id.confirm_password);
        final Button registerButton= findViewById(R.id.registerButton);
        final TextView loginView=findViewById(R.id.loginNow);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String lastnameText=lastname.getText().toString();
                final String firstnameText=firstname.getText().toString();
                final String emailText=email.getText().toString();
                final  String phoneText=phone.getText().toString();
                final String passwordText=password.getText().toString();
                final String conf_passwordText=conf_password.getText().toString();
                final  String usernameTex=username.getText().toString();
                if(lastnameText.isEmpty()||firstnameText.isEmpty()||emailText.isEmpty()||phoneText.isEmpty()||passwordText.isEmpty()||conf_passwordText.isEmpty()||usernameTex.isEmpty()){
                    Toast.makeText(Register.this, "Te rugam sa completezi toate campurile", Toast.LENGTH_SHORT).show();
                }
                else
                    if(!passwordText.equals(conf_passwordText)){
                        Toast.makeText(Register.this, "Parolele nu coincid", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.hasChild(usernameTex)){
                                    Toast.makeText(Register.this, "Numele de utilizator exista deja", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {   //databaseReference.child("users").child(usernameTex).setValue(usernameTex);
                                    databaseReference.child("users").child(usernameTex).child("lastname").setValue(lastnameText);
                                    databaseReference.child("users").child(usernameTex).child("firsname").setValue(firstnameText);
                                    databaseReference.child("users").child(usernameTex).child("phone").setValue(phoneText);
                                    databaseReference.child("users").child(usernameTex).child("password").setValue(passwordText);
                                    databaseReference.child("users").child(usernameTex).child("mail").setValue(emailText);

                                    Toast.makeText(Register.this,"Utilizator creat cu succes!",Toast.LENGTH_SHORT).show();
                                    //finish();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
            }
        });
        loginView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}