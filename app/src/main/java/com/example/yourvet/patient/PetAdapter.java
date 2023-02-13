package com.example.yourvet.patient;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.yourvet.R;
import com.example.yourvet.model.Pet;
import com.example.yourvet.model.Request;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PetAdapter extends BaseAdapter {

    private ArrayList<Pet> list = new ArrayList<Pet>();
    private Context context;
    private ImageView imageView;
    private TextView name, rasa,sex;
    private LinearLayout linearLayout;
    public PetAdapter(ArrayList<Pet> list, Context context) {
        super();
        this.list = list;
        this.context = context;
    }

    public PetAdapter() {
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
        view=inflater.inflate(R.layout.pet_layout,viewGroup,false);
        linearLayout=view.findViewById(R.id.choose_pet);
        imageView=view.findViewById(R.id.pet_photo);
        name=view.findViewById(R.id.name_pet);
        sex=view.findViewById(R.id.sex);
        rasa=view.findViewById(R.id.rasa);
        name.setText(list.get(i).getName());
        sex.setText(list.get(i).getSex());
        String img=  list.get(i).getPhotoUrl();
        Picasso.get().load(img).into(imageView);
        rasa.setText(list.get(i).getBreed());
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return view;
    }
}
