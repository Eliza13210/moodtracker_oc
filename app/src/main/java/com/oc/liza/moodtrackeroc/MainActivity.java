package com.oc.liza.moodtrackeroc;


import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.oc.liza.moodtrackeroc.model.Mood;
import com.oc.liza.moodtrackeroc.model.ScreenSlide;
import com.oc.liza.moodtrackeroc.model.VerticalViewPager;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {


    private VerticalViewPager mViewPager;
    private ScreenSlide mAdapter;
    private TextView mTxt;
    private Date mCurrentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewPager = findViewById(R.id.pager);
        mAdapter = new ScreenSlide(this);

        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(3);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            public void onPageSelected(int position) {
                // Check if this is the page you want.
                getMood();
            }
        });


        mCurrentTime = Calendar.getInstance().getTime();
        //at midnight save


    }

    private int getMood(){
        int position = mViewPager.getCurrentItem();

        return position;

    }

    private void saveMood(){
        int position=getMood();
        Mood mood=new Mood(position);

    }

}


