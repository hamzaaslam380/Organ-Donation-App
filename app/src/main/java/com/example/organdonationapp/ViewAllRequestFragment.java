package com.example.organdonationapp;

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
 * Use the {@link ViewAllRequestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewAllRequestFragment extends Fragment {
    RecyclerView recyclerView;
    RequestAdaptor requestAdaptor;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ViewAllRequestFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ViewAllRequestFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewAllRequestFragment newInstance(String param1, String param2) {
        ViewAllRequestFragment fragment = new ViewAllRequestFragment();
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
        View view = inflater.inflate(R.layout.fragment_view_all_request, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.requestRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        recyclerView.setHasFixedSize(true);

        FirebaseRecyclerOptions<Request> options =
                new FirebaseRecyclerOptions.Builder<Request>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Requests"), Request.class)
                        .build();

        requestAdaptor = new RequestAdaptor(options,this.getActivity());

        recyclerView.setAdapter(requestAdaptor);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        requestAdaptor.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        requestAdaptor.stopListening();
    }
}