package com.example.yourvet.patient;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yourvet.R;
import com.example.yourvet.model.Doctor;
import com.example.yourvet.model.Pet;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DoctorAdapter extends BaseAdapter {
    private ArrayList<Doctor> list = new ArrayList<Doctor>();
    private Context context;
    private ImageView imageView;
    private TextView name, specialization, description;
    public DoctorAdapter(ArrayList<Doctor> list, Context context) {
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
        view=inflater.inflate(R.layout.doctor_layout,viewGroup,false);
        imageView=view.findViewById(R.id.doctor_photo);
        name=view.findViewById(R.id.name_doctor);
        specialization=view.findViewById(R.id.specialization);
        description=view.findViewById(R.id.description);
        name.setText(list.get(i).getLastname()+" "+list.get(i).getFirstname());
        specialization.setText(list.get(i).getSpecialization());
        description.setText(list.get(i).getDescription());
        String img=  list.get(i).getPhotoUrl();
        Picasso.get().load(img).into(imageView);
        return  view;
    }
}
