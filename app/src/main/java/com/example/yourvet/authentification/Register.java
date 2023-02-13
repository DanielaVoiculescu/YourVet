package com.example.yourvet.authentification;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yourvet.R;
import com.example.yourvet.model.Patient;
import com.example.yourvet.model.Request;
import com.example.yourvet.model.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class Register extends AppCompatActivity {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://yourvet-fdaf2-default-rtdb.firebaseio.com/");
    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    StorageReference storageReference= FirebaseStorage.getInstance().getReference("userProfile");
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
    CircleImageView imgGallery;
    Uri imgUri;
    public static final int PICK_IMAGE_REQUEST=1;
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
        imgGallery=findViewById(R.id.imgGallery);
        Button btnGallery=findViewById(R.id.btnGallery);
        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iGallery=new Intent(Intent.ACTION_PICK);
                iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                mGetContent.launch("image/*");
            }
        });
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


                                if(!doctorIdText.isEmpty()){
                                    Request r=new Request(usernameTex,lastnameText,firstnameText,doctorIdText,mAuth.getCurrentUser().getUid());
                                    databaseReference.child("requests").child(doctorIdText).setValue(r);
                                }
                                StorageReference fileReference= storageReference.child(mAuth.getCurrentUser().getUid()+"."+getFileExtension(imgUri));

                              UploadTask uploadTask= fileReference.putFile(imgUri);
                                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                    @Override
                                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                        if (!task.isSuccessful()) {
                                            throw task.getException();
                                        }

                                        // Continue with the task to get the download URL

                                        return fileReference.getDownloadUrl();

                                    }
                                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        if (task.isSuccessful()) {
                                            User user;
                                            Uri downloadUri = task.getResult();
                                            if(!doctorIdText.isEmpty()){
                                                user= new User(lastnameText,firstnameText,usernameTex,emailText,passwordText,phoneText,downloadUri.toString(),mAuth.getCurrentUser().getUid());
                                                databaseReference.child("users").child("patients").child(mAuth.getCurrentUser().getUid()).setValue(user);
                                            }
                                            else{
                                                user= new Patient(lastnameText,firstnameText,usernameTex,emailText,passwordText,phoneText,downloadUri.toString(),mAuth.getCurrentUser().getUid());
                                                databaseReference.child("roles").child(mAuth.getCurrentUser().getUid()).setValue("patient");
                                                databaseReference.child("users").child("patients").child(mAuth.getCurrentUser().getUid()).setValue(user);
                                            }



                                        } else {

                                        }
                                    }
                                });

                                Toast.makeText(Register.this, "Utilizator creat cu succes!", Toast.LENGTH_SHORT).show();


                            }
                        }
                    });

                }
            }
        });

        loginView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Register.this, Login.class));
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
    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    Picasso.get().load(uri).into(imgGallery);

                    imgUri=uri;
                }
            });
    private String getFileExtension(Uri uri){
        ContentResolver cR=getContentResolver();
        MimeTypeMap mime=MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
}