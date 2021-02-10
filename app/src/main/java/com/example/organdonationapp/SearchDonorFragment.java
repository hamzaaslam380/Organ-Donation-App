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
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchDonorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchDonorFragment extends Fragment {
    EditText etSearchDonor;
    Button btnSearchDonor,btnEditSearchDonor,btnDeleteSearchDonor;
    DatabaseReference firebase;
    String getType;

    EditText tvSearchDonorName;
    EditText tvSearchDonorPhone;
    EditText tvSearchDonorAddress;
    EditText tvSearchDonorDoctorID;
    EditText tvSearchDonorCNIC;
    EditText tvSearchDonorDisease;
    EditText tvSearchDonorOrgan;
    EditText tvSearchDonorAge;
    EditText tvSearchDonorBloodGroup;
    EditText tvSearchDonorID;

    TextView textView13;
    TextView textView14;
    TextView textView15;
    TextView textView16;
    TextView textView17;
    TextView textView18;
    TextView textView19;
    TextView textView20;
    TextView textView21;
    TextView textView22;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SearchDonorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchDonorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchDonorFragment newInstance(String param1, String param2) {
        SearchDonorFragment fragment = new SearchDonorFragment();
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
        View view = inflater.inflate(R.layout.fragment_search_donor, container, false);

        intializeAllFields(view);

        disableAllFields();

        getUserType();

        searchDonor();

        editDoctor();

        deleteDoctor(view);

        return view;
    }

    private void deleteDoctor(View view) {
        btnDeleteSearchDonor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            if (dataSnapshot.child("CNIC").getValue().toString().trim().equals(etSearchDonor.getText().toString().trim())) {
                                dataSnapshot.getRef()
                                        .removeValue()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(view.getContext(), "Success", Toast.LENGTH_SHORT).show();
                                                clearAllFields();
                                                disableAllFields();
                                            }
                                        });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
    }

    private void editDoctor() {
        btnEditSearchDonor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            if (dataSnapshot.child("CNIC").getValue().toString().trim().equals(etSearchDonor.getText().toString().trim())) {

                                Map<String, Object> map = new HashMap<>();
                                map.put("id",tvSearchDonorID.getText().toString().trim());
                                map.put("name",tvSearchDonorName.getText().toString().trim());
                                map.put("phone",tvSearchDonorPhone.getText().toString().trim());
                                map.put("address",tvSearchDonorAddress.getText().toString().trim());
                                map.put("Blood Grouo: " ,tvSearchDonorBloodGroup.getText().toString().trim());
                                map.put("doc_id" ,tvSearchDonorDoctorID.getText().toString().trim());
                                map.put("CNIC",tvSearchDonorCNIC.getText().toString().trim());
                                map.put("age",tvSearchDonorAge.getText().toString().trim());
                                map.put("organ_part",tvSearchDonorOrgan.getText().toString().trim());
                                map.put("disease",tvSearchDonorDisease.getText().toString().trim());

                                dataSnapshot.getRef()
                                        .updateChildren(map)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                clearAllFields();
                                disableAllFields();
                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
    }

    public void getUserType() {

        DatabaseReference user;

        user = FirebaseDatabase.getInstance().getReference().child("Type");

        user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.child("Type").getValue().toString().trim().equals("admin")) {
                        getType = "admin";
                        break;
                    }
                    else{
                        getType="user";
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void searchDonor() {
        btnSearchDonor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etSearchDonor.getText().toString().trim().isEmpty()){
                    etSearchDonor.setError("Please Enter Donor CNIC");
                }
                else{
                    firebase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                if (dataSnapshot.child("CNIC").getValue().toString().trim().equals(etSearchDonor.getText().toString().trim())) {
                                    enableAllFields();
                                    tvSearchDonorID.setText(dataSnapshot.child("id").getValue().toString().trim());
                                    tvSearchDonorName.setText(dataSnapshot.child("name").getValue().toString().trim());
                                    tvSearchDonorPhone.setText( dataSnapshot.child("phone").getValue().toString().trim());
                                    tvSearchDonorAddress.setText( dataSnapshot.child("address").getValue().toString().trim());
                                    tvSearchDonorBloodGroup.setText( dataSnapshot.child("blood").getValue().toString().trim());
                                    tvSearchDonorDoctorID.setText(dataSnapshot.child("doc_id").getValue().toString().trim());
                                    tvSearchDonorCNIC.setText(dataSnapshot.child("CNIC").getValue().toString().trim());
                                    tvSearchDonorAge.setText( dataSnapshot.child("age").getValue().toString().trim());
                                    tvSearchDonorOrgan.setText( dataSnapshot.child("organ_part").getValue().toString().trim());
                                    tvSearchDonorDisease.setText( dataSnapshot.child("disease").getValue().toString().trim());
                                    if(getType.equals("admin")) {
                                        enableAllFields();
                                    }
                                    else{
                                        enableNonEditableFields();
                                        tvSearchDonorID.setVisibility(View.GONE);
                                        textView22.setVisibility(View.GONE);
                                        tvSearchDonorAddress.setVisibility(View.GONE);
                                        tvSearchDonorDoctorID.setVisibility(View.GONE);
                                        textView15.setVisibility(View.GONE);
                                        textView16.setVisibility(View.GONE);
                                    }
                                    break;
                                }
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

    private void enableNonEditableFields() {
        tvSearchDonorName.setEnabled(false);
        tvSearchDonorPhone.setEnabled(false);
        tvSearchDonorAddress.setEnabled(false);
        tvSearchDonorAge.setEnabled(false);
        tvSearchDonorBloodGroup.setEnabled(false);
        tvSearchDonorOrgan.setEnabled(false);
        tvSearchDonorDisease.setEnabled(false);
        tvSearchDonorCNIC.setEnabled(false);
        btnEditSearchDonor.setVisibility(View.GONE);
        btnDeleteSearchDonor.setVisibility(View.GONE);
        textView13.setEnabled(false);
        textView14.setEnabled(false);
        textView15.setEnabled(false);
        textView16.setEnabled(false);
        textView17.setEnabled(false);
        textView18.setEnabled(false);
        textView19.setEnabled(false);
        textView20.setEnabled(false);
        textView21.setEnabled(false);
        textView22.setEnabled(false);
    }

    private void intializeAllFields(View view) {
        firebase = FirebaseDatabase.getInstance().getReference().child("Donors");
        etSearchDonor = view.findViewById(R.id.etSearchDonor);
        btnSearchDonor = view.findViewById(R.id.btnSearchDonor);
        tvSearchDonorName = view.findViewById(R.id.tvSearchDonorName);
        tvSearchDonorPhone = view.findViewById(R.id.tvSearchDonorPhone);
        tvSearchDonorAddress = view.findViewById(R.id.tvSearchDonorAddress);
        tvSearchDonorAge = view.findViewById(R.id.tvSearchDonorAge);
        tvSearchDonorBloodGroup = view.findViewById(R.id.tvSearchDonorBloodGroup);
        tvSearchDonorDisease = view.findViewById(R.id.tvSearchDonorDisease);
        tvSearchDonorDoctorID = view.findViewById(R.id.tvSearchDonorDoctorID);
        tvSearchDonorCNIC = view.findViewById(R.id.tvSearchDonorCNIC);
        tvSearchDonorOrgan = view.findViewById(R.id.tvSearchDonorOrgan);
        tvSearchDonorID = view.findViewById(R.id.tvSearchDonorID);
        btnEditSearchDonor = view.findViewById(R.id.btnEditSearchDonor);
        textView13 = view.findViewById(R.id.textView13);
        textView14 = view.findViewById(R.id.textView14);
        textView15 = view.findViewById(R.id.textView15);
        textView16 = view.findViewById(R.id.textView16);
        textView17 = view.findViewById(R.id.textView17);
        textView18 = view.findViewById(R.id.textView18);
        textView19 = view.findViewById(R.id.textView19);
        textView20 = view.findViewById(R.id.textView20);
        textView21 = view.findViewById(R.id.textView21);
        textView22 = view.findViewById(R.id.textView22);
        btnDeleteSearchDonor = view.findViewById(R.id.btnDeleteSearchDonor);
    }

    public void enableAllFields(){
        tvSearchDonorName.setVisibility(View.VISIBLE);
        tvSearchDonorPhone.setVisibility(View.VISIBLE);
        tvSearchDonorAddress.setVisibility(View.VISIBLE);
        tvSearchDonorDoctorID.setVisibility(View.VISIBLE);
        tvSearchDonorAge.setVisibility(View.VISIBLE);
        tvSearchDonorBloodGroup.setVisibility(View.VISIBLE);
        tvSearchDonorOrgan.setVisibility(View.VISIBLE);
        tvSearchDonorDisease.setVisibility(View.VISIBLE);
        tvSearchDonorCNIC.setVisibility(View.VISIBLE);
        tvSearchDonorID.setVisibility(View.VISIBLE);
        btnEditSearchDonor.setVisibility(View.VISIBLE);
        btnDeleteSearchDonor.setVisibility(View.VISIBLE);
        textView13.setVisibility(View.VISIBLE);
        textView14.setVisibility(View.VISIBLE);
        textView15.setVisibility(View.VISIBLE);
        textView16.setVisibility(View.VISIBLE);
        textView17.setVisibility(View.VISIBLE);
        textView18.setVisibility(View.VISIBLE);
        textView19.setVisibility(View.VISIBLE);
        textView20.setVisibility(View.VISIBLE);
        textView21.setVisibility(View.VISIBLE);
        textView22.setVisibility(View.VISIBLE);
    }
    public void disableAllFields() {
        btnEditSearchDonor.setVisibility(View.GONE);
        btnDeleteSearchDonor.setVisibility(View.GONE);
        tvSearchDonorName.setVisibility(View.GONE);
        tvSearchDonorPhone.setVisibility(View.GONE);
        tvSearchDonorAddress.setVisibility(View.GONE);
        tvSearchDonorDoctorID.setVisibility(View.GONE);
        tvSearchDonorAge.setVisibility(View.GONE);
        tvSearchDonorBloodGroup.setVisibility(View.GONE);
        tvSearchDonorOrgan.setVisibility(View.GONE);
        tvSearchDonorDisease.setVisibility(View.GONE);
        tvSearchDonorCNIC.setVisibility(View.GONE);
        tvSearchDonorID.setVisibility(View.GONE);
        btnEditSearchDonor.setVisibility(View.GONE);
        btnDeleteSearchDonor.setVisibility(View.GONE);
        textView13.setVisibility(View.GONE);
        textView14.setVisibility(View.GONE);
        textView15.setVisibility(View.GONE);
        textView16.setVisibility(View.GONE);
        textView17.setVisibility(View.GONE);
        textView18.setVisibility(View.GONE);
        textView19.setVisibility(View.GONE);
        textView20.setVisibility(View.GONE);
        textView21.setVisibility(View.GONE);
        textView22.setVisibility(View.GONE);
    }
    public void clearAllFields(){
        tvSearchDonorName.setText("");
        tvSearchDonorPhone.setText("");
        tvSearchDonorAddress.setText("");
        tvSearchDonorDoctorID.setText("");
        tvSearchDonorAge.setText("");
        tvSearchDonorBloodGroup.setText("");
        tvSearchDonorOrgan.setText("");
        tvSearchDonorDisease.setText("");
        tvSearchDonorCNIC.setText("");
        tvSearchDonorID.setText("");
        btnEditSearchDonor.setVisibility(View.GONE);
        btnDeleteSearchDonor.setVisibility(View.GONE);
    }
}