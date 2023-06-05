package com.example.yourvet.patient;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.example.yourvet.R;
import com.example.yourvet.doctor.Profile;
import com.example.yourvet.model.Doctor;
import com.example.yourvet.model.Intervention;
import com.example.yourvet.model.Pet;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DoctorAdapter extends BaseAdapter implements Filterable {
    private ArrayList<Doctor> list = new ArrayList<Doctor>();
    private final Context context;
    private ImageView imageView;
    private TextView name, specialization, description;
    private MaterialCardView linearLayout;
    private ArrayList<Doctor> filteredList = new ArrayList<>();
    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://yourvet-fdaf2-default-rtdb.firebaseio.com/");
    private String grades;

    public DoctorAdapter(ArrayList<Doctor> list, Context context) {
        this.list = list;
        this.context = context;
        this.filteredList = list;
    }

    @Override
    public int getCount() {
        return filteredList.size();
    }

    @Override
    public Object getItem(int i) {
        return filteredList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.doctor_layout, viewGroup, false);
        imageView = view.findViewById(R.id.doctor_photo);
        name = view.findViewById(R.id.name_doctor);
        specialization = view.findViewById(R.id.specialization);

        description = view.findViewById(R.id.description);
        linearLayout = view.findViewById(R.id.choose_doctor);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Doctor an = filteredList.get(i);
                System.out.println(an.getPhotoUrl());
                SharedPreferences sharedPref = context.getSharedPreferences("myKey", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("doctorId", filteredList.get(i).getId());
                editor.apply();
                FragmentActivity fragmentActivity = (FragmentActivity) context;
                fragmentActivity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Profile()).commit();

            }
        });


        name.setText(filteredList.get(i).getLastname() + " " + filteredList.get(i).getFirstname());
        specialization.setText(filteredList.get(i).getSpecialization());
        description.setText(filteredList.get(i).getDescription());
        String img = filteredList.get(i).getPhotoUrl();
        Picasso.get().load(img).into(imageView);
        return view;
    }

    private String getGrade(String id) {
        databaseReference.child("interventions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                float doctorRate = 0;
                int number = 0;
                for (DataSnapshot d : snapshot.getChildren()) {
                    Intervention inte = d.getValue(Intervention.class);
                    if (inte.getDoctorId().equals(id)) {
                        if (inte.getRate() != 0) {
                            Log.d("inter_", "" + inte.getRate());
                            doctorRate += inte.getRate();
                            number++;
                        }
                    }

                }
                grades = doctorRate / number + "/5";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return grades;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                FilterResults results = new FilterResults();

                if (TextUtils.isEmpty(charSequence)) {
                    results.values = list;
                    results.count = list.size();
                } else {
                    ArrayList<Doctor> doctors = new ArrayList<>();
                    for (Doctor doctor : list) {
                        if (doctor.getLastname().toLowerCase().contains(charSequence.toString().toLowerCase()) || doctor.getFirstname().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                            doctors.add(doctor);
                        }
                    }
                    results.values = doctors;
                    results.count = doctors.size();
                }


                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredList = (ArrayList<Doctor>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


}
