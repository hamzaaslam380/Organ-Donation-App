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
import android.widget.Toast;

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
 * Use the {@link addNewUserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class addNewUserFragment extends Fragment {
    EditText etFragAddUserName,etFragAddUserEmail,etFragAddUserPhone,etFragAddUserPass,etFragAddUserConfirmPass;
    Button btnAddUserFrag;
    DatabaseReference firebase;
    DatabaseReference user;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public addNewUserFragment() {
        // Required empty public constructor
    }

    public static addNewUserFragment newInstance(String param1, String param2) {
        addNewUserFragment fragment = new addNewUserFragment();
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

        View view = inflater.inflate(R.layout.fragment_add_new_user, container, false);

        intializeAllFields(view);

        addUser();

        return view;
    }

    private void intializeAllFields(View view) {
        etFragAddUserName = view.findViewById(R.id.etFragAddUserName);
        etFragAddUserEmail = view.findViewById(R.id.etFragAddUserEmail);
        etFragAddUserPhone = view.findViewById(R.id.etFragAddUserPhone);
        etFragAddUserPass = view.findViewById(R.id.etFragAddUserPass);
        etFragAddUserConfirmPass = view.findViewById(R.id.etFragAddUserConfirmPass);
        btnAddUserFrag = view.findViewById(R.id.btnAddUserFrag);
        firebase = FirebaseDatabase.getInstance().getReference().child("Users");
    }

    private void addUser() {
        btnAddUserFrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etFragAddUserName.getText().toString().isEmpty()){
                    etFragAddUserName.setError("Please Enter Your Email ");
                }else if(etFragAddUserPhone.getText().toString().isEmpty()){
                    etFragAddUserPhone.setError("Please Enter Your Phone Number");
                }else if(etFragAddUserEmail.getText().toString().isEmpty()){
                    etFragAddUserEmail.setError("Please Enter Your Email");
                }else if(etFragAddUserPass.getText().toString().isEmpty()){
                    etFragAddUserPass.setError("Please Enter Your Password");
                }else if(etFragAddUserConfirmPass.getText().toString().isEmpty()){
                    etFragAddUserConfirmPass.setError("Please Enter Re Enter Your Password");
                }
                else if(etFragAddUserConfirmPass.getText().toString().equals(etFragAddUserPass.getText().toString())){
                    final int[] yes = {0};
                    user = FirebaseDatabase.getInstance().getReference().child("Users");
                    user.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                if (dataSnapshot.child("Email").getValue().toString().trim().equals(etFragAddUserEmail.getText().toString().trim())) {
                                    yes[0] = 1;
                                    break;
                                }
                            }
                            if(yes[0] == 1){
                                etFragAddUserEmail.setError("Email Exist");
                            }
                            else{

                                HashMap<String,String> post = new HashMap<>();

                                post.put("Name",etFragAddUserName.getText().toString().trim());
                                post.put("Email",etFragAddUserEmail.getText().toString().trim());
                                post.put("Phone",etFragAddUserPhone.getText().toString().trim());
                                post.put("Password",etFragAddUserConfirmPass.getText().toString().trim());
                                post.put("Type","user");


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
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

    }

    private void resetAllFields() {
        etFragAddUserName.setText(" ");
        etFragAddUserEmail.setText(" ");
        etFragAddUserPhone.setText(" ");
        etFragAddUserPass.setText(" ");
        etFragAddUserConfirmPass.setText(" ");
    }

}