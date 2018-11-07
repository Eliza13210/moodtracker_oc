package com.oc.liza.moodtrackeroc.model;

import java.io.Serializable;
import java.util.Date;

public class Mood implements Serializable{

    //mood number check with list in screenslide
    private int mood;
    private Date date;

    public Mood(){}

    public Mood(int mood, Date date ){
        this.mood=mood;
    }

    public int getMood() {
        return mood;
    }
    public void setMood(int mood) {
        this.mood = mood;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    public Date getDate() {
        return date;
    }
    public String toString(){
        String str=String.valueOf(date) + String.valueOf(mood);
        return str;

    }



}