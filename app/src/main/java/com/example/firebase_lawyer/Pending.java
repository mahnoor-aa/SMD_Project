package com.example.firebase_lawyer;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Pending extends Fragment {

    private RecyclerView recyclerView1;
    private PendingAdap pendingAdapter;
    private ArrayList<PendingGig> pendingList;
    private String lawyerId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=  inflater.inflate(R.layout.fragment_pending, container, false);
        recyclerView1 = view.findViewById(R.id.rvPendingDisplay);
        recyclerView1.setLayoutManager(new LinearLayoutManager(getActivity()));
        pendingList = new ArrayList<>();
        pendingAdapter=new PendingAdap(getActivity(), pendingList, this);
        recyclerView1.setAdapter(pendingAdapter);
        loadPending();

        return view;
    }
    private void loadPending() {
        DatabaseReference requestedGigs = FirebaseDatabase.getInstance().getReference().child("Requested Gigs");
        requestedGigs.orderByChild("lawyerid").equalTo(lawyerId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pendingList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    PendingGig gig = snapshot1.getValue(PendingGig.class);
                    pendingList.add(gig);
                }
                pendingAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    public void setLawyerId(String lawyerId) {
        this.lawyerId = lawyerId;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadPending();
    }

    public void deleteGig(PendingGig gig) {
        DatabaseReference requestedGigs = FirebaseDatabase.getInstance().getReference().child("Requested Gigs");
        requestedGigs.child(gig.getRequestedgigid()).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error != null) {
                    Toast.makeText(getActivity(), "Failed to delete gig", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Gig deleted successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void approveGig(PendingGig gig){

        String approvedGigId = FirebaseDatabase.getInstance().getReference().child("Approved Gigs").push().getKey();

        Subscribers approvedGig = new Subscribers();
        approvedGig.setGigid(gig.getGigId());
        approvedGig.setHearing(0);
        approvedGig.setId(approvedGigId);
        approvedGig.setLawyerid(gig.getLawyerId());
        approvedGig.setPrice("0");
        approvedGig.setUsername(gig.getUsername());
        approvedGig.setCasetitle(gig.getCasetitle());


        DatabaseReference approvedGigsRef = FirebaseDatabase.getInstance().getReference().child("Approved Gigs").child(approvedGigId);
        approvedGigsRef.setValue(approvedGig);

        DatabaseReference pendingGigsRef = FirebaseDatabase.getInstance().getReference().child("Requested Gigs").child(gig.getRequestedgigid());
        pendingGigsRef.removeValue();
    }

}