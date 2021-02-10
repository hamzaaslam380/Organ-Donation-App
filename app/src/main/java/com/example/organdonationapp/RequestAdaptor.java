package com.example.organdonationapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class RequestAdaptor extends FirebaseRecyclerAdapter<Request, RequestAdaptor.RequestViewHolder> {
    Context context;


    public RequestAdaptor(@NonNull FirebaseRecyclerOptions<Request> options, Context context) {
        super(options);
        this.context=context;
    }

    @Override
    protected void onBindViewHolder(@NonNull RequestViewHolder holder, int position, @NonNull Request model) {
        holder.tvCNIC.setText("CNIC: " + model.getCNIC());
        holder.tvPhone.setText("Phone: " + model.getPhone());
        holder.tvEmail.setText("Email: " + model.getEmail());
        holder.tvRequest.setText("Suggestion/Complain: " + model.getRequest());
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.request_view,parent,false);
        return new RequestViewHolder(view);
        // post view has our own view on recycler view
    }

    public class RequestViewHolder extends RecyclerView.ViewHolder{
        TextView tvPhone,tvCNIC,tvEmail,tvRequest;
        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);

            tvCNIC = itemView.findViewById(R.id.tvCNIC);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvRequest = itemView.findViewById(R.id.tvComplain);

        }
    }

}
