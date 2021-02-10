package com.example.organdonationapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Random;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddNewDonorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddNewDonorFragment extends Fragment {
    EditText etDonorName,etDonorPhone,etDonorAddress,etDonorBlood,etDonorDisease,etDonorDoctorID,etDonorOrganPart,etDonorAge,etDonorCNIC;
    TextView tvGetDoctorID;
    Button addDonorButton,btnGetDoctorID,btnClose;
    DatabaseReference firebase;
    DatabaseReference firebase2;
    int id = 100;
    final int getStatus = 1;
    Context view2;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddNewDonorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddNewDonorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddNewDonorFragment newInstance(String param1, String param2) {
        AddNewDonorFragment fragment = new AddNewDonorFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_add_new_donor, container, false);

        intializeAllFields(view);

        addDonor();

        view2 = view.getContext();

        getDoctorID(view2,view);

        return view;
    }

    private void intializeAllFields(View view) {
        etDonorName = view.findViewById(R.id.etDonorName);
        etDonorPhone = view.findViewById(R.id.etDonorPhone);
        etDonorBlood = view.findViewById(R.id.etDonorBlood);
        etDonorAge = view.findViewById(R.id.etDonorAge);
        etDonorDisease = view.findViewById(R.id.etDonorDisease);
        etDonorDoctorID = view.findViewById(R.id.etDonorDoctorID);
        etDonorAddress = view.findViewById(R.id.etDonorAddress);
        etDonorCNIC = view.findViewById(R.id.etDonorCNIC);
        etDonorOrganPart = view.findViewById(R.id.etDonorOrganPart);
        addDonorButton = view.findViewById(R.id.addDonorButton);
        tvGetDoctorID = view.findViewById(R.id.tvGetDoctorID);
        tvGetDoctorID = view.findViewById(R.id.tvGetDoctorID);
        btnClose = view.findViewById(R.id.btnClose);
        firebase = FirebaseDatabase.getInstance().getReference().child("Donors");
    }

    private void getDoctorID(Context view,View view2) {

        tvGetDoctorID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogPlus dialog = DialogPlus.newDialog(view)
                        .setContentHolder(new ViewHolder(R.layout.get_doctor_info))
                        .setGravity(Gravity.CENTER)
                        .create();
                View itemView = dialog.getHolderView();

                btnClose = itemView.findViewById(R.id.btnClose);

                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

    }

    private void addDonor() {
        addDonorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etDonorName.getText().toString().isEmpty()) {
                    etDonorName.setError("Please Enter Your Name");
                } else if (etDonorPhone.getText().toString().isEmpty()) {
                    etDonorPhone.setError("Please Enter Your Phone Number");
                } else if (etDonorBlood.getText().toString().isEmpty()) {
                    etDonorBlood.setError("Please Enter Your Blood Group");
                } else if (etDonorDoctorID.getText().toString().isEmpty()) {
                    etDonorDoctorID.setError("Please Enter Doctor ID");
                } else if (etDonorDisease.getText().toString().isEmpty()) {
                    etDonorDisease.setError("Please Enter Disease If You Have or Type No");
                } else if (etDonorAge.getText().toString().isEmpty()) {
                    etDonorAge.setError("Please Enter Your Age");
                } else if (etDonorAddress.getText().toString().isEmpty()) {
                    etDonorAddress.setError("Please Enter Your Address");
                } else if (etDonorCNIC.getText().toString().isEmpty()) {
                    etDonorCNIC.setError("Please Enter Your CNIC Number");
                } else if (etDonorOrganPart.getText().toString().isEmpty()) {
                    etDonorOrganPart.setError("Name can't be empty");
                } else if (etDonorCNIC.getText().toString().length() > 15) {
                    etDonorCNIC.setError("Enter Correct CNIC Number");
                } else {
                    firebase = FirebaseDatabase.getInstance().getReference().child("Donors");
                    firebase2 = FirebaseDatabase.getInstance().getReference().child("Doctors");
                    firebase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            int[] yes = {0};
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                if (dataSnapshot.child("CNIC").getValue().toString().trim().equals(etDonorCNIC.getText().toString().trim())) {
                                    yes[0] = 1;
                                    break;
                                }
                            }

                            if (yes[0] == 1) {
                                etDonorCNIC.setError("Donor Already Register");
                            }
                            else {
                                int[] yes2 = {0};
                                firebase2.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                            if (dataSnapshot.child("id").getValue().toString().trim().equals(etDonorDoctorID.getText().toString().trim())) {
                                                yes2[0] = 1;
                                                break;
                                            }
                                        }
                                        // doctor id exist
                                        if(yes2[0] == 1){
                                            // get last id for icrement
                                            firebase.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                        id = Integer.parseInt(dataSnapshot.child("id").getValue().toString());
                                                    }
                                                    id++;
                                                    HashMap<String, String> post = new HashMap<>();
                                                    post.put("id", String.valueOf(id));
                                                    post.put("name", etDonorName.getText().toString().trim());
                                                    post.put("phone", etDonorPhone.getText().toString().trim());
                                                    post.put("disease", etDonorDisease.getText().toString().trim());
                                                    post.put("age", etDonorAge.getText().toString().trim());
                                                    post.put("blood", etDonorBlood.getText().toString().trim());
                                                    post.put("doc_id", etDonorDoctorID.getText().toString().trim());
                                                    post.put("address", etDonorAddress.getText().toString().trim());
                                                    post.put("CNIC", etDonorCNIC.getText().toString().trim());
                                                    post.put("organ_part", etDonorOrganPart.getText().toString().trim());


                                                    // to store data on new node we use push
                                                    firebase.push().setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            resetAllFields();
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                        }
                                                    });

                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });

                                        }
                                        else{
                                            etDonorDoctorID.setError("Doctor id doesn't exist");
                                        }


                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==getStatus){
            if(resultCode==RESULT_OK){

            }
            else{

            }
        }
    }

    public void resetAllFields(){
        etDonorName.setText("");
        etDonorPhone.setText("");
        etDonorAddress.setText("");
        etDonorBlood.setText("");
        etDonorDisease.setText("");
        etDonorDoctorID.setText("");
        etDonorOrganPart.setText("");
        etDonorAge.setText("");
        etDonorCNIC.setText("");
    }
}