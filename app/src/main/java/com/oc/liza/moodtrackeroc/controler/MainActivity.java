package com.oc.liza.moodtrackeroc.controler;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.oc.liza.moodtrackeroc.R;
import com.oc.liza.moodtrackeroc.model.Mood;
import com.oc.liza.moodtrackeroc.view.ScreenSlide;
import com.oc.liza.moodtrackeroc.view.VerticalViewPager;

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
    private Calendar mCurrentTime;
    private Mood mMood;
    private List<Mood> mMoodList = new ArrayList<>();
    private String mFilename = "moodsList.txt";
    private FileOutputStream mOutputStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //fetch saved mood list
        readFile();

        /*Try to add these moods to the list to test if history is showed correctly
        mMoodList.clear();

        Calendar c= Calendar.getInstance();
        c.add(Calendar.DATE, -7);
        mMoodList.add(new Mood(0,c , "5 nov"));
        c= Calendar.getInstance();
        c.add(Calendar.DATE, -6);
        mMoodList.add(new Mood(1,c , "6 nov"));
        c= Calendar.getInstance();
        c.add(Calendar.DATE, -5);
        mMoodList.add(new Mood(2,c , "7 nov"));
        c= Calendar.getInstance();
        c.add(Calendar.DATE, -4);
        mMoodList.add(new Mood(3,c , "8 nov"));
        c= Calendar.getInstance();
        c.add(Calendar.DATE, -3);
        mMoodList.add(new Mood(4,c , "9 nov"));
        c= Calendar.getInstance();
        c.add(Calendar.DATE, -2);
        mMoodList.add(new Mood(1,c , "10 nov"));
        c= Calendar.getInstance();
        c.add(Calendar.DATE, -1);
        mMoodList.add(new Mood(2,c , "11 nov"));
        c=Calendar.getInstance();
        c.add(Calendar.DATE,-8);

        //This one will be removed when saved() function is called
        mMoodList.add(new Mood(0,c, "Il y a 8 jours"));
        save();
        */

        //Button to add a comment
        mCommentBtn = findViewById(R.id.commentButton);
        mCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentPopUp();
            }
        });

        //Button to access history
        mHistoryBtn = findViewById(R.id.historyButton);
        mHistoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //checkDate();
                Intent history = new Intent(MainActivity.this, History.class);
                history.putExtra("mMoodList", (Serializable) mMoodList);
                startActivity(history);
            }
        });

        // Set slide up and down function
        mViewPager = findViewById(R.id.pager);
        mAdapter = new ScreenSlide(this);
        mViewPager.setAdapter(mAdapter);

        //Put the happy smiley as first image when app is launched
        mViewPager.setCurrentItem(3);
    }

    //Comment pop up dialog
    private void commentPopUp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog_Alert);
        builder.setTitle("Commentaire");

        final EditText input = new EditText(this);
        input.setTextColor(Color.BLACK);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Valider", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                mComment = input.getText().toString();
                checkDate();
                sharePopUp();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.cancel();
            }
        });
        builder.show();
    }
//Share mood with a friend::::
    private void sharePopUp() {
        AlertDialog.Builder buildShare = new AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog_Alert);
        buildShare.setTitle("Partagez votre humeur avec un ami?");

        final EditText inputShare = new EditText(this);
        inputShare.setTextColor(Color.BLACK);
        inputShare.setInputType(InputType.TYPE_CLASS_PHONE);
        buildShare.setView(inputShare);

        // Set up the buttons
        buildShare.setPositiveButton("Valider", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                String numero = inputShare.getText().toString();

                try {
                    if (numero.length() >= 6 && mMood.toString().length() > 0) {
                        SmsManager.getDefault().sendTextMessage(numero, null, mMood.toString(), null, null);

                    } else {
                        Toast.makeText(getApplicationContext(), "Entrez le numero de téléphone", Toast.LENGTH_SHORT).show();
                    }
                }catch(Exception e){
                    Toast.makeText(getApplicationContext(), "Le message n'a pas été envoyé", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });
        buildShare.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.cancel();
            }
        });
        buildShare.show();
    }

    private void save() {

        //check if saved moods are older than 7 days and in that case remove it from the list
        if(!mMoodList.isEmpty()){
            for(int i=0;i<mMoodList.size(); i++) {
            Date old = mMoodList.get(i).getDate().getTime();
            Calendar cal=Calendar.getInstance();
            cal.add(Calendar.DATE, -8);
            Date dateSevenDaysAgo = cal.getTime();

            if (old.before(dateSevenDaysAgo)) {
                mMoodList.remove(i);
            }
          }
        }
        try {
            mOutputStream = openFileOutput(mFilename, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(mOutputStream);
            oos.writeObject(mMoodList);
            oos.close();
            mOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mComment = "";
    }

    private void readFile() {
        try (
                FileInputStream fis = openFileInput(mFilename);
                BufferedInputStream bis = new BufferedInputStream(fis);
                ObjectInputStream ois = new ObjectInputStream(bis)) {
            mMoodList = (List<Mood>) ois.readObject();
            ois.close();
            bis.close();
            fis.close();
        } catch (FileNotFoundException e) {
            System.err.println("Le fichier est introuvable ! ");
        } catch (IOException e) {
            System.err.println("Erreur de lecture du fichier de sauvegarde !");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    private void checkDate(){

        //Create a new mood
        int position = mViewPager.getCurrentItem();
        mCurrentTime = Calendar.getInstance();
        mMood = new Mood(position, mCurrentTime, mComment);

        //Compare the last saved mood with the actual to see if they are from the same day
        if(!mMoodList.isEmpty()) {
            int dayCurrentMood = mCurrentTime.get(Calendar.DAY_OF_MONTH);
            int monthCurrentMood= mCurrentTime.get(Calendar.MONTH);

            Calendar c=mMoodList.get(mMoodList.size()-1).getDate();

            int lastSavedMoodDay=c.get(Calendar.DAY_OF_MONTH);
            int lastSavedMoodMonth = c.get(Calendar.MONTH);

                //If the two are from the same day, replace the old one with the recent
                if(dayCurrentMood == lastSavedMoodDay && monthCurrentMood == lastSavedMoodMonth){
                    mMoodList.set(mMoodList.size()-1, mMood);
                    save();
                    Toast.makeText(getApplicationContext(),"Votre humeur du jour a été sauvgardé!", Toast.LENGTH_SHORT).show();
                }else{
                    mMoodList.add(mMood);
                    Toast.makeText(getApplicationContext(),"Votre humeur du jour a été ajouté à la liste!", Toast.LENGTH_SHORT).show();
                    save();
                }
        }else{
            mMoodList.add(mMood);
            save();
            Toast.makeText(getApplicationContext(),"Votre humeur du jour a été sauvgardé!", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        checkDate();
        sharePopUp();
    }

}


