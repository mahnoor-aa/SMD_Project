package com.example.firebase_lawyer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SubscribersAdap extends RecyclerView.Adapter<SubscribersAdap.ViewHolder> {

    private Context context;
    private ArrayList<Subscribers> list;

    private OnRemoveClickListener onRemoveClickListener;


    public interface OnRemoveClickListener {
        void onRemoveClick(Subscribers subscriber);
    }

    public void setOnRemoveClickListener(OnRemoveClickListener listener) {
        this.onRemoveClickListener = listener;
    }

    public SubscribersAdap(Context context, ArrayList<Subscribers> list, OnRemoveClickListener listener) {
        this.context = context;
        this.list = list;
        this.onRemoveClickListener = listener;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_subs, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Subscribers gig = list.get(position);
        holder.bind(gig);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvUsername;
        private TextView tvprice;
        private TextView tvhearing;
        private TextView tvCaseTitle;
        ImageView ivEdit,ivRemove;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername=itemView.findViewById(R.id.tvUsernameSubs);
            tvprice = itemView.findViewById(R.id.tvPrice);
            tvhearing=itemView.findViewById(R.id.tvnumber);
            tvCaseTitle=itemView.findViewById(R.id.tvCaseTitleSubs);
            ivEdit=itemView.findViewById(R.id.ivEdit);
            ivRemove=itemView.findViewById(R.id.ivRemove);

            ivRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Are you sure you want to delete this case?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    int position = getAdapterPosition();
                                    if (position != RecyclerView.NO_POSITION) {
                                        Subscribers subscriber = list.get(position);
                                        onRemoveClickListener.onRemoveClick(subscriber);
                                    }
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
            });

            ivEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showEditDialog(list.get(getAdapterPosition()));
                }
            });
        }

        private void showEditDialog(Subscribers subscriber) {
            DatabaseReference gigsRef = FirebaseDatabase.getInstance().getReference().child("Gigs").child(subscriber.getGigid());
            gigsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        GigClass gig = dataSnapshot.getValue(GigClass.class);
                        if (gig != null) {
                            int pricePerHearing = gig.getPrice();
                            showDialogWithPricePerHearing(subscriber, pricePerHearing);
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }

        private void showDialogWithPricePerHearing(Subscribers subscriber, int pricePerHearing) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.CustomDialogStyle);
            LayoutInflater inflater = LayoutInflater.from(context);
            View dialogView = inflater.inflate(R.layout.edit_dialog, null);
            builder.setView(dialogView);

            EditText editTextHearing = dialogView.findViewById(R.id.editTextHearing);
            editTextHearing.setText(String.valueOf(subscriber.getHearing()));

            builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    int newHearing = Integer.parseInt(editTextHearing.getText().toString());
                    String newPrice= String.valueOf(pricePerHearing * newHearing);
                    subscriber.setHearing(newHearing);
                    subscriber.setPrice(newPrice);

                    DatabaseReference subscribersRef = FirebaseDatabase.getInstance().getReference().child("Approved Gigs").child(subscriber.getId());
                    subscribersRef.child("hearing").setValue(newHearing);
                    subscribersRef.child("price").setValue(newPrice);

                    notifyItemChanged(getAdapterPosition());
                    dialog.dismiss();
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();

            Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
            Button negativeButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 0, 20, 0);
            negativeButton.setLayoutParams(params);

            positiveButton.setBackground(ContextCompat.getDrawable(context, R.drawable.custom_button_background));
            positiveButton.setTextColor(ContextCompat.getColor(context, R.color.Three));
            negativeButton.setBackground(ContextCompat.getDrawable(context, R.drawable.custom_button_background));
            negativeButton.setTextColor(ContextCompat.getColor(context, R.color.Three));
        }

        public void bind(Subscribers gig) {
            tvUsername.setText(gig.getUsername());
            tvprice.setText(gig.getPrice());
            tvhearing.setText(String.valueOf(gig.getHearing()));
            tvCaseTitle.setText(gig.getCasetitle());
        }
    }
}
