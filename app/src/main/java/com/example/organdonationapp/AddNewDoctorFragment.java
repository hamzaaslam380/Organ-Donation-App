package com.example.organdonationapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddNewDoctorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddNewDoctorFragment extends Fragment {
    EditText etDoctorName,etDoctorPhone,etDoctorAddress,etDoctorHospitalName,etDoctorCNIC;
    Button addDoctorButton;
    DatabaseReference firebase;
    DatabaseReference firebase2;
    int id = 100;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddNewDoctorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddNewDoctorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddNewDoctorFragment newInstance(String param1, String param2) {
        AddNewDoctorFragment fragment = new AddNewDoctorFragment();
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
        View view = inflater.inflate(R.layout.fragment_add_new_doctor, container, false);

        intializeAllFields(view);

        addDoctor();

        return view;
    }

    private void addDoctor() {
        addDoctorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etDoctorName.getText().toString().isEmpty()) {
                    etDoctorName.setError("Please Enter Your Name");
                } else if (etDoctorPhone.getText().toString().isEmpty()) {
                    etDoctorPhone.setError("Please Enter Your Phone Number");
                }  else if (etDoctorHospitalName.getText().toString().isEmpty()) {
                    etDoctorHospitalName.setError("Please Enter Hospital/Clinic Name");
                } else if (etDoctorAddress.getText().toString().isEmpty()) {
                    etDoctorAddress.setError("Please Enter Your Address");
                } else if (etDoctorCNIC.getText().toString().isEmpty()) {
                    etDoctorCNIC.setError("Please Enter Your CNIC");
                } else if (etDoctorCNIC.getText().toString().length() > 15) {
                    etDoctorCNIC.setError("Enter Correct CNIC Number");
                } else {
                    firebase = FirebaseDatabase.getInstance().getReference().child("Doctors");
                    firebase2 = FirebaseDatabase.getInstance().getReference().child("Donors");

                    firebase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            int[] yes = {0};
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                if (dataSnapshot.child("CNIC").getValue().toString().trim().equals(etDoctorCNIC.getText().toString().trim())) {
                                    yes[0] = 1;
                                    break;
                                }
                            }

                            if (yes[0] == 1) {
                                etDoctorCNIC.setError("Doctor Already Register");
                            }
                            else {
                                // get last id for increment
                                firebase.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                            id = Integer.parseInt(dataSnapshot.child("id").getValue().toString().trim());
                                        }
                                        id++;

                                        HashMap<String, String> post = new HashMap<>();
                                        post.put("id", String.valueOf(id));
                                        post.put("name", etDoctorName.getText().toString().trim());
                                        post.put("phone", etDoctorPhone.getText().toString().trim());
                                        post.put("address", etDoctorAddress.getText().toString().trim());
                                        post.put("hospital_name", etDoctorHospitalName.getText().toString().trim());
                                        post.put("CNIC", etDoctorCNIC.getText().toString().trim());
                                        post.put("status","waiting");

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
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

    }

    public void intializeAllFields(View view){
        etDoctorName = view.findViewById(R.id.etDoctorName);
        etDoctorPhone = view.findViewById(R.id.etDoctorPhone);
        etDoctorAddress = view.findViewById(R.id.etDoctorAddress);
        etDoctorHospitalName = view.findViewById(R.id.etDoctorHospitalName);
        etDoctorCNIC= view.findViewById(R.id.etDoctorCNIC);
        addDoctorButton = view.findViewById(R.id.addDoctorButton);
        firebase = FirebaseDatabase.getInstance().getReference().child("Requests");
    }

    public void resetAllFields(){
        etDoctorPhone.setText("");
        etDoctorName.setText("");
        etDoctorCNIC.setText("");
        etDoctorHospitalName.setText("");
        etDoctorAddress.setText("");
    }
}