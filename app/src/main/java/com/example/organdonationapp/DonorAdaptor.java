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

import java.util.HashMap;
import java.util.Map;

public class DonorAdaptor extends FirebaseRecyclerAdapter<Donor, DonorAdaptor.DonorViewHolder> {
    Context context;
    DatabaseReference user;
    EditText etDialogDonorName;
    EditText etDialogDonorPhone;
    EditText etDialogDonorAddress;
    EditText etDialogDonorCNIC;
    EditText etDialogDonorAge;
    EditText etDialogDonorBlood;
    EditText etDialogDonorDoctorID;
    EditText etDialogDonorDisease;
    EditText etDialogDonorOrgan;
    EditText etDialogDonorID;
    Button btnEditDonor;

    public DonorAdaptor(@NonNull FirebaseRecyclerOptions<Donor> options, Context context) {
        super(options);
        this.context=context;
    }


    @Override
    protected void onBindViewHolder(@NonNull DonorViewHolder holder, int position, @NonNull Donor model) {

        setTextInHolder(holder,model);

        // for admin access we create child of type and update tht child with user type
        user = FirebaseDatabase.getInstance().getReference().child("Type");

        setTextAccordingToAdminRights(holder,model);

        deleteDonor(holder,position);

        editDonor(holder,position,model);

        callUser(holder,model);
    }

    private void callUser(DonorViewHolder holder, Donor model) {
        holder.ivCallDonor.setOnClickListener(new View.OnClickListener() {
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

    private void editDonor(DonorViewHolder holder, int position, Donor model) {
        holder.ivEditDonor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogPlus dialog = DialogPlus.newDialog(context)
                        .setContentHolder(new ViewHolder(R.layout.edit_donor_dialog))
                        .setGravity(Gravity.CENTER)
                        .create();

                View itemView = dialog.getHolderView();

                intializeAllFields(itemView);

                setTextInDialog(itemView,model);

                btnEditDonor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String , Object> map = new HashMap<>();

                        map.put("id",etDialogDonorID.getText().toString().trim());
                        map.put("name",etDialogDonorName.getText().toString().trim());
                        map.put("phone",etDialogDonorPhone.getText().toString().trim());
                        map.put("blood",etDialogDonorBlood.getText().toString().trim());
                        map.put("address",etDialogDonorAddress.getText().toString().trim());
                        map.put("age",etDialogDonorAge.getText().toString().trim());
                        map.put("doc_id",etDialogDonorDoctorID.getText().toString().trim());
                        map.put("CNIC",etDialogDonorCNIC.getText().toString().trim());
                        map.put("organ_part",etDialogDonorOrgan.getText().toString().trim());
                        map.put("disease",etDialogDonorDisease.getText().toString().trim());


                        FirebaseDatabase.getInstance().getReference()
                                .child("Donors").child(getRef(position).getKey())
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

    private void setTextInDialog(View itemView,Donor model) {
        etDialogDonorName.setText(model.getName());
        etDialogDonorID.setText(model.getId());
        etDialogDonorPhone.setText(model.getPhone());
        etDialogDonorAddress.setText(model.getAddress());
        etDialogDonorBlood.setText(model.getBlood());
        etDialogDonorDoctorID.setText(model.getDoc_id());
        etDialogDonorDisease.setText(model.getDisease());
        etDialogDonorOrgan.setText(model.getOrgan_part());
        etDialogDonorAge.setText(model.getAge());
        etDialogDonorCNIC.setText(model.getCNIC());
    }

    private void intializeAllFields(View itemView) {
        etDialogDonorName = itemView.findViewById(R.id.etDialogDonorName);
        etDialogDonorPhone = itemView.findViewById(R.id.etDialogDonorPhone);
        etDialogDonorAddress = itemView.findViewById(R.id.etDialogDonorAddress);
        etDialogDonorCNIC = itemView.findViewById(R.id.etDialogDonorCNIC);
        etDialogDonorAge = itemView.findViewById(R.id.etDialogDonorAge);
        etDialogDonorDisease = itemView.findViewById(R.id.etDialogDonorDisease);
        etDialogDonorDoctorID = itemView.findViewById(R.id.etDialogDonorDoctorID);
        etDialogDonorOrgan = itemView.findViewById(R.id.etDialogDonorOrgan);
        etDialogDonorBlood = itemView.findViewById(R.id.etDialogDonorBlood);
        etDialogDonorID = itemView.findViewById(R.id.etDialogDonorID);
        btnEditDonor = itemView.findViewById(R.id.btnEditDonor);
    }

    private void deleteDonor(DonorViewHolder holder, int position) {
        holder.ivDeleteDonor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("Donors")
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

    private void setTextAccordingToAdminRights(DonorViewHolder holder, Donor model) {
        user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if(dataSnapshot.child("Type").getValue().toString().trim().equals("admin")) {
                        holder.tvDonorAddress.setVisibility(View.VISIBLE);
                        holder.ivDeleteDonor.setVisibility(View.VISIBLE);
                        holder.tvDonorDoctorID.setVisibility(View.VISIBLE);
                        holder.ivEditDonor.setVisibility(View.VISIBLE);
                    }
                    else {
                        holder.tvDonorAddress.setVisibility(View.GONE);
                        holder.ivDeleteDonor.setVisibility(View.GONE);
                        holder.tvDonorDoctorID.setVisibility(View.GONE);
                        holder.ivEditDonor.setVisibility(View.GONE);
                        holder.tvDonorCNIC.setVisibility(View.GONE);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setTextInHolder(DonorViewHolder holder,Donor model) {
        holder.tvDonorName.setText("Name: " + model.getName());
        holder.tvDonorPhone.setText("Phone: " + model.getPhone());
        holder.tvDonorAddress.setText("Address: " + model.getAddress());
        holder.tvDonorBloodGroup.setText("Blood Group: " + model.getBlood());
        holder.tvDonorDoctorID.setText("Doctor ID: " + model.getDoc_id());
        holder.tvDonorDisease.setText("Disease: " + model.getDisease());
        holder.tvDonorOrgan.setText("Organ Part: " + model.getOrgan_part());
        holder.tvDonorAge.setText("Age: " + model.getAge());
        holder.tvDonorCNIC.setText("CNIC: " + model.getCNIC());
    }

    @NonNull
    @Override
    public DonorAdaptor.DonorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.donor_view,parent,false);
        return new DonorAdaptor.DonorViewHolder(view);
        // post view has our own view on recycler view
    }

    public class DonorViewHolder extends RecyclerView.ViewHolder{
        TextView tvDonorName,tvDonorPhone,tvDonorAddress,tvDonorBloodGroup,tvDonorDoctorID,tvDonorDisease,tvDonorOrgan,tvDonorAge,tvDonorCNIC;
        ImageView ivEditDonor,ivDeleteDonor,ivCallDonor;

        public DonorViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDonorName = itemView.findViewById(R.id.tvDonorName);
            tvDonorPhone = itemView.findViewById(R.id.tvDonorPhone);
            tvDonorDisease = itemView.findViewById(R.id.tvDonorDisease);
            tvDonorAge = itemView.findViewById(R.id.tvDonorAge);
            tvDonorBloodGroup = itemView.findViewById(R.id.tvDonorBlood);
            tvDonorDoctorID = itemView.findViewById(R.id.tvDonorDoctorID);
            tvDonorAddress = itemView.findViewById(R.id.tvDonorAddress);
            tvDonorCNIC = itemView.findViewById(R.id.tvDonorCNIC);
            tvDonorOrgan = itemView.findViewById(R.id.tvDonorOrganPart);

            ivDeleteDonor = itemView.findViewById(R.id.ivDeleteDonor);
            ivEditDonor = itemView.findViewById(R.id.ivEditDonor);
            ivCallDonor = itemView.findViewById(R.id.ivCallDonor);

        }
    }

}
