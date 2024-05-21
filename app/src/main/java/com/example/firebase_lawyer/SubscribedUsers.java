package com.example.firebase_lawyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SubscribedUsers extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SubscribersAdap subscribersAdapter;
    private ArrayList<Subscribers> subscribersList;
    private String gigId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribed_users);

        gigId = getIntent().getStringExtra("GIG_ID");

        recyclerView = findViewById(R.id.rvSubsDisplay);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        subscribersList = new ArrayList<>();

        subscribersAdapter = new SubscribersAdap(this, subscribersList, new SubscribersAdap.OnRemoveClickListener() {
            @Override
            public void onRemoveClick(Subscribers subscriber) {
                DatabaseReference subscribersRef = FirebaseDatabase.getInstance().getReference().child("Approved Gigs");
                subscribersRef.child(subscriber.getId()).removeValue();

                int index = subscribersList.indexOf(subscriber);
                if (index != -1) {
                    subscribersList.remove(index);
                    subscribersAdapter.notifyItemRemoved(index);
                }
            }
        });

        recyclerView.setAdapter(subscribersAdapter);

        loadSubscribers();


    }

    private void loadSubscribers() {
        DatabaseReference subscribersRef = FirebaseDatabase.getInstance().getReference().child("Approved Gigs");
        subscribersRef.orderByChild("gigid").equalTo(gigId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                subscribersList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Subscribers subscriber = snapshot1.getValue(Subscribers.class);
                    subscribersList.add(subscriber);
                }
                subscribersAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}