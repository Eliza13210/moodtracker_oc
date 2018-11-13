package com.oc.liza.moodtrackeroc.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Mood implements Serializable{

    //mood number check with list in screenslide
    private int mood;
    private Calendar date;
    private String comment="";
    private String[] moods={" très mauvaise humeur"," mauvaise humeur", " humeur normale"," bonne humeur", " super bonne humeur"};

    public Mood(){}

    public Mood(int mood, Calendar date, String comment ){
        this.mood=mood;
        this.date=date;
        this.comment=comment;
    }

    public int getMood() {
        return mood;
    }
    public void setMood(int mood) {
        this.mood = mood;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }
    public Calendar getDate() {
        return date;
    }


    public void setComment(String comment) {
        this.comment = comment;
    }
    public String getComment() {
        return comment;
    }


    public String toString(){
        String str=" Ce jour: " + String.valueOf(date) + " je suis de" + moods[mood] + " : " + getComment();
        return str;


    }



}