package com.sustainablefood.com.Adapters;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sustainablefood.com.Objects.Location;

public class LocationViewHolder extends RecyclerView.ViewHolder {


    View locationView;
    FirebaseDatabase mFirebaseDatabase;
    Location locationObject;
    boolean isFav;
    TextView locationTitle;
    ImageView locationImage;
    TextView locationPrice;
    ImageView favouritesImage;

    public LocationViewHolder(View locationView){

        super(locationView);

        this.locationView = locationView;

        mFirebaseDatabase = FirebaseDatabase.getInstance();



    }



    public void setDetails(final Context mContext, final Location location) {





        locationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


}