package com.example.yourvet.Message;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yourvet.R;
import com.example.yourvet.doctor.Profile;
import com.example.yourvet.model.Message;
import com.example.yourvet.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private Context context;
    private List<User> userList;
    String theLastMessage;
    public UserAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.user_item,parent,false);

        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user=userList.get(position);
        holder.username.setText(user.getFirstname()+" "+user.getLastname());
        String img=  userList.get(position).getPhotoUrl();
        Picasso.get().load(img).into(holder.imageView);
        last_messager(user.getId(),holder.last_msg);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPref = context.getSharedPreferences("reciver", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("reciverId", user.getId());
                editor.apply();
                FragmentActivity fragmentActivity=(FragmentActivity) context;
                fragmentActivity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new PrivateChat()).commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView username;
        public ImageView imageView;
        private TextView last_msg;
        public ViewHolder(View view) {
            super(view);
            username = view.findViewById(R.id.username);
            imageView = view.findViewById(R.id.profile_image);
            last_msg=view.findViewById(R.id.last_message);
        }
    }
    private void last_messager(String userId, TextView last_messsage){
        theLastMessage="default";
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://yourvet-fdaf2-default-rtdb.firebaseio.com/");
        databaseReference.child("chats").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Message m= dataSnapshot.getValue(Message.class);
                        if(m.getReciverId().equals(firebaseUser.getUid())&& m.getSenderId().equals(userId)||m.getReciverId().equals(userId)&& m.getSenderId().equals(firebaseUser.getUid())){
                            theLastMessage=m.getText();
                        }
                }
                switch (theLastMessage){
                    case "default":
                        last_messsage.setText("No Message");
                        break;
                    default:
                        last_messsage.setText(theLastMessage);
                        break;
                }
                theLastMessage="default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
