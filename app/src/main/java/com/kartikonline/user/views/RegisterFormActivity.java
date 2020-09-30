package com.kartikonline.user.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.kartikonline.user.Adapters.RegisterFormViewpagerAdapter;
import com.kartikonline.user.R;
import com.kartikonline.user.views.fragments.AddressDetail;
import com.kartikonline.user.views.fragments.CompanyDetail;

import java.util.ArrayList;

public class RegisterFormActivity extends AppCompatActivity {

    Toolbar toolbar;
    ViewPager mViewPager;
    TabLayout tabLayout;

    ArrayList<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_form);

        fragments = new ArrayList<>();

        toolbar = findViewById(R.id.toolbar);
        tabLayout = findViewById(R.id.tabs);
        mViewPager = findViewById(R.id.v_pager_form);

        initActionBar();

        fragments.add(new AddressDetail());
        fragments.add(new CompanyDetail());

        RegisterFormViewpagerAdapter formTabs = new RegisterFormViewpagerAdapter(getSupportFragmentManager(),this, fragments);
        mViewPager.setAdapter(formTabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.getTabAt(0).setText("Company");
        tabLayout.getTabAt(1).setText("Address");


    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void initActionBar(){
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Enter Details");
        }
    }
}
