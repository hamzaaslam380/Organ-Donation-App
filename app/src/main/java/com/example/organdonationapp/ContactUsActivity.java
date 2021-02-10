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

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ContactUsActivity extends AppCompatActivity {
    Toolbar toolbar;
    ViewPager viewPager;
    TabLayout tabLayout;

    Fragment ViewAllRequestFragment;
    Fragment AddNewRequestFragment;
    Fragment SearchRequestFragment;

    String getType,getPhone,getEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        Intent intent = getIntent();
        getType = intent.getStringExtra("Type");
        getPhone = intent.getStringExtra("Phone");
        getEmail = intent.getStringExtra("Email");

        sentUserTypeToRequestFragments();

        if(getType.equals("admin")) {
            adminRights();
        }
        else {
            userRights();
        }

    }

    private void sentUserTypeToRequestFragments() {
        Intent intent2 = new Intent(ContactUsActivity.this,AddNewRequestFragment.class);
        intent2.putExtra("Type",getType);
        intent2.putExtra("Phone",getPhone);
        intent2.putExtra("Email",getEmail);
    }

    private void userRights() {
        toolbar = findViewById(R.id.toolBarContactUs);
        setSupportActionBar(toolbar);

        viewPager = findViewById(R.id.viewPagerContactUs);
        tabLayout = findViewById(R.id.tabLayoutContactUs);

        ViewAllRequestFragment = new ViewAllRequestFragment();
        AddNewRequestFragment = new AddNewRequestFragment();

        tabLayout.setupWithViewPager(viewPager);

        ContactUsActivity.ViewPagerAdaptor viewPagerAdaptor = new ContactUsActivity.ViewPagerAdaptor(getSupportFragmentManager(), 0);
        viewPagerAdaptor.addTab(AddNewRequestFragment, "Add");
        viewPager.setAdapter(viewPagerAdaptor);

        tabLayout.getTabAt(0).setIcon(R.drawable.add);
    }

    private void adminRights() {
        toolbar = findViewById(R.id.toolBarContactUs);
        setSupportActionBar(toolbar);

        viewPager = findViewById(R.id.viewPagerContactUs);
        tabLayout = findViewById(R.id.tabLayoutContactUs);

        ViewAllRequestFragment = new ViewAllRequestFragment();
        AddNewRequestFragment = new AddNewRequestFragment();
        SearchRequestFragment = new SearchRequestFragment();

        tabLayout.setupWithViewPager(viewPager);

        ContactUsActivity.ViewPagerAdaptor viewPagerAdaptor = new ContactUsActivity.ViewPagerAdaptor(getSupportFragmentManager(), 0);
        viewPagerAdaptor.addTab(AddNewRequestFragment, "Add");
        viewPagerAdaptor.addTab(ViewAllRequestFragment, "View");
        viewPagerAdaptor.addTab(SearchRequestFragment, "Search");

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