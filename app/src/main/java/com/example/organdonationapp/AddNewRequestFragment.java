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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddNewRequestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddNewRequestFragment extends Fragment {
    EditText etContactUsCNIC,etContactUsComplain;
    Button btnContactUs;
    DatabaseReference firebase;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddNewRequestFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddNewRequestFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddNewRequestFragment newInstance(String param1, String param2) {
        AddNewRequestFragment fragment = new AddNewRequestFragment();
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
        View view = inflater.inflate(R.layout.fragment_add_new_request, container, false);

        intializeAllFields(view);

        addRequest();

        return view;
    }

    private void intializeAllFields(View view) {
        etContactUsCNIC = view.findViewById(R.id.etContactUsCNIC);
        etContactUsComplain = view.findViewById(R.id.etContactUsComplain);
        btnContactUs = view.findViewById(R.id.btnContactUs);
        firebase = FirebaseDatabase.getInstance().getReference().child("Requests");
    }

    private void addRequest() {
        btnContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etContactUsCNIC.getText().toString().trim().isEmpty()) {
                    etContactUsCNIC.setError("Please Enter Your CNIC");
                } else if (etContactUsComplain.getText().toString().trim().isEmpty()) {
                    etContactUsComplain.setError("If You Want To Register A Complain This Field Cant Be Emppty");
                } else {

                    Intent intent = getActivity().getIntent();
                    String getEmail = intent.getStringExtra("Email");
                    String getPhone = intent.getStringExtra("Phone");

                    HashMap<String, String> post = new HashMap<>();
                    post.put("CNIC", etContactUsCNIC.getText().toString().trim());
                    post.put("request", etContactUsComplain.getText().toString().trim());
                    post.put("email", getEmail);
                    post.put("phone", getPhone);

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
            }
        });

    }

    public void resetAllFields(){
        etContactUsComplain.setText("");
        etContactUsCNIC.setText("");
    }
}