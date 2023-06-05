package com.example.yourvet.doctor;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;

import com.example.yourvet.R;
import com.example.yourvet.model.Pet;
import com.example.yourvet.model.User;
import com.example.yourvet.patient.PetProfileFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PetForDoctorAdapter extends BaseAdapter {
    private DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://yourvet-fdaf2-default-rtdb.firebaseio.com/");

    private ArrayList<Pet> list = new ArrayList<Pet>();
    private Context context;
    private ImageView imageView;
    private TextView name, rasa,sex,owner_name;
    private CardView linearLayout;
    public PetForDoctorAdapter(ArrayList<Pet> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater=(LayoutInflater)  context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view=inflater.inflate(R.layout.petfordoctor_layout,viewGroup,false);
        linearLayout=view.findViewById(R.id.choose_pet);
        imageView=view.findViewById(R.id.pet_photo);
        name=view.findViewById(R.id.name_pet);
        sex=view.findViewById(R.id.sex);
        rasa=view.findViewById(R.id.rasa);
        owner_name=view.findViewById(R.id.name_owner);
        name.setText(list.get(i).getName());
        sex.setText(list.get(i).getSex());
        String img=  list.get(i).getPhotoUrl();
        Picasso.get().load(img).into(imageView);
        rasa.setText(list.get(i).getBreed());
        databaseReference.child("users").child("patients").child(list.get(i).getOwnerId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User p=snapshot.getValue(User.class);
                owner_name.setText(p.getFirstname()+" "+p.getLastname());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPref = context.getSharedPreferences("pet", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("petId", list.get(i).getId());
                editor.apply();
                SharedPreferences sharedPref1 = context.getSharedPreferences("de_unde_vine", MODE_PRIVATE);
                SharedPreferences.Editor editor1 = sharedPref1.edit();
                editor1.putString("nume", this.toString());
                editor1.apply();

                FragmentActivity fragmentActivity=(FragmentActivity) context;
                fragmentActivity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new PetProfileFragment()).commit();

            }
        });
        return view;
    }
}
