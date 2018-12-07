package com.oc.liza.moodtrackeroc.controler;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
    private String mComment = "";
    private Mood mMood;
    private List<Mood> mMoodList = new ArrayList<>();
    private String mFilename = "moodsList.txt";
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;
    private String mNumber = "";
    private String mAddress = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //fetch saved mood list
        readFile();
        mMoodList.clear();
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -7);
        mMoodList.add(new Mood(0, c, "Seven days ago"));
         c = Calendar.getInstance();
        c.add(Calendar.DATE, -6);
        mMoodList.add(new Mood(1, c, "Six days ago"));
        c = Calendar.getInstance();
        c.add(Calendar.DATE, -5);
        mMoodList.add(new Mood(2, c, "Five days ago"));
        c = Calendar.getInstance();
        c.add(Calendar.DATE, -4);
        mMoodList.add(new Mood(3, c, "Four days ago"));
        c = Calendar.getInstance();
        c.add(Calendar.DATE, -3);
        mMoodList.add(new Mood(4, c, "Three days ago"));
        c = Calendar.getInstance();
        c.add(Calendar.DATE, -2);
        mMoodList.add(new Mood(3, c, "Two days ago"));
        c = Calendar.getInstance();
        c.add(Calendar.DATE, -1);
        mMoodList.add(new Mood(4, c, "One day ago"));

        //Button to add a comment
        ImageButton commentBtn = findViewById(R.id.commentButton);
        commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentPopUp();
            }
        });

        //Button to share your mood by SMS
        ImageButton shareBtn = findViewById(R.id.shareButton);
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharePopUp();
            }
        });

        //Button to access history
        ImageButton historyBtn = findViewById(R.id.historyButton);
        historyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent history = new Intent(MainActivity.this, History.class);
                history.putExtra("mMoodList", (Serializable) mMoodList);
                startActivity(history);
            }
        });

        // Set slide up and down function
        mViewPager = findViewById(R.id.pager);
        ScreenSlide adapter = new ScreenSlide(this);
        mViewPager.setAdapter(adapter);

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

    //Share mood by sms or e-mail
    private void sharePopUp() {

        if (!mMoodList.isEmpty()) {
            mMood = mMoodList.get(mMoodList.size() - 1);
        } else {
            Toast.makeText(getApplicationContext(), "Sauvegardez un humeur avant de partager", Toast.LENGTH_SHORT).show();
        }

        AlertDialog.Builder buildShare = new AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog_Alert);
        buildShare.setTitle("Choissisez votre mode de partage");
        LinearLayout layout = new LinearLayout(getApplicationContext());
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText inputShare = new EditText(this);
        inputShare.setTextColor(Color.DKGRAY);
        inputShare.setHintTextColor(Color.LTGRAY);
        inputShare.setHint("N° de téléphone");
        inputShare.setInputType(InputType.TYPE_CLASS_PHONE);
        layout.addView(inputShare);

        final EditText inputEmail = new EditText(this);
        inputEmail.setTextColor(Color.DKGRAY);
        inputEmail.setHintTextColor(Color.LTGRAY);
        inputEmail.setHint("Adresse e-mail");
        inputEmail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        layout.addView(inputEmail);
        buildShare.setView(layout);

        // Set up the buttons
        buildShare.setPositiveButton("Valider", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {

                mNumber = inputShare.getText().toString();
                mAddress = inputEmail.getText().toString();

                if (mAddress.isEmpty()) {
                    sendSMS();
                    dialog.dismiss();
                } else {
                    sendEmail(mAddress);
                    dialog.dismiss();
                }
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

    private void sendSMS() {
        //check if permission to send sms and if not so, ask for permission
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS},
                    MY_PERMISSIONS_REQUEST_SEND_SMS);
        }
        // Permission has already been granted
        try {
            SmsManager.getDefault().sendTextMessage(mNumber, null, mMood.toString(), null, null);
            Toast.makeText(getApplicationContext(), "SMS envoyé", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Le message n'a pas été envoyé ", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                // permission was granted
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try {
                        SmsManager.getDefault().sendTextMessage(mNumber, null, mMood.toString(), null, null);
                        Toast.makeText(getApplicationContext(), "SMS envoyé", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Le message n'a pas été envoyé ", Toast.LENGTH_SHORT).show();
                    }
                }
                // permission denied
                Toast.makeText(getApplicationContext(), "Vous n'avez pas autorisé l'application d'envoyer des SMS", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void sendEmail(String adress) {
        Intent send = new Intent(Intent.ACTION_SENDTO);
        String uriText = "mailto:" + Uri.encode(adress) +
                "?subject=" + Uri.encode("Je voudrais partager mon humeur") +
                "&body=" + Uri.encode(mMood.toString());
        Uri uri = Uri.parse(uriText);
        send.setData(uri);
        startActivity(Intent.createChooser(send, "Send mail"));

    }

    private void save() {

        //check if saved moods are older than 7 days and in that case remove it/them from the list
        if (!mMoodList.isEmpty()) {
            for (int i = 0; i < mMoodList.size(); i++) {
                Date old = mMoodList.get(i).getDate().getTime();
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, -8);
                Date dateSevenDaysAgo = cal.getTime();

                if (old.before(dateSevenDaysAgo)) {
                    mMoodList.remove(i);
                }
            }
        }
        try {
            FileOutputStream outputStream = openFileOutput(mFilename, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(outputStream);
            oos.writeObject(mMoodList);
            oos.close();
            outputStream.close();
            Toast.makeText(getApplicationContext(), "Votre humeur du jour a été sauvegardée!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            System.err.println("Votre humeur du jour n'a pas pu être sauvegardée!");
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

    private void checkDate() {

        //Create a new mood
        int position = mViewPager.getCurrentItem();
        Calendar currentTime = Calendar.getInstance();
        mMood = new Mood(position, currentTime, mComment);

        //Compare the last saved mood with the actual to see if they are from the same day
        if (!mMoodList.isEmpty()) {
            int dayCurrentMood = currentTime.get(Calendar.DAY_OF_MONTH);
            int monthCurrentMood = currentTime.get(Calendar.MONTH);

            Calendar c = mMoodList.get(mMoodList.size() - 1).getDate();

            int lastSavedMoodDay = c.get(Calendar.DAY_OF_MONTH);
            int lastSavedMoodMonth = c.get(Calendar.MONTH);

            //If the two are from the same day, replace the old one with the recent
            if (dayCurrentMood == lastSavedMoodDay && monthCurrentMood == lastSavedMoodMonth) {
                mMoodList.set(mMoodList.size() - 1, mMood);
                save();
            } else {
                mMoodList.add(mMood);
                save();
            }
        } else {
            mMoodList.add(mMood);
            save();
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Voudriez vous sauvegarder l'humeur sélectionné?")
                .setCancelable(false)
                .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        checkDate();
                        MainActivity.super.onBackPressed();

                    }
                })
                .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        MainActivity.super.onBackPressed();

                    }
                })
                .show();
    }
}


