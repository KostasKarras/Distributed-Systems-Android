package com.example.uni_tok;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TabHost;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class runUser extends AppCompatActivity implements View.OnClickListener {

    TabHost tab;
    ViewPager viewPager;

    ImageButton profile_button;
    ImageButton exit_button;
    ImageButton home_button;
    ImageButton search_button;
    ImageButton upload_button;
    EditText search_bar;

    /*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page_activity);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Videos"));
        tabLayout.addTab(tabLayout.newTab().setText("Search"));
        tabLayout.addTab(tabLayout.newTab().setText("Upload"));
        tabLayout.addTab(tabLayout.newTab().setText("Channel"));

        viewPager = (ViewPager)findViewById(R.id.viewPager);

        MyAdapter adapter = new MyAdapter(this,getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
   */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page_activity);

        profile_button = (ImageButton)findViewById(R.id.profileButton);
        exit_button = (ImageButton)findViewById(R.id.exitButton);
        home_button = (ImageButton)findViewById(R.id.homeButton);
        search_button = (ImageButton)findViewById(R.id.search_button);
        upload_button = (ImageButton)findViewById(R.id.uploadButton);
        search_bar = (EditText)findViewById(R.id.search_bar);

        profile_button.setOnClickListener(this);
        exit_button.setOnClickListener(this);
        home_button.setOnClickListener(this);
        search_button.setOnClickListener(this);
        upload_button.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.profileButton:
                intent = new Intent(this, ChannelFragment.class);
                startActivity(intent);
                break;
            case R.id.exitButton:
                break;
            case R.id.homeButton:
                break;
            case R.id.search_button:
                String topic = search_bar.getText().toString();
                intent = new Intent(this, SearchFragment.class);
                startActivity(intent);
                break;
            case R.id.uploadButton:
                break;
        }

    }

    private void channelFragment() {
    }
}