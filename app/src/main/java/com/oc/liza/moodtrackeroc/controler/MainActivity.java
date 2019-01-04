package com.oc.liza.moodtrackeroc.controler;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.oc.liza.moodtrackeroc.R;
import com.oc.liza.moodtrackeroc.model.Mood;
import com.oc.liza.moodtrackeroc.utils.CommentPopUp;
import com.oc.liza.moodtrackeroc.utils.MoodListManager;
import com.oc.liza.moodtrackeroc.utils.SharePopUp;
import com.oc.liza.moodtrackeroc.view.ScreenSlide;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;
    private Mood mMood;
    private MoodListManager mMoodListManager;
    private final Context ctx = this;
    private SharePopUp mSharePopUp;

    @BindView(R.id.historyButton)
    ImageButton historyBtn;
    @BindView(R.id.pager)
    ViewPager mViewPager;
    @BindView(R.id.commentButton)
    ImageButton commentBtn;
    @BindView(R.id.shareButton)
    ImageButton shareBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initButtons();
        initSlideScreen();
    }

    private void initButtons() {
        mMoodListManager = new MoodListManager(this);

        // Button to add a comment

        commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int mood = mViewPager.getCurrentItem();
                new CommentPopUp(ctx, mood);

            }
        });


        // Button to share your mood by SMS or e-mail

        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<Mood> moods = mMoodListManager.getMoodList();
                mMood = moods.get(moods.size() - 1);
                mSharePopUp = new SharePopUp(ctx, mMood);

            }
        });


        // Button to access history

        historyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, History.class));
            }
        });

    }

    private void initSlideScreen() {
        // Set slide up and down function

        ScreenSlide adapter = new ScreenSlide(this);
        mViewPager.setAdapter(adapter);

        //Put the happy smiley as first image when app is launched
        mViewPager.setCurrentItem(3);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            //when page has changed, save mood
            @Override
            public void onPageSelected(int i) {
                createMood();
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });

    }

    /**
     * Create a new mood and save it in the mood list
     */
    private void createMood() {
        Calendar c = Calendar.getInstance();
        int mood = mViewPager.getCurrentItem();
        mMood = new Mood(mood, c, null);
        mMoodListManager.addMood(mMood);
    }

    /**
     * Ask for permission to send SMS
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                // permission was granted
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mSharePopUp.permissionGranted();
                }
                // permission denied
                Toast.makeText(this, "Vous n'avez pas autorisé l'application d'envoyer des SMS", Toast.LENGTH_SHORT).show();
            }
        }
    }
}


