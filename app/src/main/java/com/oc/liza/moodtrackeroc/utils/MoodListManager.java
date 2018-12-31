package com.oc.liza.moodtrackeroc.utils;

import android.content.Context;

import com.oc.liza.moodtrackeroc.model.Mood;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MoodListManager {
    private List<Mood> moodList = new ArrayList<>();
    private final static String filename = "moodsList.txt";
    private Context context;

    public MoodListManager(Context context) {
        this.context = context;

    }

    //Return the saved mood list
    public List<Mood> getMoodList() {
        readFile();
        return moodList;
    }

    //fetch the saved mood list
    private void readFile() {
        try (
                FileInputStream fis = new FileInputStream(new File(context.getFilesDir(), filename));
                BufferedInputStream bis = new BufferedInputStream(fis);
                ObjectInputStream ois = new ObjectInputStream(bis)) {
            moodList = (List<Mood>) ois.readObject();
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

    //Add new mood
    public void addMood(Mood mood) {
        readFile();
        checkDate(mood);
    }

    //check if there is already a mood saved the same day, and in that case replace it
    private void checkDate(Mood mood) {

        Calendar currentTime = Calendar.getInstance();

        //Compare the last saved mood with the actual to see if they are from the same day
        if (!moodList.isEmpty()) {
            int dayCurrentMood = currentTime.get(Calendar.DAY_OF_MONTH);
            int monthCurrentMood = currentTime.get(Calendar.MONTH);

            Calendar c = moodList.get(moodList.size() - 1).getDate();

            int lastSavedMoodDay = c.get(Calendar.DAY_OF_MONTH);
            int lastSavedMoodMonth = c.get(Calendar.MONTH);

            //If the two are from the same day, replace the old one with the recent
            if (dayCurrentMood == lastSavedMoodDay && monthCurrentMood == lastSavedMoodMonth) {
                moodList.set(moodList.size() - 1, mood);
                save();
            } else {
                moodList.add(mood);
                save();
            }
        } else {
            moodList.add(mood);
            save();
        }
    }

    private void save() {

        //check if saved moods are older than 7 days and in that case remove it/them from the list
        if (!moodList.isEmpty()) {
            for (int i = 0; i < moodList.size(); i++) {
                Date old = moodList.get(i).getDate().getTime();
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, -8);
                Date dateSevenDaysAgo = cal.getTime();

                if (old.before(dateSevenDaysAgo)) {
                    moodList.remove(i);
                }
            }
        }
        try {
            FileOutputStream outputStream = new FileOutputStream(new File(context.getFilesDir(), filename));
            ObjectOutputStream oos = new ObjectOutputStream(outputStream);
            oos.writeObject(moodList);
            oos.close();
            outputStream.close();
        } catch (Exception e) {
            System.err.println("Votre humeur du jour n'a pas pu être sauvegardée!" + e);
        }
    }

}
