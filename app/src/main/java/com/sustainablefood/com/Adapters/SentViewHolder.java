package com.sustainablefood.com.Adapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.sustainablefood.com.Objects.Notification;
import com.sustainablefood.com.R;

public class SentViewHolder extends NotificationViewHolder {


    View notificationView;
    TextView nameTv;
    TextView phoneTv;
    ImageView foodImage;
    ImageView statusImage;

    public SentViewHolder(View mesajView){

        super(mesajView);

        this.notificationView = mesajView;
        this.nameTv = notificationView.findViewById(R.id.sent_item_username);
        this.phoneTv = notificationView.findViewById(R.id.sent_item_phone);
        this.foodImage = notificationView.findViewById(R.id.sent_item_image);
        this.statusImage = notificationView.findViewById(R.id.sent_item_status);

    }



    @Override
    public void setDetails(final Context mContext, final Notification notification){
        nameTv.setText(notification.name);
        phoneTv.setText(notification.phone);
        Picasso.with(mContext).load(notification.image).into(foodImage);
        if(notification.status == 1){
            statusImage.setImageResource(R.drawable.ic_waiting);
        }
        else if(notification.status == 2){
            statusImage.setImageResource(R.drawable.ic_approved);
        }
        else{
            statusImage.setImageResource(R.drawable.ic_declined);
        }

    }


}
