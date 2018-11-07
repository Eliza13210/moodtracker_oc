package com.oc.liza.moodtrackeroc.model;

import android.widget.TextView;

import com.oc.liza.moodtrackeroc.R;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
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

public class MoodList implements Serializable {

    private List<Mood> mMoodList=new ArrayList<>();
    private String saveFile="D:\\OpenClassrooms\\moodList.txt";

    public MoodList(){
        readSavedFile();
    }

    private void readSavedFile(){
        try (FileInputStream fis = new FileInputStream(saveFile);
                BufferedInputStream bis = new BufferedInputStream(fis);
                    ObjectInputStream ois = new ObjectInputStream(bis) ) {
                        mMoodList = (List<Mood>)ois.readObject();

            ois.close();
        }	catch (FileNotFoundException e) {
            System.err.println("Aucun humeur sauvegardé ! ");
        } catch (IOException e) {
            System.err.println("Erreur de lecture du fichier de sauvegarde !");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    private void writeSavedFile(){
        try (FileOutputStream fis = new FileOutputStream(saveFile);
                BufferedOutputStream bis = new BufferedOutputStream(fis);
                    ObjectOutputStream ois = new ObjectOutputStream(bis) ) {
                        ois.writeObject(mMoodList);
            ois.close();
        }	catch (FileNotFoundException e) {
            System.err.println("Aucun fichier trouvé ! " + e);
        } catch (IOException e) {
            System.err.println("Erreur d'écriture dans le fichier de sauvegarde !");
        }
    }
    public void addMood(Mood m){
        mMoodList.add(m);
        writeSavedFile();

    }

    public String toString(){
        String str;
        Date mCurrentTime = Calendar.getInstance().getTime();
        str =  "****************************\n";


        for (Mood m : mMoodList)
            str += m + "\n";

        return str;
    }

}
