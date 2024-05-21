package com.example.firebase_lawyer;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class GigAdap extends RecyclerView.Adapter<GigAdap.ViewHolder> {
    private Context context;
    private ArrayList<GigClass> list;

    private OnEditClickListener onEditClickListener1;
    private OnPeopleClickListener onPeopleClickListener1;
    public interface OnEditClickListener {
        void onEditClick(String gigId);
    }

    public interface OnPeopleClickListener {
        void onPeopleClick(String gigId);
    }



    public GigAdap(Context context, ArrayList<GigClass> list, OnEditClickListener listener,OnPeopleClickListener peopleClickListener) {
        this.context = context;
        this.list = list;
        this.onEditClickListener1 = listener;
        this.onPeopleClickListener1 = peopleClickListener;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_gig, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GigClass gig = list.get(position);
        holder.bind(gig);

        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEditClickListener1.onEditClick(gig.getId());
            }
        });

        holder.ivPeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPeopleClickListener1.onPeopleClick(gig.getId());
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;
        private TextView tvdesc;
        private TextView tvprice;
        private TextView tvCity;
        ImageView ivEdit;
        ImageView ivPeople;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvdisplayTitle);
            tvdesc = itemView.findViewById(R.id.tvdisplaydesc);
            tvprice = itemView.findViewById(R.id.tvdisplayprice);
            tvCity=itemView.findViewById(R.id.tvdisplaycity);
            ivEdit=itemView.findViewById(R.id.ivEdit);
            ivPeople=itemView.findViewById(R.id.ivPeople);
        }

        public void bind(GigClass gig) {
            tvTitle.setText(gig.getTitle());
            tvdesc.setText(gig.getDesc());
            tvprice.setText(String.valueOf(gig.getPrice()));
            tvCity.setText(gig.getCity());
        }
    }
}
