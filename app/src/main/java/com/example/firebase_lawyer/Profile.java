package com.example.firebase_lawyer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.regex.Pattern;

public class Profile extends Fragment {
    private String lawyerId;
    private EditText nameEditText, emailEditText, phoneEditText, usernameEditText, passwordEditText, confirmPasswordEditText;
    private Spinner lawyerTypeSpinner, courtTypeSpinner, highestLicenseSpinner;
    private ImageView profileImageView;
    private Button selectPhoto, saveChanges, logout;
    private Uri imageUri;
    private static final int IMAGE_PICK_CODE = 1000;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_profile, container, false);

        nameEditText = view.findViewById(R.id.nameEditTextUpdate);
        emailEditText = view.findViewById(R.id.emailEditTextUpdate);
        phoneEditText = view.findViewById(R.id.phoneEditTextUpdate);
        usernameEditText = view.findViewById(R.id.usernameEditTextUpdate);
        passwordEditText = view.findViewById(R.id.passwordEditTextUpdate);
        confirmPasswordEditText = view.findViewById(R.id.confirmpasswordEditTextUpdate);
        lawyerTypeSpinner = view.findViewById(R.id.lawyerTypeSpinnerUpdate);
        courtTypeSpinner = view.findViewById(R.id.courtTypeSpinnerUpdate);
        highestLicenseSpinner = view.findViewById(R.id.highestLicenseSpinnerUpdate);
        profileImageView = view.findViewById(R.id.profileImageView);
        selectPhoto = view.findViewById(R.id.selectPhotoButtonUpdate);
        saveChanges = view.findViewById(R.id.savechangesProfile);
        logout=view.findViewById(R.id.LogoutButton);

        DatabaseReference lawyer = FirebaseDatabase.getInstance().getReference().child("Lawyers").child(lawyerId);
        lawyer.addListenerForSingleValueEvent(new ValueEventListener(){


            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Lawyer lawyer = snapshot.getValue(Lawyer.class);
                    if (lawyer != null) {
                        nameEditText.setText(lawyer.getName());
                        emailEditText.setText(lawyer.getEmail());
                        phoneEditText.setText(lawyer.getPhone());
                        usernameEditText.setText(lawyer.getUsername());
                        passwordEditText.setText(lawyer.getPassword());
                        confirmPasswordEditText.setText(lawyer.getPassword());

                        ArrayAdapter<String> lawyerTypeAdapter = (ArrayAdapter<String>) lawyerTypeSpinner.getAdapter();
                        int lawyerTypePosition = lawyerTypeAdapter.getPosition(lawyer.getLawyerType());
                        if (lawyerTypePosition != -1) {
                            lawyerTypeSpinner.setSelection(lawyerTypePosition);
                        }

                        ArrayAdapter<String> courtTypeAdapter = (ArrayAdapter<String>) courtTypeSpinner.getAdapter();
                        int courtTypePosition = courtTypeAdapter.getPosition(lawyer.getCourtType());
                        if (courtTypePosition != -1) {
                            courtTypeSpinner.setSelection(courtTypePosition);
                        }

                        ArrayAdapter<String> highestLicenseAdapter = (ArrayAdapter<String>) highestLicenseSpinner.getAdapter();
                        int highestLicensePosition = highestLicenseAdapter.getPosition(lawyer.getHighestLicense());
                        if (highestLicensePosition != -1) {
                            highestLicenseSpinner.setSelection(highestLicensePosition);
                        }

                        Picasso.get().load(lawyer.getPicture()).into(profileImageView);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        selectPhoto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, IMAGE_PICK_CODE);
            }
        });

        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSaveChangesDialog();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutDialog();
            }
        });

        return view;
    }

    private void showSaveChangesDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Are you sure you want to save changes?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveChanges();
            }
        });
        builder.setNegativeButton("No", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getActivity(), LogIn.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("No", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_PICK_CODE && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.getData();
        }
    }

    private void uploadImage(Uri imageUri) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child(lawyerId); // Use lawyer ID as the filename
        storageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        DatabaseReference lawyerRef = FirebaseDatabase.getInstance().getReference().child("Lawyers").child(lawyerId);
                        lawyerRef.child("picture").setValue(uri.toString());

                        Picasso.get().load(uri).into(profileImageView);
                    });
                })
                .addOnFailureListener(e -> {

                });
    }

    private void saveChanges() {
        String updatedName = nameEditText.getText().toString().trim();
        String updatedEmail = emailEditText.getText().toString().trim();
        String updatedPhone = phoneEditText.getText().toString().trim();
        String updatedUsername = usernameEditText.getText().toString().trim();
        String updatedPassword = passwordEditText.getText().toString().trim();
        String updatedConfirmPassword = confirmPasswordEditText.getText().toString().trim();
        String updatedLawyerType = lawyerTypeSpinner.getSelectedItem().toString();
        String updatedCourtType = courtTypeSpinner.getSelectedItem().toString();
        String updatedHighestLicense = highestLicenseSpinner.getSelectedItem().toString();

        if ( lawyerTypeSpinner.getSelectedItemPosition() == 0 || courtTypeSpinner.getSelectedItemPosition()==0 || highestLicenseSpinner.getSelectedItemPosition()==0)
        {
            Toast.makeText(getContext(), "Invalid Changes", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isValidEmail(updatedEmail)) {
            Toast.makeText(getContext(), "Invalid email format", Toast.LENGTH_SHORT).show();
            return;
        }


        if (!updatedPassword.equals(updatedConfirmPassword)) {
            Toast.makeText(getContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference lawyerRef = FirebaseDatabase.getInstance().getReference().child("Lawyers").child(lawyerId);
        lawyerRef.child("name").setValue(updatedName);
        lawyerRef.child("email").setValue(updatedEmail);
        lawyerRef.child("phone").setValue(updatedPhone);
        lawyerRef.child("username").setValue(updatedUsername);
        lawyerRef.child("password").setValue(updatedPassword);
        lawyerRef.child("lawyerType").setValue(updatedLawyerType);
        lawyerRef.child("courtType").setValue(updatedCourtType);
        lawyerRef.child("highestLicense").setValue(updatedHighestLicense);

        if (imageUri != null) {
            uploadImage(imageUri);
        }
        Toast.makeText(getContext(), "Changes saved successfully ", Toast.LENGTH_SHORT).show();

    }

    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._%+-]+@[a-zA-Z]+\\.com";
        return Pattern.matches(emailPattern, email);
    }

    public void setLawyerId(String lawyerId) {
        this.lawyerId = lawyerId;
    }
}