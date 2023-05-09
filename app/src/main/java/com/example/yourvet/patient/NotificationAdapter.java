package com.example.yourvet.patient;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yourvet.Message.MessageAdapter;
import com.example.yourvet.Message.UserAdapter;
import com.example.yourvet.R;
import com.example.yourvet.model.Message;
import com.example.yourvet.model.Notification;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private Context context;
    private List<Notification> list;
    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://yourvet-fdaf2-default-rtdb.firebaseio.com/");

    public NotificationAdapter(Context context, List<Notification> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.notification_item,parent,false);

        return new NotificationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.ViewHolder holder, int position) {
        Notification notification=list.get(position);
        holder.title.setText(notification.getTitle());
        DateFormat dateFormat = new SimpleDateFormat("E MMM dd HH:mm:ss zzz yyyy");
        if(!notification.isSeen()){
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context,R.color.white));
        }

        Date date = null;
        try {
            date = dateFormat.parse(list.get(position).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }


        DateFormat dateFormatDate = new SimpleDateFormat("dd/MM/yyyy");

        DateFormat dateFormatTime = new SimpleDateFormat("HH:mm");

        String dateStr = dateFormatDate.format(date);
        String timeStr = dateFormatTime.format(date);
        holder.time.setText(dateStr+" "+timeStr);
        holder.seen_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child("notifications").child(mAuth.getCurrentUser().getUid()).child(notification.getId()).child("seen").setValue(true);

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView time;
        private Button seen_button;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.notification_title);
            time=itemView.findViewById(R.id.notification_time);
            seen_button=itemView.findViewById(R.id.seen_button);
        }
    }
}
