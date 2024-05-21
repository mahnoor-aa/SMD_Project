package com.example.firebase_lawyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditGig extends AppCompatActivity {
    private EditText etTitle, etDesc, etPrice, etIban;
    private Spinner etCity;
    private Button btnSaveChanges,btnBack;
    private String gig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_gig);

        etTitle = findViewById(R.id.edittitleEditText);
        etDesc = findViewById(R.id.editdescEditText);
        etCity = findViewById(R.id.editcitySpinner);
        etPrice = findViewById(R.id.editpriceEditText);
        etIban = findViewById(R.id.editibanEditText);
        btnSaveChanges = findViewById(R.id.savechangesButton);
        btnBack=findViewById(R.id.backButton);

        gig = getIntent().getStringExtra("GIG_ID");

        if (gig != null) {
            DatabaseReference gigRef = FirebaseDatabase.getInstance().getReference().child("Gigs").child(gig);
            gigRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        GigClass gig = snapshot.getValue(GigClass.class);
                        if (gig != null) {
                            etTitle.setText(gig.getTitle());
                            etDesc.setText(gig.getDesc());
                            ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) etCity.getAdapter();
                            if (adapter != null) {
                                int position = adapter.getPosition(gig.getCity());
                                if (position != -1) {
                                    etCity.setSelection(position);
                                }
                            }
                            etPrice.setText(String.valueOf(gig.getPrice()));
                            etIban.setText(gig.getIBAN());
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }

        btnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSaveChangesDialog();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDiscardChangesDialog();
            }
        });


    }

    private void showSaveChangesDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to save changes?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        saveChanges();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog, do nothing
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDiscardChangesDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to discard changes?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog, do nothing
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void saveChanges() {
        String updatedTitle = etTitle.getText().toString().trim();
        String updatedDesc = etDesc.getText().toString().trim();
        String updatedCity = etCity.getSelectedItem().toString();
        String updatedPriceStr = etPrice.getText().toString().trim();
        String updatedIban = etIban.getText().toString().trim();

        if (updatedTitle.isEmpty() || updatedDesc.isEmpty() || updatedCity.isEmpty() || updatedPriceStr.isEmpty() || updatedIban.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int updatedPrice = Integer.parseInt(updatedPriceStr);
        if (updatedPrice <= 0) {
            Toast.makeText(this, "Price should be a positive value", Toast.LENGTH_SHORT).show();
            return;
        }
        if (updatedIban.length() != 24) {
            Toast.makeText(this, "IBAN number should be 24 characters long", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference gigRef = FirebaseDatabase.getInstance().getReference().child("Gigs").child(gig);
        gigRef.child("title").setValue(updatedTitle);
        gigRef.child("desc").setValue(updatedDesc);
        gigRef.child("city").setValue(updatedCity);
        gigRef.child("price").setValue(updatedPrice);
        gigRef.child("iban").setValue(updatedIban);

        Toast.makeText(this, "Gig information updated successfully", Toast.LENGTH_SHORT).show();

        DatabaseReference requestedGigRef = FirebaseDatabase.getInstance().getReference().child("Requested Gigs");
        requestedGigRef.orderByChild("gigid").equalTo(gig).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    dataSnapshot.getRef().child("gigtitle").setValue(updatedTitle);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });



        finish();
    }

}
