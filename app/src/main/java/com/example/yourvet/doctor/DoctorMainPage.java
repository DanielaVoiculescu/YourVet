package com.example.yourvet.doctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.yourvet.Message.ChatsFragment;
import com.example.yourvet.R;
import com.example.yourvet.authentification.Login;
import com.example.yourvet.model.Doctor;
import com.example.yourvet.patient.FragmentMainPage;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorMainPage extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://yourvet-fdaf2-default-rtdb.firebaseio.com/");
    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private DrawerLayout drawer;
    TextView text;
    NavigationView navigationView;
    View headerView;
    CircleImageView imageView;

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_profil:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new Profile()).commit();
                break;
            case R.id.view_pets:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ViewAllPets()).commit();
                break;
            case R.id.view_appointments:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ViewAppointements()).commit();
                break;
            case R.id.chats:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ChatsFragment()).commit();
                break;

            case R.id.nav_logout:
                AlertDialog.Builder builder= new AlertDialog.Builder(this);
                builder.setTitle("Logout");
                builder.setMessage("Sunteti sigur ca vreti sa va deconectati?");
                builder.setPositiveButton("Da", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mAuth.signOut();
                        startActivity(new Intent(DoctorMainPage.this, Login.class));

                        finish();
                    }
                });
                builder.setNegativeButton("Nu", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
                break;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_main_page);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
        headerView=navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);
        imageView=headerView.findViewById(R.id.imgProfile);
        text=headerView.findViewById(R.id.header_text);
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new Profile()).commit();
            }
        });
        databaseReference.child("users").child("doctors").child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Doctor d =snapshot.getValue(Doctor.class);
                if(snapshot.exists()&& snapshot.getChildrenCount()>0){
                    if(snapshot.hasChild("photoUrl")){
                        String img=  snapshot.child("photoUrl").getValue().toString();
                        Picasso.get().load(img).into(imageView);


                    }
                    text.setText(snapshot.child("firstname").getValue()+" "+snapshot.child("lastname").getValue());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        ActionBarDrawerToggle toggle= new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        if(savedInstanceState==null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new FragmentMainPage()).commit();
        }
        databaseReference.child("users").child("doctors").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Doctor doctor= snapshot.getValue(Doctor.class);
                if (doctor.getDescription().equals("Fara descriere")){
                    DoctorSetUpDialog doctorSetUpDialog=new DoctorSetUpDialog(DoctorMainPage.this);
                    doctorSetUpDialog.show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    public void onBackPressed(){
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }

    }
}