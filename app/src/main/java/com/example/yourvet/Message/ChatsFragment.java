package com.example.yourvet.Message;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yourvet.R;
import com.example.yourvet.model.Message;
import com.example.yourvet.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatsFragment extends Fragment {
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> userList;
    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://yourvet-fdaf2-default-rtdb.firebaseio.com/");

    private List<String> usersList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_chats,container,false);
        recyclerView=view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        usersList=new ArrayList<>();
        databaseReference.child("chats").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersList.clear();
                for (DataSnapshot d:snapshot.getChildren()){
                    Message m= d.getValue(Message.class);
                    if(m.getSenderId().equals(mAuth.getCurrentUser().getUid())){
                        usersList.add(m.getReciverId());
                    }
                    if(m.getReciverId().equals(mAuth.getCurrentUser().getUid())){
                        usersList.add(m.getSenderId());
                    }
                }
                readChats();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }
    private void readChats() {
        userList = new ArrayList<>();
        userAdapter = new UserAdapter(getContext(), userList);
        recyclerView.setAdapter(userAdapter);

        databaseReference.child("roles").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String path;
                if (snapshot.getValue(String.class).equals("doctor")) {
                    path = "patients";
                } else {
                    path = "doctors";
                }

                databaseReference.child("users").child(path).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<User> usersToAdd = new ArrayList<>(); // Create a separate list

                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            User u = snapshot1.getValue(User.class);
                            for (String id : usersList) {
                                if (u.getId().equals(id)) {
                                    // Add user to the separate list
                                    usersToAdd.add(u);
                                    break; // Break the loop after finding a match
                                }
                            }
                        }

                        // Add the users from the separate list to userList
                        userList.addAll(usersToAdd);
                        userAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle onCancelled
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });
    }
}
