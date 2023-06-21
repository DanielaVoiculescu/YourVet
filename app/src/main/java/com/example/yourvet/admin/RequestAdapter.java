package com.example.yourvet.admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yourvet.R;
import com.example.yourvet.model.Doctor;
import com.example.yourvet.model.Request;
import com.example.yourvet.model.Specialization;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {
    private Context context;
    private List<Request> requests;
    private String specializationName;
    private ArrayAdapter adapterItem;
    private StorageReference storage=FirebaseStorage.getInstance().getReference("userProfile");;
    private ArrayList<String> specializations=new ArrayList<>();
    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://yourvet-fdaf2-default-rtdb.firebaseio.com/");
    String imagine;
    public RequestAdapter(List<Request> requests,Context context) {
        this.context = context;
        this.requests = requests;
    }

    @NonNull
    @Override
    public RequestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.request_layout,parent,false);

        return new RequestAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestAdapter.ViewHolder holder, int position) {
        Request request = requests.get(position);
        int i= position;

        holder.name.setText(requests.get(position).getLastname()+" "+requests.get(position).getFirstname());
        holder.Id.setText(requests.get(position).getDoctorID());
        databaseReference.child("specialization").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                specializations.clear();
                for(DataSnapshot dataSnapshot1:snapshot.getChildren()){
                    Specialization specialization=dataSnapshot1.getValue(Specialization.class);

                    specializations.add(specialization.getName());
                    adapterItem.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        String doctor_id=requests.get(i).getDoctorID();
        adapterItem= new ArrayAdapter(context,R.layout.list_breed,specializations);
        holder.autoCompleteTextView.setAdapter(adapterItem);
        holder.autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                specializationName = adapterView.getItemAtPosition(i).toString();
            }
        });
        databaseReference.child("users").child("patients").child(request.getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                imagine=  snapshot.child("photoUrl").getValue().toString();
                Picasso.get()
                        .load(imagine)
                        .into(holder.imageView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                databaseReference.child("roles").child(request.getUserId()).setValue("doctor");
                databaseReference.child("users").child("patients").child(request.getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Doctor d= snapshot.getValue(Doctor.class);
                        d.setSpecialization(specializationName);
                        d.setDescription("Fara descriere");
                        databaseReference.child("requests").child(request.getUserId()).removeValue();
                        databaseReference.child("users").child("doctors").child(request.getUserId()).setValue(d);
                        databaseReference.child("users").child("patients").child(request.getUserId()).removeValue();
                        requests.remove(i);
                        notifyItemRemoved(i);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
        holder.btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child("requests").child(doctor_id).removeValue();
                requests.remove(i);
                notifyItemRemoved(i);
            }
        });


    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        Button btnAccept;
        Button btnReject;

        ImageView imageView;
        TextView name;
        TextView Id;
        AutoCompleteTextView autoCompleteTextView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.request_name);
            Id=itemView.findViewById(R.id.request_id);
            imageView=itemView.findViewById(R.id.image_request);
            btnReject=itemView.findViewById(R.id.reject_button);
            btnAccept=itemView.findViewById(R.id.accept_button);
            autoCompleteTextView=itemView.findViewById(R.id.set_specialization);
        }
    }

}
