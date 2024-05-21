package com.example.firebase_lawyer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.database.FirebaseDatabase;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;


public class SignUp extends AppCompatActivity {

    EditText nameEditText, emailEditText, phoneEditText, usernameEditText, passwordEditText, confirmPasswordEditText;
    Spinner lawyerTypeSpinner, courtTypeSpinner, highestLicenseSpinner;
    Button selectPhotoButton, signupButton;
    TextView errorFields, errorPassword,errorEmail;
    private static final int IMAGE_PICK_CODE = 1000;
    private Uri imageUri;
    DatabaseReference database;
    FirebaseStorage storage = FirebaseStorage.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        init();
        database = FirebaseDatabase.getInstance().getReference();

        selectPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearErrorMessages();
                if (checkFields()) {
                    if (isValidEmail()) {
                        if (passwordMatch()) {
                            String lawyerId = insertLawyerInfo();
                            Toast.makeText(SignUp.this, "Your account has been sent for approval", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(SignUp.this, LogIn.class);
                            intent.putExtra("LAWYER_ID", lawyerId);
                            startActivity(intent);
                            finish();
                        } else {
                            showErrorPassword("Passwords do not match");
                        }
                    }else{showErrorEmail("Invalid email format");}
                } else {
                    showErrorFields("Fill all the fields");
                }
            }
        });
    }

    private String insertLawyerInfo() {
        String id = database.child("Lawyers").push().getKey();
        Lawyer lawyer = new Lawyer(id,
                nameEditText.getText().toString(),
                emailEditText.getText().toString(),
                phoneEditText.getText().toString(),
                "",
                usernameEditText.getText().toString(),
                passwordEditText.getText().toString(),
                lawyerTypeSpinner.getSelectedItem().toString(),
                courtTypeSpinner.getSelectedItem().toString(),
                highestLicenseSpinner.getSelectedItem().toString());

        assert id != null;

        if (imageUri != null) {
            StorageReference storageRef = storage.getReference().child(id);
            storageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            String imageUrl = uri.toString();
                            lawyer.setPicture(imageUrl);
                            database.child("Lawyers").child(id).setValue(lawyer);
                            Toast.makeText(this, "Lawyer information saved successfully", Toast.LENGTH_SHORT).show();
                        });
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });

        } else {
            database.child("Lawyers").child(id).setValue(lawyer);
            Toast.makeText(this, "Lawyer information saved successfully", Toast.LENGTH_SHORT).show();
        }

        return id;
    }

    private boolean checkFields() {
        return !TextUtils.isEmpty(nameEditText.getText()) &&
                !TextUtils.isEmpty(emailEditText.getText()) &&
                !TextUtils.isEmpty(phoneEditText.getText()) &&
                !TextUtils.isEmpty(usernameEditText.getText()) &&
                !TextUtils.isEmpty(passwordEditText.getText()) &&
                !TextUtils.isEmpty(confirmPasswordEditText.getText()) &&
                lawyerTypeSpinner.getSelectedItemPosition() != 0 &&
                courtTypeSpinner.getSelectedItemPosition() != 0 &&
                highestLicenseSpinner.getSelectedItemPosition() != 0 &&
                isImageUploaded();
    }

    private boolean isValidEmail() {
        String emailPattern = "[a-zA-Z0-9._%+-]+@[a-zA-Z]+\\.com";
        return Pattern.matches(emailPattern, emailEditText.getText());
    }


    private boolean passwordMatch() {
        return passwordEditText.getText().toString().equals(confirmPasswordEditText.getText().toString());
    }

    private void clearErrorMessages() {
        errorFields.setText("");
        errorFields.setVisibility(View.GONE);
        errorPassword.setText("");
        errorPassword.setVisibility(View.GONE);
    }

    private void showErrorFields(String error) {
        errorFields.setText(error);
        errorFields.setVisibility(View.VISIBLE);
    }

    private void showErrorPassword(String error) {
        errorPassword.setText(error);
        errorPassword.setVisibility(View.VISIBLE);
    }

    private void showErrorEmail(String error) {
        errorEmail.setText(error);
        errorEmail.setVisibility(View.VISIBLE);
    }

    public void init() {
        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        usernameEditText = findViewById(R.id.usernameEditTextSignUp);
        passwordEditText = findViewById(R.id.passwordEditTextSignUp);
        confirmPasswordEditText = findViewById(R.id.confirmpasswordEditText);
        lawyerTypeSpinner = findViewById(R.id.lawyerTypeSpinner);
        courtTypeSpinner = findViewById(R.id.courtTypeSpinner);
        highestLicenseSpinner = findViewById(R.id.highestLicenseSpinner);
        selectPhotoButton = findViewById(R.id.selectPhotoButton);
        signupButton = findViewById(R.id.signupButton);
        errorFields = findViewById(R.id.errorFields);
        errorPassword = findViewById(R.id.errorPassword);
        errorEmail=findViewById(R.id.errorEmail);
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK);
        gallery.setType("image/*");
        startActivityForResult(gallery, IMAGE_PICK_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_PICK_CODE && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            Toast.makeText(this, "Image selected", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isImageUploaded() {
        return imageUri != null;
    }

}
