package com.example.firebase_lawyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LogIn extends AppCompatActivity {

    EditText username, password;
    Button logIn, signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        init();

        signUp.setOnClickListener(v -> {
            Intent intent = new Intent(LogIn.this, SignUp.class);
            startActivity(intent);
            finish();
        });

        logIn.setOnClickListener(v -> {
            String username1 = username.getText().toString().trim();
            String password1 = password.getText().toString().trim();

            if (username1.isEmpty() || password1.isEmpty()) {
                Toast.makeText(LogIn.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            authenticateUser(username1, password1);
        });
    }

    private void init() {
        username = findViewById(R.id.usernameEditText);
        password = findViewById(R.id.passwordEditText);
        signUp = findViewById(R.id.signUpButton);
        logIn = findViewById(R.id.loginButton);
    }

    private void authenticateUser(String username, String password) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("Lawyers");
        usersRef.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        String lawyerId = userSnapshot.child("id").getValue(String.class);
                        String storedPassword = userSnapshot.child("password").getValue(String.class);
                        if (storedPassword != null && storedPassword.equals(password)) {
                            checkApproved(lawyerId);
                            return;
                        }
                    }
                    Toast.makeText(LogIn.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(LogIn.this, "User does not exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LogIn.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkApproved(String lawyerId) {
        DatabaseReference approvedRef = FirebaseDatabase.getInstance().getReference().child("Approved Lawyers");
        approvedRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                 boolean approved = false;
                for (DataSnapshot snapshot2 : snapshot.getChildren()) {
                    String value = snapshot2.getValue(String.class);
                     if (value != null && value.equals(lawyerId)) {
                        approved = true;
                        break;
                    }
                }
                if (approved) {
                    Intent intent = new Intent(LogIn.this, HomePage.class);
                    intent.putExtra("LAWYER_ID", lawyerId);
                    startActivity(intent);
                    finish();
                } else {
                    checkDisapproved(lawyerId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LogIn.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkDisapproved(String lawyerId) {
        DatabaseReference disapprovedRef = FirebaseDatabase.getInstance().getReference().child("Disapproved Lawyers");
        disapprovedRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot disapprovedSnapshot) {
                boolean disapproved = false;
                for (DataSnapshot snapshot : disapprovedSnapshot.getChildren()) {
                    String value = snapshot.getValue(String.class);
                    if (value != null && value.equals(lawyerId)) {
                        disapproved = true;
                        break;
                    }
                }

                if (disapproved) {
                    Toast.makeText(LogIn.this, "Your account is not approved by the admin.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LogIn.this, "Your account is not approved. Please wait for approval.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LogIn.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
