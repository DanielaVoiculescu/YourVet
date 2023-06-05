package com.example.yourvet.Message;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yourvet.R;
import com.example.yourvet.model.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PrivateChat extends Fragment {
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://yourvet-fdaf2-default-rtdb.firebaseio.com/");
    private Button back_button;
    private ImageView btn_send;
    private EditText message_text;
    private MessageAdapter messageAdapter;
    private List<Message> messageList;
    private RecyclerView recyclerView;
    private String reciverId;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_private_chat, container, false);
        btn_send=view.findViewById(R.id.btn_send_message);
        back_button= view.findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ChatsFragment()).commit();

            }
        });
        message_text=view.findViewById(R.id.message_text);
        recyclerView=view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext().getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        SharedPreferences sharedPreferences =getContext().getSharedPreferences("reciver", MODE_PRIVATE);
        reciverId = sharedPreferences.getString("reciverId","");
        readMessages(FirebaseAuth.getInstance().getCurrentUser().getUid(), reciverId);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send_message(reciverId);
               message_text.getText().clear();
            }
        });
        return view;
    }
    private void send_message(String reciverId){
        String message=message_text.getText().toString();
        String senderId=mAuth.getCurrentUser().getUid();
        HashMap<String , Object> hashMap=new HashMap<>();
        hashMap.put("senderId",senderId);
        hashMap.put("reciverId",reciverId);
        hashMap.put("text",message);
        databaseReference.child("chats").push().setValue(hashMap);
    }
    private void readMessages(String myid, String userId){
        messageList=new ArrayList<>();
        databaseReference.child("chats").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageList.clear();
                for(DataSnapshot d:snapshot.getChildren()){
                    Message message=d.getValue(Message.class);
                    System.out.println(message);
                    if(message.getReciverId().equals(myid)&&message.getSenderId().equals(userId)|| message.getReciverId().equals(userId)&& message.getSenderId().equals(myid)){
                        messageList.add(message);

                    }
                    messageAdapter=new MessageAdapter(getContext(),messageList);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
