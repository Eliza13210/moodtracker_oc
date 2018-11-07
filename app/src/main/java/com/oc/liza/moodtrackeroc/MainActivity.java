package com.oc.liza.moodtrackeroc;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.oc.liza.moodtrackeroc.model.Mood;
import com.oc.liza.moodtrackeroc.model.MoodList;
import com.oc.liza.moodtrackeroc.model.ScreenSlide;
import com.oc.liza.moodtrackeroc.model.VerticalViewPager;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {


    private VerticalViewPager mViewPager;
    private ScreenSlide mAdapter;
    private ImageButton mCommentBtn;
    private ImageButton mHistoryBtn;
    private String mComment = "";
    private Date mCurrentTime;
    private MoodList mMoodList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMoodList=new MoodList();

        //Button to add a comment
        mCommentBtn=findViewById(R.id.commentButton);
        mCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentPopUp();

            }
        });

        //Button to access history
        mHistoryBtn=findViewById(R.id.historyButton);
        mHistoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //Here I put the slide up and down function
        mViewPager = findViewById(R.id.pager);
        mAdapter = new ScreenSlide(this);
        mViewPager.setAdapter(mAdapter);

        //Put the happy smiley as first image when app is launched
        mViewPager.setCurrentItem(3);


        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            public void onPageSelected(int position) {
                int pos=getMood();
                Mood mood=new Mood(pos, mCurrentTime);

                TextView tv=findViewById(R.id.txt);
                tv.setText("saved");
            }
        });


        mCurrentTime = Calendar.getInstance().getTime();
        //at midnight save


    }

    private int getMood(){
        int position = mViewPager.getCurrentItem();
        return position;
    }

    //Comment pop up dialog
    private void commentPopUp(){
    AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.Theme_AppCompat_Light_Dialog_Alert);
    builder.setTitle("Commentaire");

    final EditText input = new EditText(this);
    input.setTextColor(Color.BLACK);
        input.setInputType(InputType.TYPE_CLASS_TEXT);

        builder.setView(input);

    // Set up the buttons
    builder.setPositiveButton("Valider", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

            mComment = input.getText().toString();
            dialog.dismiss();
        }
    });
    builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
        }
    });

        builder.show();
    }


}


