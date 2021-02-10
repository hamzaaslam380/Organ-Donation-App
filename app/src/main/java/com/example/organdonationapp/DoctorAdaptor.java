package com.example.organdonationapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DoctorAdaptor extends FirebaseRecyclerAdapter<Doctor, DoctorAdaptor.DoctorViewHolder> {
    Context context;
    DatabaseReference user;
    EditText etDialogDoctorName;
    EditText etDialogDoctorPhone;
    EditText etDialogDoctorAddress;
    EditText etDialogDoctorHospitalName;
    EditText etDialogDoctorCNIC;
    EditText etDialogDoctorID;
    EditText etDialogDoctorStatus;
    Button btnEditDoctor;

    public DoctorAdaptor(@NonNull FirebaseRecyclerOptions<Doctor> options, Context context) {
        super(options);
        this.context = context;
    }


    @Override
    protected void onBindViewHolder(@NonNull DoctorViewHolder holder, int position, @NonNull Doctor model) {

        setDataInHolderOfWaitingDoctors(holder, model);

        // for admin access we create child of type and update tht child with user type
        // user = FirebaseDatabase.getInstance().getReference().child("Type");
        setDataInHolder(holder, model);

        deleteDoctor(holder, position);

        editDoctor(holder, position, model);

        callDoctor(holder,model);

    }

    private void callDoctor(DoctorViewHolder holder, Doctor model) {
        holder.ivCallDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = model.getPhone();
                Uri num = Uri.parse("tel:" + number);
                Intent dial = new Intent(Intent.ACTION_DIAL);
                dial.setData(num);
                context.startActivity(dial);
            }
        });
    }


    private void editDoctor(DoctorViewHolder holder, int position,Doctor model) {
        holder.ivEditDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogPlus dialog = DialogPlus.newDialog(context)
                        .setContentHolder(new ViewHolder(R.layout.edit_doctor_dialog))
                        .setGravity(Gravity.CENTER)
                        .create();

                View itemView = dialog.getHolderView();

                intializeDialogFields(itemView);

                setTextInDialogFields(model);

                btnEditDoctor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("id", etDialogDoctorID.getText().toString().trim());
                        map.put("name", etDialogDoctorName.getText().toString().trim());
                        map.put("phone", etDialogDoctorPhone.getText().toString().trim());
                        map.put("address", etDialogDoctorAddress.getText().toString().trim());
                        map.put("hospital_name", etDialogDoctorHospitalName.getText().toString().trim());
                        map.put("CNIC", etDialogDoctorCNIC.getText().toString().trim());
                        map.put("status", etDialogDoctorStatus.getText().toString().trim());


                        FirebaseDatabase.getInstance().getReference()
                                .child("Doctors").child(getRef(position).getKey())
                                .updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(context, "Data Updated Successfully", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }
                                });
                    }
                });
                dialog.show();
            }
        });
    }

    private void setTextInDialogFields(Doctor model) {
        etDialogDoctorID.setText(model.getId());
        etDialogDoctorName.setText(model.getName());
        etDialogDoctorPhone.setText(model.getPhone());
        etDialogDoctorAddress.setText(model.getAddress());
        etDialogDoctorHospitalName.setText(model.getHospital_name());
        etDialogDoctorCNIC.setText(model.getCNIC());
        etDialogDoctorStatus.setText(model.getStatus());
    }

    private void intializeDialogFields(View itemView) {
        btnEditDoctor = itemView.findViewById(R.id.btnEditDoctor);
        etDialogDoctorName = itemView.findViewById(R.id.etDialogDoctorName);
        etDialogDoctorPhone = itemView.findViewById(R.id.etDialogDoctorPhone);
        etDialogDoctorAddress = itemView.findViewById(R.id.etDialogDoctorAddress);
        etDialogDoctorHospitalName = itemView.findViewById(R.id.etDialogDoctorHospitalName);
        etDialogDoctorCNIC = itemView.findViewById(R.id.etDialogDoctorCNIC);
        etDialogDoctorID = itemView.findViewById(R.id.etDialogDoctorID);
        etDialogDoctorStatus = itemView.findViewById(R.id.etDialogDoctorStatus);
    }

    private void deleteDoctor(DoctorViewHolder holder, int position) {
        holder.ivDeleteDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("Doctors")
                        .child(getRef(position).getKey())
                        .removeValue()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });
    }

    private void setDataInHolderOfWaitingDoctors(DoctorViewHolder holder, Doctor model) {
        if(model.getStatus().equals("waiting") == false){
            holder.tvDoctorName.setText("Name: " + model.getName());
            holder.tvDoctorPhone.setText("Phone: " + model.getPhone());
            holder.tvDoctorAddress.setText("Address: " + model.getAddress());
            holder.tvDoctorHospitalName.setText("Hospital/Clinic: " + model.getHospital_name());
            holder.tvDoctorCNIC.setText("CNIC: " + model.getCNIC());
            holder.tvDoctorStatus.setText("Status:" + model.getStatus());
        }
        if((model.getStatus().equals("waiting")))
            holder.tvDoctorStatus.setText("Waiting For Confirmation .......");
    }

    private void setDataInHolder(DoctorViewHolder holder,Doctor model) {
        user = FirebaseDatabase.getInstance().getReference().child("Type");
        user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if(dataSnapshot.child("Type").getValue().toString().trim().equals("admin") && model.getStatus().equals("waiting") == false) {
                        holder.tvDoctorName.setText("Name: " + model.getName());
                        holder.tvDoctorPhone.setText("Phone: " + model.getPhone());
                        holder.tvDoctorAddress.setText("Address: " + model.getAddress());
                        holder.tvDoctorHospitalName.setText("Hospital/Clinic: " + model.getHospital_name());
                        holder.tvDoctorCNIC.setText("CNIC: " + model.getCNIC());
                        holder.tvDoctorStatus.setText("Status: " + model.getStatus());
                        holder.tvDoctorAddress.setVisibility(View.VISIBLE);
                        holder.ivDeleteDoctor.setVisibility(View.VISIBLE);
                        holder.ivEditDoctor.setVisibility(View.VISIBLE);
                    }
                    else if(dataSnapshot.child("Type").getValue().toString().trim().equals("admin") && model.getStatus().equals("waiting")) {
                        holder.tvDoctorName.setText("Name: " + model.getName());
                        holder.tvDoctorPhone.setText("Phone: " + model.getPhone());
                        holder.tvDoctorAddress.setText("Address: " + model.getAddress());
                        holder.tvDoctorHospitalName.setText("Hospital/Clinic: " + model.getHospital_name());
                        holder.tvDoctorCNIC.setText("CNIC: " + model.getCNIC());
                        holder.tvDoctorStatus.setText("Status: " + model.getStatus());
                        holder.tvDoctorAddress.setVisibility(View.VISIBLE);
                        holder.ivDeleteDoctor.setVisibility(View.VISIBLE);
                        holder.ivEditDoctor.setVisibility(View.VISIBLE);
                    }
                    else {
                        holder.tvDoctorAddress.setVisibility(View.GONE);
                        holder.tvDoctorCNIC.setVisibility(View.GONE);
                        holder.ivDeleteDoctor.setVisibility(View.GONE);
                        holder.ivEditDoctor.setVisibility(View.GONE);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @NonNull
    @Override
    public DoctorAdaptor.DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.doctor_view,parent,false);
        return new DoctorAdaptor.DoctorViewHolder(view);
        // post view has our own view on recycler view
    }

    public class DoctorViewHolder extends RecyclerView.ViewHolder{
        TextView tvDoctorName,tvDoctorPhone,tvDoctorAddress,tvDoctorHospitalName,tvDoctorCNIC,tvDoctorStatus;
        ImageView ivEditDoctor,ivDeleteDoctor,ivCallDoctor;

        public DoctorViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDoctorName = itemView.findViewById(R.id.tvDoctorName);
            tvDoctorPhone = itemView.findViewById(R.id.tvDoctorPhone);
            tvDoctorAddress = itemView.findViewById(R.id.tvDoctorAddress);
            tvDoctorHospitalName= itemView.findViewById(R.id.tvDoctorHospitalName);
            tvDoctorCNIC = itemView.findViewById(R.id.tvDoctorCNIC);
            tvDoctorStatus = itemView.findViewById(R.id.tvDoctorStatus);

            ivEditDoctor = itemView.findViewById(R.id.ivEditDoctor);
            ivDeleteDoctor = itemView.findViewById(R.id.ivDeleteDoctor);

            ivCallDoctor = itemView.findViewById(R.id.ivCallDoctor);

        }
    }

}
