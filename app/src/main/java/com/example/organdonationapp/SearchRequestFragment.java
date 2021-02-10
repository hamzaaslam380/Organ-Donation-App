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

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchRequestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchRequestFragment extends Fragment {
    EditText etGetUserRequest, etRequestEmail, etRequestCNIC, etRequestPhone, etRequestText;
    Button btnSearchRequest, btnEditRequest, btnDeleteRequest;
    TextView textView27;
    TextView textView28;
    TextView textView29;
    TextView textView30;
    DatabaseReference firebase;
    String getType;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SearchRequestFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchRequestFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchRequestFragment newInstance(String param1, String param2) {
        SearchRequestFragment fragment = new SearchRequestFragment();
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
        View view = inflater.inflate(R.layout.fragment_search_request, container, false);

        intializeAllFields(view);

        getUserType();

        disableAllFields();

        searchDoctor();

        deleteDoctor(view);

        editDoctor(view);

        return view;
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


    private void editDoctor(View view) {
        btnEditRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(etRequestCNIC.getText().toString().trim().isEmpty()){etRequestCNIC.setError("Enter CNIC");}
                        if(etRequestEmail.getText().toString().trim().isEmpty()){etRequestEmail.setError("Enter Email");}
                        if(etRequestPhone.getText().toString().trim().isEmpty()){etRequestPhone.setError("Enter Phone Number");}
                        if(etRequestText.getText().toString().trim().isEmpty()){etRequestText.setError("This Field Cant be Empty ");}
                        else{
                            for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                if (dataSnapshot.child("CNIC").getValue().toString().trim().equals(etRequestCNIC.getText().toString().trim())) {

                                    Map<String, Object> map = new HashMap<>();
                                    map.put("CNIC", etRequestCNIC.getText().toString().trim());
                                    map.put("phone", etRequestPhone.getText().toString().trim());
                                    map.put("email", etRequestEmail.getText().toString().trim());
                                    map.put("request", etRequestText.getText().toString().trim());

                                    dataSnapshot.getRef()
                                            .updateChildren(map)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Intent intent = new Intent(view.getContext(),ContactUsActivity.class);
                                                    intent.putExtra("Type",getType);
                                                    startActivity(intent);
                                                    clearAllFields();
                                                    disableAllFields();
                                                }
                                            });

                                    clearAllFields();
                                    disableAllFields();
                                }
                                else{
                                    etRequestCNIC.setError("Enter Correct CNIC Number");
                                }
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

    private void deleteDoctor(View view) {
        btnDeleteRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            if (dataSnapshot.child("CNIC").getValue().toString().trim().equals(etRequestCNIC.getText().toString().trim())) {
                                dataSnapshot.getRef()
                                        .removeValue()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Intent intent = new Intent(view.getContext(),ContactUsActivity.class);
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

    private void searchDoctor() {
        btnSearchRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etGetUserRequest.getText().toString().trim().isEmpty()){
                    etRequestCNIC.setError("Please Enter User CNIC");
                }
                else{
                    firebase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                if (dataSnapshot.child("CNIC").getValue().toString().trim().equals(etGetUserRequest.getText().toString().trim())) {
                                    enableAllFields();
                                    etRequestCNIC.setText(dataSnapshot.child("CNIC").getValue().toString().trim());
                                    etRequestPhone.setText(dataSnapshot.child("phone").getValue().toString().trim());
                                    etRequestEmail.setText(dataSnapshot.child("email").getValue().toString().trim());
                                    etRequestText.setText(dataSnapshot.child("request").getValue().toString().trim());
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

    private void intializeAllFields(View view) {
        etRequestCNIC = view.findViewById(R.id.etRequestCNIC);
        etRequestPhone = view.findViewById(R.id.etRequestPhone);
        etRequestEmail = view.findViewById(R.id.etRequestEmail);
        etRequestText = view.findViewById(R.id.etRequestText);
        etGetUserRequest = view.findViewById(R.id.etGetUserRequest);
        btnSearchRequest = view.findViewById(R.id.btnSearchRequest);
        btnEditRequest = view.findViewById(R.id.btnEditRequest);
        btnDeleteRequest = view.findViewById(R.id.btnDeleteRequest);
        textView27 = view.findViewById(R.id.textView27);
        textView28 = view.findViewById(R.id.textView28);
        textView29 = view.findViewById(R.id.textView29);
        textView30 = view.findViewById(R.id.textView30);
        firebase = FirebaseDatabase.getInstance().getReference().child("Requests");
    }

    private void clearAllFields() {
        etRequestPhone.setText("");
        etRequestEmail.setText("");
        etRequestCNIC.setText("");
        etRequestText.setText("");
    }

    public void disableAllFields(){
        etRequestText.setVisibility(View.GONE);
        etRequestCNIC.setVisibility(View.GONE);
        etRequestEmail.setVisibility(View.GONE);
        etRequestPhone.setVisibility(View.GONE);
        textView27.setVisibility(View.GONE);
        textView28.setVisibility(View.GONE);
        textView29.setVisibility(View.GONE);
        textView30.setVisibility(View.GONE);
        btnEditRequest.setVisibility(View.GONE);
        btnDeleteRequest.setVisibility(View.GONE);
    }

    public void enableAllFields(){
        etRequestText.setVisibility(View.VISIBLE);
        etRequestCNIC.setVisibility(View.VISIBLE);
        etRequestEmail.setVisibility(View.VISIBLE);
        etRequestPhone.setVisibility(View.VISIBLE);
        textView27.setVisibility(View.VISIBLE);
        textView28.setVisibility(View.VISIBLE);
        textView29.setVisibility(View.VISIBLE);
        textView30.setVisibility(View.VISIBLE);
        btnEditRequest.setVisibility(View.VISIBLE);
        btnDeleteRequest.setVisibility(View.VISIBLE);
    }
}