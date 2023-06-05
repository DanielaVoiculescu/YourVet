package com.example.yourvet.doctor;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yourvet.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PhotoRecyclerAdapterUrl extends RecyclerView.Adapter<PhotoRecyclerAdapterUrl.ViewHolder> {
    private ArrayList<String> uriArrayList;

    public PhotoRecyclerAdapterUrl(ArrayList<String> uriArrayList) {
        this.uriArrayList = uriArrayList;
    }

    @NonNull
    @Override
    public PhotoRecyclerAdapterUrl.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.single_photo,parent,false);



        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoRecyclerAdapterUrl.ViewHolder holder, int position) {
        String img=  uriArrayList.get(position);
        Picasso.get().load(img).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return uriArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.image);

        }
    }
}
