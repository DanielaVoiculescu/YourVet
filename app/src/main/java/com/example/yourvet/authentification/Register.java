package com.example.yourvet.authentification;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.media.ExifInterface;
import android.util.Log;
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

import com.example.yourvet.model.Request;
import com.example.yourvet.model.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
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
    TextView loginView,password_error;
    RadioGroup radioGroup;
    String passwordText;
    String conf_passwordText;
    RadioButton radioButtonDoctor;
    RadioButton radioButtonUser;
    TextInputLayout doctorId;
    EditText doctorID;
    String doctorIdText;
    CircleImageView imgGallery;
    Uri imgUri;
    public static final int PICK_IMAGE_REQUEST=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        lastname = findViewById(R.id.lastname);
        firstname= findViewById(R.id.firstname);
        email = findViewById(R.id.mail);
        phone = findViewById(R.id.phone);
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
        password_error=findViewById(R.id.password_error);
        doctorId=findViewById(R.id.doctor_id_label);
        doctorId.setVisibility(View.GONE);

        imgGallery.setOnClickListener(new View.OnClickListener() {
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

                doctorIdText=doctorID.getText().toString();
                final String role;
                if (lastnameText.isEmpty()
                        || firstnameText.isEmpty()
                        || emailText.isEmpty() ||
                        phoneText.isEmpty()
                        || passwordText.isEmpty()
                        || conf_passwordText.isEmpty()) {
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
                    password_error.setVisibility(View.VISIBLE);
                    Toast.makeText(Register.this, "Parola invalida", Toast.LENGTH_SHORT).show(); }
                else {

                    mAuth.createUserWithEmailAndPassword(emailText,passwordText).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){

                                if(!doctorIdText.isEmpty()){
                                    Request r=new Request(lastnameText,firstnameText,doctorIdText,mAuth.getCurrentUser().getUid());
                                    databaseReference.child("requests").child(mAuth.getCurrentUser().getUid()).setValue(r); }
                                StorageReference fileReference= storageReference.child(mAuth.getCurrentUser().getUid()+"."+getFileExtension(imgUri));
                                UploadTask uploadTask= fileReference.putFile(imgUri);
                                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                    @Override
                                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                        if (!task.isSuccessful()) {
                                            throw task.getException(); }
                                        return fileReference.getDownloadUrl(); }
                                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        if (task.isSuccessful()) {
                                            User user;
                                            Uri downloadUri = task.getResult();
                                            if(!doctorIdText.isEmpty()){
                                                user= new User(lastnameText,firstnameText,emailText,passwordText,phoneText,downloadUri.toString(),mAuth.getCurrentUser().getUid());
                                                databaseReference.child("roles").child(mAuth.getCurrentUser().getUid()).setValue("unconfirmed_doctor");
                                                databaseReference.child("users").child("patients").child(mAuth.getCurrentUser().getUid()).setValue(user); }
                                            else{
                                                user= new User(lastnameText,firstnameText,emailText,passwordText,phoneText,downloadUri.toString(),mAuth.getCurrentUser().getUid());
                                                databaseReference.child("roles").child(mAuth.getCurrentUser().getUid()).setValue("patient");
                                                databaseReference.child("users").child("patients").child(mAuth.getCurrentUser().getUid()).setValue(user); } }
                                        else { } }});
                                Toast.makeText(Register.this, "Utilizator creat cu succes!", Toast.LENGTH_SHORT).show(); } }});

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
                    doctorId.setVisibility(View.VISIBLE);
                    doctorID.setVisibility(View.VISIBLE);
                    radioButtonUser.setChecked(false);
                }
            }
        });
        radioButtonUser.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    doctorId.setVisibility(View.GONE);
                    doctorID.setVisibility(View.VISIBLE);
                    radioButtonDoctor.setChecked(false);
                }
            }
        });


    }
    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
        @Override
        public void onActivityResult(Uri uri) {

            imgUri = uri;

            Log.d("orie",""+getRotationAngle(uri));

            Bitmap rotatedBitmap = rotateBitmapFromUri(Register.this,uri, getRotationAngle(uri));
            if (rotatedBitmap != null) {
                // Afișați imaginea rotită în ImageView
                imgGallery.setImageBitmap(rotatedBitmap);
            } else {
                // Manipulați cazul în care imaginea rotită este nulă (de exemplu, afișați un mesaj de eroare)
                Toast.makeText(Register.this, "Nu s-a putut roti imaginea.", Toast.LENGTH_SHORT).show();
            }

        }
    });
    public static Bitmap rotateBitmapFromUri(Context context, Uri imageUri, float degrees) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(imageUri);
            Bitmap originalBitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();

            Matrix matrix = new Matrix();
            matrix.postRotate(degrees);

            return Bitmap.createBitmap(originalBitmap, 0, 0, originalBitmap.getWidth(), originalBitmap.getHeight(), matrix, true);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private int getRotationAngle(Uri uri) {
        int orientation1 = 0;
        InputStream inputStream = null;
        try {
            inputStream = getContentResolver().openInputStream(uri);
            if (inputStream != null) {
                ExifInterface exifInterface = new ExifInterface(inputStream);
                int orientation = exifInterface.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        orientation1=90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        orientation1=180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        orientation1=270;
                        break;

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return orientation1;
    }
    private String getFileExtension(Uri uri){
        ContentResolver cR=getContentResolver();
        MimeTypeMap mime=MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
    private String getRealPathFromURI(Uri uri) {
        String realPath = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                realPath = cursor.getString(index);
                cursor.close();
            }
        } else if (uri.getScheme().equals("file")) {
            realPath = uri.getPath();
        }
        return realPath;
    }

}