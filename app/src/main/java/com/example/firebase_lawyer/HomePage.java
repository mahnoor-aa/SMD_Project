package com.example.firebase_lawyer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

public class HomePage extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    ViewPagerAdap viewPagerAdap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        String lawyerId = getIntent().getStringExtra("LAWYER_ID");

        tabLayout=findViewById(R.id.tabLayout);
        viewPager2=findViewById(R.id.viewPager);
        viewPagerAdap=new ViewPagerAdap(this);
        viewPagerAdap.setLawyerId(lawyerId);
        viewPager2.setAdapter(viewPagerAdap);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            public void onTabSelected(TabLayout.Tab tab){
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
        });
    }
}