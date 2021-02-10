package com.example.organdonationapp;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.ListFragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class ViewAllDonorByPartActivity extends AppCompatActivity {
    ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_donor_by_part);

        ivBack = findViewById(R.id.ivBack);

        Intent intent = getIntent();
        String getType = intent.getStringExtra("Type");

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewAllDonorByPartActivity.this,DonorActivity.class);
                intent.putExtra("Type",getType);
                startActivity(intent);
                ViewAllDonorByPartActivity.this.finish();
            }
        });

        ViewAllDonorByPartFragment fragmentDemo = (ViewAllDonorByPartFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);

    }
}