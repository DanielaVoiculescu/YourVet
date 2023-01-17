package com.example.yourvet.admin;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.yourvet.R;
import com.example.yourvet.model.Doctor;
import com.example.yourvet.model.Request;
import com.example.yourvet.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RequestAdapter extends BaseAdapter {
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://yourvet-fdaf2-default-rtdb.firebaseio.com/");
    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    Button btnAccept;
    Button btnReject;
    ImageView img;
    TextView name;
    TextView Id;
    private User user;
    private Doctor doctor;
    private int layoutResourceId;
    private ArrayList<Request> list = new ArrayList<Request>();
    private Context context;

    public RequestAdapter(ArrayList<Request> list, Context context) {
        super();
        this.list = list;
        this.context = context;
    }

    public RequestAdapter() {
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
        view=inflater.inflate(R.layout.request_layout,viewGroup,false);
        name=view.findViewById(R.id.request_name);
        Id=view.findViewById(R.id.request_id);
        name.setText(list.get(i).getLastname()+" "+list.get(i).getFirstname());
        Id.setText(list.get(i).getDoctorID());
        btnReject=view.findViewById(R.id.reject_button);
        btnAccept=view.findViewById(R.id.accept_button);


        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                databaseReference.child("users").child(list.get(i).getUserId()).child("role").setValue("doctor");
                databaseReference.child("users").child(list.get(i).getUserId()).child("id").setValue(list.get(i).getDoctorID());
                databaseReference.child("requests").child(list.get(i).getDoctorID()).removeValue();
            }
        });
        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child("requests").child(list.get(i).getDoctorID()).removeValue();

            }
        });
        return view;
    }
}
