package com.oc.liza.moodtrackeroc;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {


    VerticalViewPager viewPager;
    ScreenSlide adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        viewPager = findViewById(R.id.pager);
        adapter = new ScreenSlide(this);
        viewPager.setAdapter(adapter);

    }

}







