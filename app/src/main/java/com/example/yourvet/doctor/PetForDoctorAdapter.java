package com.example.yourvet.doctor;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
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
import java.util.HashMap;

public class PetForDoctorAdapter extends BaseAdapter {
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://yourvet-fdaf2-default-rtdb.firebaseio.com/");
    private ArrayList<Pet> list = new ArrayList<Pet>();
    private HashMap<String, String> ownersMap = new HashMap<>();
    private Context context;

    public PetForDoctorAdapter(ArrayList<Pet> list, Context context) {
        this.list = list;
        this.context = context;
        fetchOwnersData();
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    private void fetchOwnersData() {
        DatabaseReference ownersRef = databaseReference.child("users").child("patients");
        ownersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User owner = dataSnapshot.getValue(User.class);
                    if (owner != null) {
                        ownersMap.put(dataSnapshot.getKey(), owner.getFirstname() + " " + owner.getLastname());
                    }
                }
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });
    }

    // Other overridden methods

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.petfordoctor_layout, viewGroup, false);

            holder = new ViewHolder();
            holder.linearLayout = view.findViewById(R.id.choose_pet);
            holder.imageView = view.findViewById(R.id.pet_photo);
            holder.name = view.findViewById(R.id.name_pet);
            holder.sex = view.findViewById(R.id.sex);
            holder.rasa = view.findViewById(R.id.rasa);
            holder.owner_name = view.findViewById(R.id.name_owner);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Pet pet = list.get(i);
        holder.name.setText(pet.getName());
        holder.sex.setText(pet.getSex());
        String img = pet.getPhotoUrl();
        Picasso.get().load(img).into(holder.imageView);
        holder.rasa.setText(pet.getBreed());

        String ownerId = pet.getOwnerId();
        String ownerName = ownersMap.get(ownerId);
        Log.d("peto", pet.getId());
        if (ownerName != null) {
            holder.owner_name.setText(ownerName);
        }

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPref = context.getSharedPreferences("pet", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("petId", pet.getId());
                editor.apply();

                SharedPreferences sharedPref1 = context.getSharedPreferences("de_unde_vine", MODE_PRIVATE);
                SharedPreferences.Editor editor1 = sharedPref1.edit();
                editor1.putString("nume", this.toString());
                editor1.apply();

                FragmentActivity fragmentActivity = (FragmentActivity) context;
                fragmentActivity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PetProfileFragment()).commit();
            }
        });

        return view;
    }

    static class ViewHolder {
        CardView linearLayout;
        ImageView imageView;
        TextView name, rasa, sex, owner_name;
    }
}
