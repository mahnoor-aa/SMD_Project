package com.example.firebase_lawyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddGig extends AppCompatActivity {
    Button back;
    Button add;
    EditText etTitle, etdesc,etprice,etIban;
    Spinner CitySpinner;
    String lawyerId;
    DatabaseReference database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance().getReference();
        setContentView(R.layout.activity_add_gig);
        lawyerId = getIntent().getStringExtra("LAWYER_ID");

        if (lawyerId != null) {
            Toast.makeText(this, "ID " + lawyerId, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error: Lawyer ID not received", Toast.LENGTH_SHORT).show();
        }

        back=findViewById(R.id.BackButton);
        add=findViewById(R.id.addButton);
        etTitle=findViewById(R.id.titleEditText);
        etdesc=findViewById(R.id.descEditText);
        etprice=findViewById(R.id.priceEditText);
        etIban=findViewById(R.id.ibanEditText);
        CitySpinner=findViewById(R.id.citySpinner);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(AddGig.this);
                builder.setTitle("Confirm");
                builder.setMessage("Are you sure you want to discard changes?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                builder.setNegativeButton("No", null);
                builder.show();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(AddGig.this);
                builder.setTitle("Confirm");
                builder.setMessage("Are you sure you want to add this gig?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addGig();
                    }
                });
                builder.setNegativeButton("No", null);
                builder.show();
            }
        });

    }

    private void addGig() {
        String title = etTitle.getText().toString().trim();
        String desc = etdesc.getText().toString().trim();
        String priceStr =etprice.getText().toString().trim();
        String iban = etIban.getText().toString().trim();

        if (title.isEmpty() || desc.isEmpty() || priceStr.isEmpty() || iban.isEmpty() || CitySpinner.getSelectedItemPosition()==0) {
            Toast.makeText(AddGig.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int price = Integer.parseInt(priceStr);
        if (price <= 0) {
            Toast.makeText(AddGig.this, "Price should be a positive value", Toast.LENGTH_SHORT).show();
            return;
        }
        if (iban.length() != 24) {
            Toast.makeText(AddGig.this, "IBAN number should be 24 characters long", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference lawyerRef = FirebaseDatabase.getInstance().getReference().child("Lawyers").child(lawyerId);
        lawyerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Lawyer found, retrieve the details
                    String name = snapshot.child("name").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);
                    String phoneNo = snapshot.child("phoneNo").getValue(String.class);
                    String lawyerType = snapshot.child("lawyerType").getValue(String.class);
                    String courtType = snapshot.child("courtType").getValue(String.class);
                    String highestLicense = snapshot.child("highestLicense").getValue(String.class);

                    String id = database.child("Gigs").push().getKey();
                    GigClass gig = new GigClass(id,
                            etTitle.getText().toString(),
                            etdesc.getText().toString(),
                            Integer.parseInt(etprice.getText().toString()),
                            etIban.getText().toString(),
                            lawyerId,
                            name,
                            email,
                            phoneNo,
                            lawyerType,
                            courtType,
                            highestLicense,
                            CitySpinner.getSelectedItem().toString()
                    );

                    database.child("Gigs").child(id).setValue(gig, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if (databaseError != null) {
                                Toast.makeText(AddGig.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(AddGig.this, "Gig information saved successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(AddGig.this, "Error: Lawyer not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AddGig.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

}


