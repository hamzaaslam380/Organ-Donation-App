package com.example.organdonationapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class ViewAllDonorFragment extends Fragment {
    RecyclerView donorRecyclerView;
    DonorAdaptor donorAdaptor;
    DatabaseReference firebase;
    EditText etSearchOrganPart;
    ImageView ivSearchOrganPart;
    final int result = 1;
    String getType,organ_part="no";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ViewAllDonorFragment() {
        // Required empty public constructor
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
        View view = inflater.inflate(R.layout.fragment_view_all_donor, container, false);

        etSearchOrganPart = (EditText) view.findViewById(R.id.etSearchOrganPart);
        ivSearchOrganPart = (ImageView) view.findViewById(R.id.ivSearchOrganPart);

        donorRecyclerView = (RecyclerView) view.findViewById(R.id.donorRecylcerView);
        donorRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        donorRecyclerView.setHasFixedSize(true);

        Intent intent = getActivity().getIntent();
        getType = intent.getStringExtra("Type");

        ivSearchOrganPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOrganStatus(view);
            }
        });


        firebase = FirebaseDatabase.getInstance().getReference().child("Donors");

        FirebaseRecyclerOptions<Donor> options =
                new FirebaseRecyclerOptions.Builder<Donor>()
                        .setQuery(firebase, Donor.class)
                        .build();

        donorAdaptor = new DonorAdaptor(options,view.getContext());

        donorRecyclerView.setAdapter(donorAdaptor);


        if(getType!=null){
            donorAdaptor.notifyDataSetChanged();
            donorAdaptor.startListening();
        }

        return view;
    }

    public void getOrganStatus(View view) {

        DatabaseReference donors;
        donors = FirebaseDatabase.getInstance().getReference().child("Donors");

        donors.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (etSearchOrganPart.getText().toString().trim().isEmpty() == false) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        if (dataSnapshot.child("organ_part").getValue().toString().trim().equals(etSearchOrganPart.getText().toString().trim())) {
                            Intent intent = new Intent(view.getContext(), ViewAllDonorByPartActivity.class);
                            intent.putExtra("Organ", etSearchOrganPart.getText().toString().trim());
                            intent.putExtra("Type", getType);
                            startActivityForResult(intent, result);
                            organ_part = "true";
                            break;
                        }
                        else {
                            organ_part = "false";
                        }
                    }
                    if (organ_part.equals("false")) {
                        etSearchOrganPart.setError("Sorry We Dont Find Any Donor of " + etSearchOrganPart.getText().toString().trim());
                    }
                }
                else{
                    etSearchOrganPart.setError("Please Enter Data ");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        donorAdaptor.startListening();


    }

    @Override
    public void onStop() {
        super.onStop();
        //donorAdaptor.stopListening();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==result){
            if(resultCode==RESULT_OK) {
                donorAdaptor.notifyDataSetChanged();
                donorAdaptor.startListening();
                ViewAllDonorFragment.this.getActivity().finish();
            }
            else{
                Intent intent = new Intent(this.getContext(),DonorActivity.class);
                intent.putExtra("Type",getType);
                startActivity(intent);
                ViewAllDonorFragment.this.getActivity().finish();
            }
        }
    }
}