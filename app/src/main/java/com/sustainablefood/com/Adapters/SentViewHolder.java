package com.sustainablefood.com.Adapters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;
import com.sustainablefood.com.Objects.Notification;

public class SentViewHolder extends NotificationViewHolder {


    View notificationView;


    public SentViewHolder(View mesajView){

        super(mesajView);

        this.notificationView = mesajView;



    }



    @Override
    public void setDetails(final Context mContext, final Notification notification){




    }


}
