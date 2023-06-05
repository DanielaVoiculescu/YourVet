package com.example.yourvet.patient;

import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;

import androidx.annotation.NonNull;

import com.example.yourvet.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RateInterventionDialog extends Dialog {
    public RateInterventionDialog(@NonNull Context context) {
        super(context);
    }
    Button button_send_rate,close;
    RatingBar ratingBar;
    ImageView imageView;
    private DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://yourvet-fdaf2-default-rtdb.firebaseio.com/");

    private float rate;
    private String interventionId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rate_intervention_dialog_layout);
        button_send_rate=findViewById(R.id.add_rate);
        ratingBar=findViewById(R.id.ratingBar);
        imageView=findViewById(R.id.emoji);

        SharedPreferences sharedPreferences =getContext().getSharedPreferences("intervention", MODE_PRIVATE);
        interventionId = sharedPreferences.getString("interventionId","");
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                if (v<=1){
                    imageView.setImageResource(R.drawable.onestar);

                }
                else if (v<=2){
                    imageView.setImageResource(R.drawable.twostars);
                }
                else if (v<=3){
                    imageView.setImageResource(R.drawable.threestars);
                }
                else if (v<=4){
                    imageView.setImageResource(R.drawable.fourstars);
                }
                else {
                    imageView.setImageResource(R.drawable.fivestars);

                }
                animateImage(imageView);
                rate=v;
            }
        });

        button_send_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child("interventions").child(interventionId).child("rate").setValue(ratingBar.getRating());
                dismiss();
            }
        });


    }
    private void animateImage(ImageView imageView){
        ScaleAnimation scaleAnimation=new ScaleAnimation(0,1f,0,1f, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        scaleAnimation.setFillAfter(true);
        scaleAnimation.setDuration(200);
        imageView.startAnimation(scaleAnimation);
    }
}
