package com.oc.liza.moodtrackeroc;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.oc.liza.moodtrackeroc.model.ScreenSlide;
import com.oc.liza.moodtrackeroc.model.VerticalViewPager;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private VerticalViewPager mViewPager;
    private ScreenSlide mAdapter;
    private ImageButton mCommentBtn;
    private ImageButton mHistoryBtn;
    private String mComment = "";
    private Date mCurrentTime;
    private Mood mood;
    private List<Mood> mMoodList = new ArrayList<>();
    private String filename = "moodsList.txt";
    private FileOutputStream outputStream;
    private FileInputStream fis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //fetch saved mood list
        readFile();
        mMoodList.clear();


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

                save();
                mComment="";

                Intent history = new Intent(MainActivity.this, History.class);
                history.putExtra("mMoodList", (Serializable) mMoodList);
                startActivity(history);
            }
        });

        // Slide up and down function
        mViewPager = findViewById(R.id.pager);
        mAdapter = new ScreenSlide(this);
        mViewPager.setAdapter(mAdapter);

        //Put the happy smiley as first image when app is launched
        mViewPager.setCurrentItem(3);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            public void onPageSelected(int position) {
                }
        });
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

    public void save() {
        int position= mViewPager.getCurrentItem();
        mCurrentTime = Calendar.getInstance().getTime();
        mood=new Mood(position, mCurrentTime, mComment);
        mMoodList.add(mood);
        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(outputStream);
            oos.writeObject(mMoodList);

            oos.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readFile(){
        try (
                FileInputStream fis= openFileInput(filename);
             BufferedInputStream bis = new BufferedInputStream(fis);
             ObjectInputStream ois = new ObjectInputStream(bis) ) {
             mMoodList= (List<Mood>) ois.readObject();

            ois.close();
            bis.close();
            fis.close();
        }	catch (FileNotFoundException e) {
            System.err.println("Aucun humeur sauvegardé ! ");
        } catch (IOException e) {
            System.err.println("Erreur de lecture du fichier de sauvegarde !");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


}


