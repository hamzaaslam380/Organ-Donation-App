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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class DonorActivity extends AppCompatActivity {
    Toolbar toolbar;
    ViewPager viewPager;
    TabLayout tabLayout;

    Fragment ViewAllDonorFragment;
    Fragment AddNewDonorFragment;
    Fragment SearchDonorFragment;

    String getType = " ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor);

        Intent intent = getIntent();
        getType = intent.getStringExtra("Type");

        sendDataToDonorFragments();

        addFragmentsInTabs();
    }

    private void addFragmentsInTabs() {
        toolbar = findViewById(R.id.toolBarDonor);
        setSupportActionBar(toolbar);

        viewPager = findViewById(R.id.viewPagerDonor);
        tabLayout = findViewById(R.id.tabLayoutDonor);

        ViewAllDonorFragment = new ViewAllDonorFragment();
        AddNewDonorFragment = new AddNewDonorFragment();
        SearchDonorFragment = new SearchDonorFragment();

        tabLayout.setupWithViewPager(viewPager);

        DonorActivity.ViewPagerAdaptor viewPagerAdaptor = new DonorActivity.ViewPagerAdaptor(getSupportFragmentManager(),0);
        viewPagerAdaptor.addTab(AddNewDonorFragment,"Add");
        viewPagerAdaptor.addTab(ViewAllDonorFragment,"View");
        viewPagerAdaptor.addTab(SearchDonorFragment,"Search");

        viewPager.setAdapter(viewPagerAdaptor);

        tabLayout.getTabAt(0).setIcon(R.drawable.add);
        tabLayout.getTabAt(1).setIcon(R.drawable.view);
        tabLayout.getTabAt(2).setIcon(R.drawable.search);
    }

    private void sendDataToDonorFragments() {
        Intent intent2 = new Intent(DonorActivity.this,SearchDonorFragment.class);
        intent2.putExtra("Type",getType);

        Intent intent3 = new Intent(DonorActivity.this,ViewAllDonorFragment.class);
        intent3.putExtra("Type",getType);

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