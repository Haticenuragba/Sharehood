package com.sustainablefood.com.Adapters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;
import com.sustainablefood.com.Objects.Notification;

public class ReceiveViewHolder extends NotificationViewHolder {


    View notificationView;



    public ReceiveViewHolder(View notificationView){

        super(notificationView);

        this.notificationView = notificationView;




    }



    @Override
    public void setDetails(final Context mContext, final Notification notification){





    }


}