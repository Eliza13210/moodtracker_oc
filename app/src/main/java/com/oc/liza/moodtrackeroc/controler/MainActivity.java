package com.oc.liza.moodtrackeroc.controler;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;

import com.oc.liza.moodtrackeroc.R;
import com.oc.liza.moodtrackeroc.model.Mood;
import com.oc.liza.moodtrackeroc.utils.CommentPopUp;
import com.oc.liza.moodtrackeroc.utils.MoodListManager;
import com.oc.liza.moodtrackeroc.utils.SharePopUp;
import com.oc.liza.moodtrackeroc.view.ScreenSlide;
import com.oc.liza.moodtrackeroc.view.VerticalViewPager;

import java.util.Calendar;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private VerticalViewPager mViewPager;
    private String mComment = "";
    private Mood mMood;
    private MoodListManager mMoodListManager;
    private final Context ctx = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initButtons();
        initSlideScreen();
        mMoodListManager = new MoodListManager(this);
    }

    private void initButtons() {

        //Button to add a comment
        ImageButton commentBtn = findViewById(R.id.commentButton);
        commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int mood=mViewPager.getCurrentItem();
                CommentPopUp popUp=new CommentPopUp(ctx, mood);

            }
        });

        //Button to share your mood by SMS
        ImageButton shareBtn = findViewById(R.id.shareButton);
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentItem = mViewPager.getCurrentItem();
                Calendar date = Calendar.getInstance();
                mMood = new Mood(currentItem, date, mComment);
                mMoodListManager.addMood(mMood);
                SharePopUp mSharePopUp = new SharePopUp(ctx, mMood);

            }
        });

        //Button to access history
        ImageButton historyBtn = findViewById(R.id.historyButton);
        historyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent history = new Intent(MainActivity.this, History.class);
                startActivity(history);
            }
        });

    }

    private void initSlideScreen() {
        // Set slide up and down function
        mViewPager = findViewById(R.id.pager);
        ScreenSlide adapter = new ScreenSlide(this);
        mViewPager.setAdapter(adapter);

        //Put the happy smiley as first image when app is launched
        mViewPager.setCurrentItem(3);
    }

}


