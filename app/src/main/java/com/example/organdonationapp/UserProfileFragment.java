package com.example.organdonationapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
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
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserProfileFragment extends Fragment {
    DatabaseReference firebase;
    EditText tvUserProfileName,tvUserProfilePhone,tvUSerProfileEmail,tvUserProfilePassword,tvUserProfileType;
    Button btnEditUserProfile;
    String getType;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UserProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserProfileFragment newInstance(String param1, String param2) {
        UserProfileFragment fragment = new UserProfileFragment();
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
        View view =  inflater.inflate(R.layout.fragment_user_profile, container, false);

        intializeAllFields(view);

        setTextInAllFields();

        enableAllFields();

        editUser(view,getType);

        return view;

    }

    private void editUser(View view,String getType) {
        if(getType.equals("admin")){

        }
        else{
            disableAllFields();
        }

        btnEditUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            if (dataSnapshot.child("Email").getValue().toString().trim().equals(tvUSerProfileEmail.getText().toString().trim())) {
                                Map<String , Object> map = new HashMap<>();
                                map.put("Name",tvUserProfileName.getText().toString().trim());
                                map.put("Email",tvUSerProfileEmail.getText().toString().trim());
                                map.put("Phone",tvUserProfilePhone.getText().toString().trim());
                                map.put("Password",tvUserProfilePassword.getText().toString().trim());
                                map.put("Type",tvUserProfileType.getText().toString().trim());

                                dataSnapshot.getRef()
                                        .updateChildren(map)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(view.getContext(), "Success", Toast.LENGTH_SHORT).show();
                                            }
                                        });
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

    private void setTextInAllFields() {
        Intent intent = getActivity().getIntent();
        getType = intent.getStringExtra("Type");
        tvUserProfileName.setText(intent.getStringExtra("Name"));
        tvUserProfilePhone.setText(intent.getStringExtra("Phone"));
        tvUSerProfileEmail.setText(intent.getStringExtra("Email"));
        tvUserProfilePassword.setText(intent.getStringExtra("Password"));
        tvUserProfileType.setText(getType);
    }

    private void intializeAllFields(View view) {
        tvUSerProfileEmail = view.findViewById(R.id.tvUserProfileEmail);
        tvUserProfileName = view.findViewById(R.id.tvUserProfileName);
        tvUserProfilePhone = view.findViewById(R.id.tvUserProfilePhone);
        btnEditUserProfile = view.findViewById(R.id.btnEditUserProfile);
        tvUserProfilePassword = view.findViewById(R.id.tvUserProfilePassword);
        tvUserProfileType = view.findViewById(R.id.tvUserType);
        firebase = FirebaseDatabase.getInstance().getReference().child("Users");
    }

    public void enableAllFields(){
        tvUserProfileName.setEnabled(true);
        tvUserProfilePhone.setEnabled(true);
        tvUSerProfileEmail.setEnabled(true);
        tvUserProfilePassword.setEnabled(true);
        tvUserProfileType.setEnabled(true);
        btnEditUserProfile.setVisibility(View.VISIBLE);
    }
    public void disableAllFields(){ ;
        tvUserProfileType.setEnabled(false);
    }
    public void clearAllFields(){
        tvUserProfileName.setText("");
        tvUserProfilePhone.setText("");
        tvUSerProfileEmail.setText("");
        tvUserProfilePassword.setText("");
        tvUserProfileType.setText("");
    }
}