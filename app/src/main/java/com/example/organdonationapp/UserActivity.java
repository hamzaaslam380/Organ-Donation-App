package com.example.organdonationapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserActivity extends AppCompatActivity {
    Toolbar toolbar;
    ViewPager viewPager;
    TabLayout tabLayout;

    public UserActivity activity;

    Context context;

    String getType,getName,getPhone,getEmail,getPassword;

    Fragment UserProfileFragment;
    Fragment ViewAllUserFragment;
    Fragment addNewUserFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        activity = this;

        getUserType();

        sendDataToUserFragments();

        addFragmentsInTabs();
    }


    private void addFragmentsInTabs() {
        if(getType.equals("admin")){
            toolbar = findViewById(R.id.toolBarUser);
            setSupportActionBar(toolbar);

            viewPager = findViewById(R.id.viewPagerUser);
            tabLayout = findViewById(R.id.tabLayoutUser);

            UserProfileFragment = new UserProfileFragment();
            ViewAllUserFragment = new ViewAllUserFragment();
            addNewUserFragment = new addNewUserFragment();

            tabLayout.setupWithViewPager(viewPager);

            ViewPagerAdaptor viewPagerAdaptor = new ViewPagerAdaptor(getSupportFragmentManager(),0);
            viewPagerAdaptor.addTab(addNewUserFragment,"Add");
            viewPagerAdaptor.addTab(ViewAllUserFragment,"View");
            viewPagerAdaptor.addTab(UserProfileFragment,"Profile");

            viewPager.setAdapter(viewPagerAdaptor);

            tabLayout.getTabAt(0).setIcon(R.drawable.add);
            tabLayout.getTabAt(1).setIcon(R.drawable.view);
            tabLayout.getTabAt(2).setIcon(R.drawable.profile);

        }
        else{
            toolbar = findViewById(R.id.toolBarUser);
            setSupportActionBar(toolbar);

            viewPager = findViewById(R.id.viewPagerUser);
            tabLayout = findViewById(R.id.tabLayoutUser);

            UserProfileFragment = new UserProfileFragment();

            tabLayout.setupWithViewPager(viewPager);

            ViewPagerAdaptor viewPagerAdaptor = new ViewPagerAdaptor(getSupportFragmentManager(),0);
            viewPagerAdaptor.addTab(UserProfileFragment,"Profile");

            viewPager.setAdapter(viewPagerAdaptor);

            tabLayout.getTabAt(0).setIcon(R.drawable.profile);
        }

    }

    private void sendDataToUserFragments() {
        Intent intent2 = new Intent(UserActivity.this,UserProfileFragment.class);
        intent2.putExtra("Type",getType);
        intent2.putExtra("Name",getName);
        intent2.putExtra("Email",getEmail);
        intent2.putExtra("Phone",getPhone);
        intent2.putExtra("Password",getPassword);
    }

    private void getUserType() {
        Intent intent = getIntent();
        getType = intent.getStringExtra("Type");
        getName = intent.getStringExtra("Name");
        getEmail = intent.getStringExtra("Email");
        getPhone = intent.getStringExtra("Phone");
        getPassword = intent.getStringExtra("Password");

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


    private class ViewPagerAdaptor extends FragmentPagerAdapter {

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