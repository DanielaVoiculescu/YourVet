package com.example.yourvet.patient;

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

import com.example.yourvet.R;
import com.example.yourvet.authentification.Login;
import com.example.yourvet.doctor.Profile;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserMainPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
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
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new PatientProfile()).commit();
                navigationView.setCheckedItem(R.id.nav_profil);
                break;
            case R.id.view_pets:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ViewPets()).commit();
                navigationView.setCheckedItem(R.id.view_pets);
                break;
            case R.id.view_doctors:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ViewDoctors()).commit();
                navigationView.setCheckedItem(R.id.view_doctors);
                break;
            case R.id.nav_logout:
                AlertDialog.Builder builder= new AlertDialog.Builder(this);
                builder.setTitle("Logout");
                builder.setMessage("Sunteti sigur ca vreti sa va deconectati?");
                builder.setPositiveButton("Da", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mAuth.signOut();
                        startActivity(new Intent(UserMainPage.this, Login.class));

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
        setContentView(R.layout.activity_user_main_page);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
        headerView=navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);
        imageView=headerView.findViewById(R.id.imgProfile);
        text=headerView.findViewById(R.id.header_text);
        databaseReference.child("users").child("patients").child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
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
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new PatientProfile()).commit();
//            navigationView.setCheckedItem(R.id.nav_profil);
        }
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