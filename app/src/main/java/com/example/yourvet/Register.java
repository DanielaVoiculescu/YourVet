package com.example.yourvet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yourvet.model.Request;
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

import java.util.regex.Pattern;

public class Register extends AppCompatActivity {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://yourvet-fdaf2-default-rtdb.firebaseio.com/");
    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    EditText lastname;
    String lastnameText;
    EditText firstname;
    String firstnameText;
    EditText email;
    String emailText;
    EditText phone;
    String phoneText;
    EditText username;
    EditText password;
    EditText conf_password;
    Button registerButton;
    TextView loginView;
    RadioGroup radioGroup;
    String passwordText;
    String conf_passwordText;
    String usernameTex;
    RadioButton radioButtonDoctor;
    RadioButton radioButtonUser;
    EditText doctorID;
    String doctorIdText;
    int radioId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        lastname = findViewById(R.id.lastname);
        firstname = findViewById(R.id.firstname);
        email = findViewById(R.id.mail);
        phone = findViewById(R.id.phone);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        conf_password = findViewById(R.id.confirm_password);
        registerButton = findViewById(R.id.registerButton);
        loginView = findViewById(R.id.loginNow);
        radioGroup = findViewById(R.id.radio_group);
        doctorID=findViewById(R.id.doctor_id);
        doctorID.setVisibility(View.INVISIBLE);
        radioButtonDoctor=findViewById(R.id.doctor);
        radioButtonUser=findViewById(R.id.simple_user);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lastnameText = lastname.getText().toString();
                firstnameText = firstname.getText().toString();
                emailText = email.getText().toString();
                phoneText = phone.getText().toString();
                passwordText = password.getText().toString();
                conf_passwordText = conf_password.getText().toString();
                usernameTex = username.getText().toString();
                doctorIdText=doctorID.getText().toString();
                final String role;
                if (lastnameText.isEmpty() || firstnameText.isEmpty() || emailText.isEmpty() || phoneText.isEmpty() || passwordText.isEmpty() || conf_passwordText.isEmpty() || usernameTex.isEmpty()) {
                    Toast.makeText(Register.this, "Te rugam sa completezi toate campurile", Toast.LENGTH_SHORT).show();
                } else if (!passwordText.equals(conf_passwordText)) {
                    Toast.makeText(Register.this, "Parolele nu coincid", Toast.LENGTH_SHORT).show();
                } else if (!Pattern.matches("^[A-Z][a-z]*$", lastnameText)) {
                    Toast.makeText(Register.this, "Numele de familie trebuie sa contina doar litere", Toast.LENGTH_SHORT).show();
                } else if (!Pattern.matches("^[A-Z][a-z]*$", firstnameText)) {
                    Toast.makeText(Register.this, "Prenumele trebuie sa contina doar litere", Toast.LENGTH_SHORT).show();

                } else if (!Pattern.matches("^(\\+4|)?(07[0-8]{1}[0-9]{1}|02[0-9]{2}|03[0-9]{2}){1}?(\\s|\\.|\\-)?([0-9]{3}(\\s|\\.|\\-|)){2}$", phoneText)) {
                    Toast.makeText(Register.this, "Numar de telefon invalid", Toast.LENGTH_SHORT).show();
                } else if (!Pattern.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", emailText)) {
                    Toast.makeText(Register.this, "Email invalid", Toast.LENGTH_SHORT).show();
                } else if (!Pattern.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[#$@!%&*?])[A-Za-z\\d#$@!%&*?]{6,}$", passwordText)) {
                    Toast.makeText(Register.this, "Parola invalida", Toast.LENGTH_SHORT).show();
                } else {

                    mAuth.createUserWithEmailAndPassword(emailText,passwordText).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                User user= new User(lastnameText,firstnameText,usernameTex,emailText,passwordText,phoneText,"user");
                                databaseReference.child("users").child(mAuth.getCurrentUser().getUid()).setValue(user);
                                Toast.makeText(Register.this, "Utilizator creat cu succes!", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                    /*databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(usernameTex)) {
                                Toast.makeText(Register.this, "Numele de utilizator exista deja", Toast.LENGTH_SHORT).show();
                            } else {

                                User user= new User(lastnameText,firstnameText,usernameTex,emailText,passwordText,phoneText,"user");
                                databaseReference.child("users").child(usernameTex).setValue(user);
                                radioId = radioGroup.getCheckedRadioButtonId();
                                if(!doctorIdText.isEmpty()){

                                    Request request=new Request(usernameTex, lastnameText,firstnameText,doctorIdText);
                                    databaseReference.child("doctorRequest").child(usernameTex).setValue(request);

                                }


                                Toast.makeText(Register.this, "Utilizator creat cu succes!", Toast.LENGTH_SHORT).show();
                                //finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });*/

                }
            }
        });

        loginView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Register.this,Login.class));
                finish();
            }
        });
        radioButtonDoctor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()  {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    doctorID.setVisibility(View.VISIBLE);
                    radioButtonUser.setChecked(false);
                }
            }
        });
        radioButtonUser.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    doctorID.setVisibility(View.INVISIBLE);
                    radioButtonDoctor.setChecked(false);
                }
            }
        });


    }
}