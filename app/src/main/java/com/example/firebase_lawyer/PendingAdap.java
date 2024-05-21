package com.example.firebase_lawyer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class PendingAdap extends RecyclerView.Adapter<PendingAdap.ViewHolder> {
    private Context context;
    private ArrayList<PendingGig> list;
    private Pending fragment;
    public PendingAdap(Context context, ArrayList<PendingGig> list, Pending fragment) {
        this.context = context;
        this.list = list;
        this.fragment = fragment;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_request, parent, false);
       return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull PendingAdap.ViewHolder holder, int position) {
        PendingGig gig = list.get(position);
        holder.bind(gig);

        holder.ivTick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showApproveConfirmationDialog(gig, holder);
            }
        });

        holder.ivCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteConfirmationDialog(gig, holder);
            }
        });
    }

    private void showApproveConfirmationDialog(PendingGig gig,ViewHolder holder) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Are you sure you want to approve this request?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        fragment.approveGig(gig);
                        int adapterPosition = holder.getAdapterPosition();
                        if (adapterPosition != RecyclerView.NO_POSITION) {
                            PendingGig deletedGig = list.remove(adapterPosition);
                            notifyItemRemoved(adapterPosition);
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

    private void showDeleteConfirmationDialog(PendingGig gig,ViewHolder holder) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Are you sure you want to delete this request?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        int adapterPosition = holder.getAdapterPosition();
                        if (adapterPosition != RecyclerView.NO_POSITION) {
                            PendingGig deletedGig = list.remove(adapterPosition);
                            notifyItemRemoved(adapterPosition);
                            fragment.deleteGig(deletedGig);
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
    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvcaseTitle;
        private TextView tvcaseId;
        private TextView tvuserId;
        private TextView tvUsername;
        private TextView tvgigTitle;
        ImageView ivCross;
        ImageView ivTick;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvcaseTitle = itemView.findViewById(R.id.tvCaseTitle);
            tvcaseId = itemView.findViewById(R.id.tvCaseid);
            tvuserId = itemView.findViewById(R.id.tvUserid);
            tvUsername=itemView.findViewById(R.id.tvUserfullname);
            tvgigTitle=itemView.findViewById(R.id.tvgigTitle);
            ivCross = itemView.findViewById(R.id.ivCross);
            ivTick=itemView.findViewById(R.id.ivTick);

        }
        public void bind(PendingGig gig) {
            tvcaseTitle.setText(gig.getCasetitle());
            tvcaseId.setText(gig.getCaseid());
            tvuserId.setText(gig.getUsername());
            tvUsername.setText(gig.getUserfullname());
            tvgigTitle.setText(gig.getGigTitle());
        }
    }
}
