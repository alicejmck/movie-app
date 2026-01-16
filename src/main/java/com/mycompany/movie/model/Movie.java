/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.movie.model;

/**
 *
 * @author alicemckinley
 */
public class Movie {
    public String title;
    public String shortDescription;
    public String longDescription;
    public double rating;
    public boolean rated;
    public boolean saved;

    public Movie(String title, String shortDescription, String longDescription, double rating, boolean rated, boolean saved) {
        this.title = title;
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;
        this.rating = rating;
        this.rated = rated;
        this.saved = saved;
    }

    /*
    These 6 methods take no input but return the values of the fields of the movie class
    */
    public String getTitle() { return title; }
    public String getShortDescription() { return shortDescription; }
    public String getLongDescription() { return longDescription; }
    public double getRating() { return rating; }
    public boolean isRated() { return rated; }
    public boolean isSaved() { return saved; }

    /*
    void setRating(double newRating) takes a double representing the rating of the movie
    It doesn't return anything
    */
    public void setRating(double newRating) {
        this.rating = newRating;
        this.rated = true;
    }

    /*
    void setSaved() takes no input
    It doesn't return anything
    */
    public void setSaved() { this.saved = !this.saved; }
}
