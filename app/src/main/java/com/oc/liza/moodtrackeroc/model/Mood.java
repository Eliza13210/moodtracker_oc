package com.oc.liza.moodtrackeroc.model;

import java.util.Date;

public class Mood {

    //mood number check with list in screenslide
    private int mood;
    private Date date;

    public Mood(){}

    public Mood(int mood ){
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



}