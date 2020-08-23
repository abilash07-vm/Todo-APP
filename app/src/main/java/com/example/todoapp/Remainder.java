package com.example.todoapp;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class Remainder {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String body;
    private int duration;
    private String t24hrtime;
    private String date;
    private String time;
    private boolean iscomplete;

    public Remainder(String title, String body, String date,String time,String t24hrtime) {
        this.title = title;
        this.body = body;
        this.date = date;
        this.time=time;
        this.t24hrtime=t24hrtime;
    }

    public String getT24hrtime() {
        return t24hrtime;
    }

    public void setT24hrtime(String t24hrtime) {
        this.t24hrtime = t24hrtime;
    }

    public int getId() {
        return id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }



    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public boolean isIscomplete() {
        return iscomplete;
    }

    public void setIscomplete(boolean iscomplete) {
        this.iscomplete = iscomplete;
    }
}
