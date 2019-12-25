package com.sustainablefood.com;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sustainablefood.com.Adapters.NotificationViewHolder;
import com.sustainablefood.com.Adapters.ReceiveViewHolder;
import com.sustainablefood.com.Adapters.SentViewHolder;
import com.sustainablefood.com.Objects.Notification;

public class NotificationActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference userReference = firebaseDatabase.getReference("User").child(firebaseAuth.getCurrentUser().getUid().toString());

    private static final int TYPE_SENT = 101;
    private static final int TYPE_RECEIVE = 102;
    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter firebaseRecyclerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        recyclerView = findViewById(R.id.notification_recyclerview);
        firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Notification, NotificationViewHolder>(
                        Notification.class,
                        R.layout.notification_sent_item,
                        NotificationViewHolder.class,
                        userReference.child("Notification")
                ){
                    @Override
                    public NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                        switch (viewType) {
                            case TYPE_SENT:
                                //note: save an inflater when you construct the adapter
                                return new SentViewHolder(LayoutInflater.from(parent.getContext())
                                        .inflate(R.layout.notification_sent_item, parent, false));

                            case TYPE_RECEIVE:
                                return new ReceiveViewHolder(LayoutInflater.from(parent.getContext())
                                        .inflate(R.layout.notification_received_item, parent, false));

                        }
                        return super.onCreateViewHolder(parent, viewType);
                    }

                    @Override
                    protected void populateViewHolder (NotificationViewHolder notificationViewHolder, Notification notification, int position  ){

                        notificationViewHolder.setDetails(getApplicationContext(), notification);

                    }

                    @Override
                    public int getItemViewType(int position) {
                        //note: classes should start with uppercase
                        Notification model = getItem(position);
                        if (model.isReceived()) { //note: you'll have to define this method
                            return TYPE_RECEIVE;
                        } else {
                            return TYPE_SENT;
                        }
                    }

                };

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }
}
