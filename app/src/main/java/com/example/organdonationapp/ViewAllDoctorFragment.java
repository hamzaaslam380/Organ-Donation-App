package com.example.organdonationapp;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewAllDoctorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewAllDoctorFragment extends Fragment {
    RecyclerView doctorRecyclerView;
    DoctorAdaptor doctorAdaptor;
    Context context;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ViewAllDoctorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ViewAllDoctorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewAllDoctorFragment newInstance(String param1, String param2) {
        ViewAllDoctorFragment fragment = new ViewAllDoctorFragment();
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
       View view = inflater.inflate(R.layout.fragment_view_all_doctor, container, false);

        doctorRecyclerView = (RecyclerView) view.findViewById(R.id.doctorRecyclerView);
        doctorRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        doctorRecyclerView.setHasFixedSize(true);

        FirebaseRecyclerOptions<Doctor> options =
                new FirebaseRecyclerOptions.Builder<Doctor>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Doctors"), Doctor.class)
                        .build();

        doctorAdaptor = new DoctorAdaptor(options,this.getActivity());

        doctorRecyclerView.setAdapter(doctorAdaptor);

        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
       doctorAdaptor.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
       // doctorAdaptor.stopListening();
    }
}