package com.oc.liza.moodtrackeroc.model;

import java.io.Serializable;
import java.util.Date;

public class Mood implements Serializable{

    //mood number check with list in screenslide
    private int mood;
    private Date date;
    private String comment="";

    public Mood(){}

    public Mood(int mood, Date date, String comment ){
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

    public void setDate(Date date) {
        this.date = date;
    }
    public Date getDate() {
        return date;
    }


    public void setComment(String comment) {
        this.comment = comment;
    }
    public String getComment() {
        return comment;
    }


    public String toString(){
        String str=" " + "+" + String.valueOf(date) + "+ :" + String.valueOf(mood) + getComment();
        return str;

    }



}