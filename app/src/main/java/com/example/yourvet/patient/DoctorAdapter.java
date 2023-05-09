package com.example.yourvet.patient;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.example.yourvet.R;
import com.example.yourvet.doctor.Profile;
import com.example.yourvet.model.Doctor;
import com.example.yourvet.model.Pet;
import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DoctorAdapter extends BaseAdapter implements Filterable {
    private ArrayList<Doctor> list = new ArrayList<Doctor>();
    private Context context;
    private ImageView imageView;
    private TextView name, specialization, description;
    private MaterialCardView linearLayout;
    private ArrayList<Doctor> filteredList=new ArrayList<>();
    public DoctorAdapter(ArrayList<Doctor> list, Context context) {
        this.list = list;
        this.context = context;
        this.filteredList=list;
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
        LayoutInflater inflater=(LayoutInflater)  context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view=inflater.inflate(R.layout.doctor_layout,viewGroup,false);
        imageView=view.findViewById(R.id.doctor_photo);
        name=view.findViewById(R.id.name_doctor);
        specialization=view.findViewById(R.id.specialization);
        description=view.findViewById(R.id.description);
        linearLayout=view.findViewById(R.id.choose_doctor);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Doctor an= filteredList.get(i);
                System.out.println(an.getPhotoUrl());
                /*Intent i=new Intent(context, Profile.class);
                i.putExtra("doctor",an);*/
                SharedPreferences sharedPref = context.getSharedPreferences("myKey", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("doctorId", filteredList.get(i).getId());
                editor.apply();
                FragmentActivity fragmentActivity=(FragmentActivity) context;
                fragmentActivity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new Profile()).commit();

            }
        });
        name.setText(filteredList.get(i).getLastname()+" "+filteredList.get(i).getFirstname());
        specialization.setText(filteredList.get(i).getSpecialization());
        description.setText(filteredList.get(i).getDescription());
        String img=  filteredList.get(i).getPhotoUrl();
        Picasso.get().load(img).into(imageView);
        return  view;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                FilterResults results = new FilterResults();

                if (TextUtils.isEmpty(charSequence)){
                    results.values=list;
                    results.count=list.size();
                }
                else {
                    ArrayList<Doctor> doctors=new ArrayList<>();
                    for (Doctor doctor: list){
                        if (doctor.getLastname().toLowerCase().contains(charSequence.toString().toLowerCase())||doctor.getFirstname().toLowerCase().contains(charSequence.toString().toLowerCase())){
                            doctors.add(doctor);
                        }
                    }
                    results.values=doctors;
                    results.count=doctors.size();
                }


                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredList =(ArrayList<Doctor>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


}
