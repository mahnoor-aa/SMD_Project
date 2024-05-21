package com.example.firebase_lawyer;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Gigs extends Fragment implements GigAdap.OnEditClickListener{
    private RecyclerView recyclerView;
    private GigAdap gigAdapter;
    private ArrayList<GigClass> gigList;
    private String lawyerId;
    private FloatingActionButton fabAdd;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gigs, container, false);

        recyclerView = view.findViewById(R.id.rvGigsDisplay);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        gigList = new ArrayList<>();
        GigAdap.OnPeopleClickListener onPeopleClickListener = new GigAdap.OnPeopleClickListener() {
            @Override
            public void onPeopleClick(String gigId) {
                Intent intent = new Intent(getContext(), SubscribedUsers.class);
                intent.putExtra("GIG_ID", gigId);
                startActivity(intent);
            }
        };

        gigAdapter = new GigAdap(getActivity(), gigList, this, onPeopleClickListener);

        recyclerView.setAdapter(gigAdapter);
        fabAdd = view.findViewById(R.id.fabAdd);
        loadGigs();
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddGig.class);
                intent.putExtra("LAWYER_ID", lawyerId);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onEditClick(String gigId) {
        Intent intent = new Intent(getContext(), EditGig.class);
        intent.putExtra("GIG_ID", gigId);
        startActivity(intent);
    }


    private void loadGigs() {
        DatabaseReference gigsRef = FirebaseDatabase.getInstance().getReference().child("Gigs");
        gigsRef.orderByChild("lawyerid").equalTo(lawyerId).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                gigList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    GigClass gig = snapshot1.getValue(GigClass.class);
                    gigList.add(gig);
                }
                gigAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void onResume() {
        super.onResume();
        loadGigs();
    }

    public void setLawyerId(String lawyerId) {
        this.lawyerId = lawyerId;
    }
}