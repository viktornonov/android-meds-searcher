package com.example.myapp;

/**
 * Created by viktor on 6/14/14.
 */
public class Med {
    private int id;
    private String title;

    public Med(){}

    public Med(String title) {
        super();
        this.title = title;
    }

    //getters & setters
    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return "Med [id=" + id + ", title=" + title
                + "]";
    }
}
