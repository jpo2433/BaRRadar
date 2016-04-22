package com.example.jasmin.barradar.model;

import android.location.*;

/**
 * Created by Jasmin on 07.04.2016.
 */
public class Comment {
    private long id;
    private long location;
    private String comment;

    public Comment(long id, String comment){
        location = id;
        this.comment = comment;
    }

    //Getter and Setter
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getLocation() {
        return location;
    }

    public void setLocation(long location) {
        this.location = location;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return comment;
    }
}
