package com.example.yourvet.admin;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.yourvet.R;
import com.example.yourvet.model.Request;
import com.example.yourvet.model.Specialization;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;

import java.util.ArrayList;
import java.util.Random;

public class AddDoctor extends Fragment {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://yourvet-fdaf2-default-rtdb.firebaseio.com/");
    private static final FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
    private String mail,password;
    private EditText lastname,firstname,phone, email;
    private Button addButton;
    private AutoCompleteTextView autoCompleteTextViewSpecialization;
    private ArrayAdapter adapterItemSpecialization;
    private ArrayList<String> specializations=new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_doctor, container, false);
        lastname=view.findViewById(R.id.doctor_lastname);
        firstname=view.findViewById(R.id.doctor_firstname);
        phone=view.findViewById(R.id.doctor_phone);
        email=view.findViewById(R.id.doctor_email);
        addButton=view.findViewById(R.id.add_doctor);
        autoCompleteTextViewSpecialization=view.findViewById(R.id.doctor_spec);
        databaseReference.child("specialization").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot d1: snapshot.getChildren()){
                    specializations.add(d1.child("name").getValue().toString());
                    adapterItemSpecialization.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        adapterItemSpecialization=new ArrayAdapter(getContext(),R.layout.list_breed,specializations);
        autoCompleteTextViewSpecialization.setAdapter(adapterItemSpecialization);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.createUserWithEmailAndPassword(email.getText().toString(),generateTemporaryPassword()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                    }
                });
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                    if(getContext().checkSelfPermission(Manifest.permission.SEND_SMS)== PackageManager.PERMISSION_GRANTED);
                    sendSMS();
                }
                else{
                    requestPermissions(new String[]{Manifest.permission.SEND_SMS},1);
                }
            }
        });

    }

    public  String generateTemporaryPassword() {

        // create character rule for lower case
        CharacterRule LCR = new CharacterRule(EnglishCharacterData.LowerCase);
        // set number of lower case characters
        LCR.setNumberOfCharacters(2);

        // create character rule for upper case
        CharacterRule UCR = new CharacterRule(EnglishCharacterData.UpperCase);
        // set number of upper case characters
        UCR.setNumberOfCharacters(2);

        // create character rule for digit
        CharacterRule DR = new CharacterRule(EnglishCharacterData.Digit);
        // set number of digits
        DR.setNumberOfCharacters(2);

        // create character rule for lower case
        CharacterRule SR = new CharacterRule(EnglishCharacterData.Special);
        // set number of special characters
        SR.setNumberOfCharacters(2);

        // create instance of the PasswordGenerator class
        PasswordGenerator passGen = new PasswordGenerator();

        // call generatePassword() method of PasswordGenerator class to get Passay generated password
        String password = passGen.generatePassword(8, SR, LCR, UCR, DR);

        // return Passay generated password to the main() method
        return password;
    }
    private void sendSMS(){
        String phoneNo=phone.getText().toString();
        String message="aaa";
        try {
            SmsManager smsManager=SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo,null,message,null,null);
            Toast.makeText(getContext(), "Mesaj trimis cu succes!", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
}
