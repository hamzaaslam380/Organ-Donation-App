package com.example.organdonationapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DoctorActivity extends AppCompatActivity {
    Toolbar toolbar;
    ViewPager viewPager;
    TabLayout tabLayout;

    Fragment ViewAllDoctorFragment;
    Fragment AddNewDoctorFragment;
    Fragment SearchDoctorFragment;

    String getType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);

        getUserType();

        addFragmentInTabs();

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
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addFragmentInTabs() {

        toolbar = findViewById(R.id.toolBarDoctor);
        setSupportActionBar(toolbar);

        viewPager = findViewById(R.id.viewPagerDoctor);
        tabLayout = findViewById(R.id.tabLayoutDoctor);

        ViewAllDoctorFragment = new ViewAllDoctorFragment();
        AddNewDoctorFragment = new AddNewDoctorFragment();
        SearchDoctorFragment = new SearchDoctorFragment();

        tabLayout.setupWithViewPager(viewPager);

        DoctorActivity.ViewPagerAdaptor viewPagerAdaptor = new DoctorActivity.ViewPagerAdaptor(getSupportFragmentManager(),0);
        viewPagerAdaptor.addTab(AddNewDoctorFragment,"Add");
        viewPagerAdaptor.addTab(ViewAllDoctorFragment,"View");
        viewPagerAdaptor.addTab(SearchDoctorFragment,"Search");

        viewPager.setAdapter(viewPagerAdaptor);

        tabLayout.getTabAt(0).setIcon(R.drawable.add);
        tabLayout.getTabAt(1).setIcon(R.drawable.view);
        tabLayout.getTabAt(2).setIcon(R.drawable.search);
    }

    public class ViewPagerAdaptor extends FragmentPagerAdapter {

        List<Fragment> fragments = new ArrayList<>();
        List<String> titles = new ArrayList<>();

        public ViewPagerAdaptor(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }


        public void addTab(Fragment f,String title){
            fragments.add(f);
            titles.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

    }
}