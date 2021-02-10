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
 * Use the {@link SearchDoctorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchDoctorFragment extends Fragment {
    EditText etSearchDoctor;
    Button btnSearchDoctor,btnEditSearchDoctor,btnDeleteSearchDoctor;
    DatabaseReference firebase,user;
    String getType="";

    EditText etSearchDoctorName;
    EditText etSearchDoctorPhone;
    EditText etSearchDoctorAddress;
    EditText etSearchDoctorHospitalName;
    EditText etSearchDoctorCNIC;
    EditText etSearchDoctorID;
    EditText etSearchDoctorStatus;

    TextView textView8;
    TextView textView9;
    TextView textView10;
    TextView textView11;
    TextView textView12;
    TextView textView23;
    TextView textView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SearchDoctorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchDoctorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchDoctorFragment newInstance(String param1, String param2) {
        SearchDoctorFragment fragment = new SearchDoctorFragment();
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
        View view = inflater.inflate(R.layout.fragment_search_doctor, container, false);

        intializeAllFields(view);

        disableAllFields();

        getUserType();

        deleteDoctor(view);

        searchDoctor(view);

        editDoctor(view);

        return view;
    }

    private void editDoctor(View view) {
        btnEditSearchDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            if (dataSnapshot.child("CNIC").getValue().toString().trim().equals(etSearchDoctor.getText().toString().trim())) {
                                Map<String, Object> map = new HashMap<>();
                                map.put("id", etSearchDoctorID.getText().toString().trim());
                                map.put("name", etSearchDoctorName.getText().toString().trim());
                                map.put("phone", etSearchDoctorPhone.getText().toString().trim());
                                map.put("address", etSearchDoctorAddress.getText().toString().trim());
                                map.put("hospital_name",etSearchDoctorHospitalName.getText().toString().trim());
                                map.put("CNIC", etSearchDoctorCNIC.getText().toString().trim());
                                map.put("status", etSearchDoctorStatus.getText().toString().trim());

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

    private void searchDoctor(View view) {
        btnSearchDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etSearchDoctor.getText().toString().trim().isEmpty()){
                    etSearchDoctor.setError("Please Enter Doctor CNIC");
                }
                else{
                    firebase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                if (dataSnapshot.child("CNIC").getValue().toString().trim().equals(etSearchDoctor.getText().toString().trim())) {
                                    enableAllFields();
                                    etSearchDoctorID.setText(dataSnapshot.child("id").getValue().toString().trim());
                                    etSearchDoctorName.setText(dataSnapshot.child("name").getValue().toString().trim());
                                    etSearchDoctorPhone.setText( dataSnapshot.child("phone").getValue().toString().trim());
                                    etSearchDoctorAddress.setText(dataSnapshot.child("address").getValue().toString().trim());
                                    etSearchDoctorHospitalName.setText(dataSnapshot.child("hospital_name").getValue().toString().trim());
                                    etSearchDoctorCNIC.setText(dataSnapshot.child("CNIC").getValue().toString().trim());
                                    etSearchDoctorStatus.setText(dataSnapshot.child("status").getValue().toString().trim());

                                    user = FirebaseDatabase.getInstance().getReference().child("Type");

                                    user.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                if(dataSnapshot.child("Type").getValue().toString().trim().equals("admin")) {
                                                    enableAllFields();
                                                    break;
                                                }
                                                else {
                                                    enableNonEditableFields();
                                                    textView23.setVisibility(View.GONE);
                                                    etSearchDoctorID.setVisibility(View.GONE);
                                                    etSearchDoctorAddress.setVisibility(View.GONE);
                                                    textView10.setVisibility(View.GONE);
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
        etSearchDoctorName.setEnabled(false);
        etSearchDoctorPhone.setEnabled(false);
        etSearchDoctorHospitalName.setEnabled(false);
        etSearchDoctorCNIC.setEnabled(false);
        btnEditSearchDoctor.setVisibility(View.GONE);
        btnDeleteSearchDoctor.setVisibility(View.GONE);
        etSearchDoctorStatus.setEnabled(false);
        textView8.setEnabled(false);
        textView9.setEnabled(false);
        textView11.setEnabled(false);
        textView12.setEnabled(false);
        textView.setEnabled(false);
    }

    private void deleteDoctor(View view) {
        btnDeleteSearchDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            if (dataSnapshot.child("CNIC").getValue().toString().trim().equals(etSearchDoctor.getText().toString().trim())) {
                                dataSnapshot.getRef()
                                        .removeValue()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Intent intent = new Intent(view.getContext(),DoctorActivity.class);
                                                intent.putExtra("Type",getType);
                                                startActivity(intent);
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

    private void intializeAllFields(View view) {
        firebase = FirebaseDatabase.getInstance().getReference().child("Doctors");
        etSearchDoctor = view.findViewById(R.id.etSearchDoctor);
        btnSearchDoctor = view.findViewById(R.id.btnSearchDoctor);
        etSearchDoctorName = view.findViewById(R.id.tvSearchDoctorName);
        etSearchDoctorPhone = view.findViewById(R.id.tvSearchDoctorPhone);
        etSearchDoctorAddress = view.findViewById(R.id.tvSearchDoctorAddress);
        etSearchDoctorHospitalName = view.findViewById(R.id.tvSearchDoctorHospitalName);
        etSearchDoctorCNIC = view.findViewById(R.id.tvSearchDoctorCNIC);
        etSearchDoctorID = view.findViewById(R.id.tvSearchDoctorID);
        etSearchDoctorStatus = view.findViewById(R.id.tvSearchDoctorStatus);
        btnEditSearchDoctor = view.findViewById(R.id.btnEditSearchDoctor);
        btnDeleteSearchDoctor = view.findViewById(R.id.btnDeleteSearchDoctor);
        textView8 = view.findViewById(R.id.textView8);
        textView9 = view.findViewById(R.id.textView9);
        textView10 = view.findViewById(R.id.textView10);
        textView11 = view.findViewById(R.id.textView11);
        textView12 = view.findViewById(R.id.textView12);
        textView23 = view.findViewById(R.id.textView23);
        textView = view.findViewById(R.id.textView);
    }

    private void clearAllFields() {
        etSearchDoctorName.setText("");
        etSearchDoctorPhone.setText("");
        etSearchDoctorAddress.setText("");
        etSearchDoctorHospitalName.setText("");
        etSearchDoctorCNIC.setText("");
        etSearchDoctorID.setText("");
        btnEditSearchDoctor.setVisibility(View.GONE);
    }

    public void enableAllFields(){
        etSearchDoctorName.setVisibility(View.VISIBLE);
        etSearchDoctorPhone.setVisibility(View.VISIBLE);
        etSearchDoctorAddress.setVisibility(View.VISIBLE);
        etSearchDoctorID.setVisibility(View.VISIBLE);
        etSearchDoctorHospitalName.setVisibility(View.VISIBLE);
        etSearchDoctorCNIC.setVisibility(View.VISIBLE);
        btnEditSearchDoctor.setVisibility(View.VISIBLE);
        btnDeleteSearchDoctor.setVisibility(View.VISIBLE);
        etSearchDoctorStatus.setVisibility(View.VISIBLE);
        textView8.setVisibility(View.VISIBLE);
        textView9.setVisibility(View.VISIBLE);
        textView10.setVisibility(View.VISIBLE);
        textView11.setVisibility(View.VISIBLE);
        textView12.setVisibility(View.VISIBLE);
        textView23.setVisibility(View.VISIBLE);
        textView.setVisibility(View.VISIBLE);
    }
    public void disableAllFields(){
        btnEditSearchDoctor.setVisibility(View.GONE);
        btnDeleteSearchDoctor.setVisibility(View.GONE);
        etSearchDoctorName.setVisibility(View.GONE);
        etSearchDoctorID.setVisibility(View.GONE);
        etSearchDoctorPhone.setVisibility(View.GONE);
        etSearchDoctorAddress.setVisibility(View.GONE);
        etSearchDoctorHospitalName.setVisibility(View.GONE);
        etSearchDoctorCNIC.setVisibility(View.GONE);
        etSearchDoctorStatus.setVisibility(View.GONE);
        btnEditSearchDoctor.setVisibility(View.GONE);
        btnDeleteSearchDoctor.setVisibility(View.GONE);
        textView8.setVisibility(View.GONE);
        textView9.setVisibility(View.GONE);
        textView10.setVisibility(View.GONE);
        textView11.setVisibility(View.GONE);
        textView12.setVisibility(View.GONE);
        textView23.setVisibility(View.GONE);
        textView.setVisibility(View.GONE);

    }
}