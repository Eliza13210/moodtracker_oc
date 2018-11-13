package com.oc.liza.moodtrackeroc.model;

import java.io.Serializable;
import java.util.Calendar;


public class Mood implements Serializable{

    //mood number check with list in screenslide
    private int mood;
    private Calendar date;
    private String comment;
    private String[] moods={" très mauvaise humeur"," mauvaise humeur", " humeur normale"," bonne humeur", " super bonne humeur"};

    public Mood(int mood, Calendar date, String comment ){
        this.mood=mood;
        this.date=date;
        this.comment=comment;
    }

    public int getMood() {
        return mood;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }
    public Calendar getDate() {
        return date;
    }


    public String getComment() {
        return comment;
    }


    public String toString(){
        String firstLine;
        if(mood<2) firstLine = "Fais attention! ";
        else if(mood>2){
            firstLine="C'est une excellente journée! ";
        }else{
            firstLine="Bonjour ";
        }
        return firstLine+" Aujourd'hui, je suis de" + moods[mood] + " : " + getComment();


    }



}