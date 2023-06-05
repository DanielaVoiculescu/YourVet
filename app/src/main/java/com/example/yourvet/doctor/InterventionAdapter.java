package com.example.yourvet.doctor;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yourvet.R;
import com.example.yourvet.model.Doctor;
import com.example.yourvet.model.Intervention;
import com.example.yourvet.model.Pet;
import com.example.yourvet.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class InterventionAdapter extends RecyclerView.Adapter<InterventionAdapter.ViewHolder> {
    private AlertDialog.Builder dialogBuilder;
    private Context context;
    private List<Intervention> list;
    private DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://yourvet-fdaf2-default-rtdb.firebaseio.com/");
    private AlertDialog dialog;
    public InterventionAdapter(Context context, List<Intervention> list) {
        this.context = context;
        this.list = list;
    }

    public InterventionAdapter() {
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.intervention_item,parent,false);

        return new InterventionAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Intervention i=list.get(position);
        String date=i.getDate().toString();
        holder.date.setText(date);
        databaseReference.child("users").child("doctors").child(i.getDoctorId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Doctor u=snapshot.getValue(Doctor.class);
                holder.doctor_name.setText(u.getFirstname()+" "+u.getLastname());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDetalidedIntervention(i.getId());
            }
        });
    }

    private void openDetalidedIntervention(String id) {
        TextView pet_name,owner_name,doctor,data, simptome,diagnostic, interventie, recomandari;
        RecyclerView recyclerView;
        ArrayList<String> uri=new ArrayList<>();
        PhotoRecyclerAdapterUrl photoRecyclerAdapter;
        dialogBuilder = new AlertDialog.Builder(context);
        final View detailedintrevention = LayoutInflater.from(context).inflate(R.layout.fragment_detailed_intervention, null);
        pet_name=detailedintrevention.findViewById(R.id.pet_name);
        owner_name=detailedintrevention.findViewById(R.id.pet_owner);
        doctor=detailedintrevention.findViewById(R.id.doctor_name);
        data=detailedintrevention.findViewById(R.id.date);
        simptome=detailedintrevention.findViewById(R.id.symptoms);
        diagnostic=detailedintrevention.findViewById(R.id.diagnostic);
        interventie=detailedintrevention.findViewById(R.id.interventie);
        recomandari=detailedintrevention.findViewById(R.id.prescriptii);
        recyclerView=detailedintrevention.findViewById(R.id.poze);
        photoRecyclerAdapter=new PhotoRecyclerAdapterUrl(uri);
        recyclerView.setLayoutManager(new GridLayoutManager(context,4));
        recyclerView.setAdapter(photoRecyclerAdapter);
        databaseReference.child("interventions").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Intervention in=snapshot.getValue(Intervention.class);
                data.setText(in.getDate().toString());
                simptome.setText(in.getSymptom());
                diagnostic.setText(in.getDiagnostic());
                interventie.setText(in.getIntervention());
                recomandari.setText(in.getPrescription());
                databaseReference.child("interventions").child(id).child("photos").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot d: snapshot.getChildren()){
                            String url= d.getValue(String.class);
                            uri.add(url);
                            photoRecyclerAdapter.notifyDataSetChanged();
                            //Log.d("url",url);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                databaseReference.child("pets").child(in.getPetId()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Pet p=snapshot.getValue(Pet.class);
                        pet_name.setText(p.getName());
                        databaseReference.child("users").child("patients").child(p.getOwnerId()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                User patient=snapshot.getValue(User.class);
                                owner_name.setText(patient.getFirstname()+" "+patient.getLastname());
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                databaseReference.child("users").child("doctors").child(in.getDoctorId()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Doctor d=snapshot.getValue(Doctor.class);
                        doctor.setText(d.getFirstname()+" "+d.getLastname());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        dialogBuilder.setView(detailedintrevention);
        dialog = dialogBuilder.create();
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView date,doctor_name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date=itemView.findViewById(R.id.date);
            doctor_name=itemView.findViewById(R.id.doctor);
        }
    }
}
