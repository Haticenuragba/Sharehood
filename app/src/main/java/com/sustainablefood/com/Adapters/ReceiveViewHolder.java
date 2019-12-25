package com.sustainablefood.com.Adapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.sustainablefood.com.Objects.Notification;
import com.sustainablefood.com.R;

import org.w3c.dom.Text;

public class ReceiveViewHolder extends NotificationViewHolder {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference userReference = firebaseDatabase.getReference("User").child(firebaseAuth.getCurrentUser().getUid().toString());


    View notificationView;
    TextView nameTv;
    TextView phoneTv;
    ImageView foodImage;

    ImageView approveButton;
    ImageView declineButton;

    public ReceiveViewHolder(View notificationView){

        super(notificationView);

        this.notificationView = notificationView;
        this.nameTv = notificationView.findViewById(R.id.received_item_username);
        this.phoneTv = notificationView.findViewById(R.id.received_item_phone);
        this.foodImage = notificationView.findViewById(R.id.received_item_image);
        this.approveButton = notificationView.findViewById(R.id.received_item_approve);
        this.declineButton = notificationView.findViewById(R.id.received_item_decline);

    }



    @Override
    public void setDetails(final Context mContext, final Notification notification){

    nameTv.setText(notification.name);
    phoneTv.setText(notification.phone);
    Picasso.with(mContext).load(notification.image).into(foodImage);
    approveButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            userReference.child("Notification").child(notification.id).removeValue();
            userReference.child("Location").child(notification.locationId).removeValue();
            firebaseDatabase.getReference("User").child(notification.guestId).child("Notification").child(notification.id).child("status").setValue(2);
            firebaseDatabase.getReference("Location").child(notification.locationId).removeValue();
        }
    });

    declineButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            userReference.child("Notification").child(notification.id).removeValue();
            firebaseDatabase.getReference("User").child(notification.guestId).child("Notification").child(notification.id).child("status").setValue(3);

        }
    });


    }


}