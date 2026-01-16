/*

 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license

 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template

 */

package com.mycompany.movie.model;

import java.util.ArrayList;

public class MovieCollection {
    public static ArrayList<Movie> movies;
    public DBConnector connector;
    
    public MovieCollection(DBConnector connector) {
        this.connector = connector;
        this.movies = connector.getMovies();
    }

    /*
    ArrayList<Movie> getAllMovies() doesn't take any input
    It returns all the movies in the sql database
    */
    public ArrayList<Movie> getAllMovies() { return movies; }

    /*
    ArrayList<Movie> getRatedMovies() doesn't take any input
    It returns an arraylist of all the rated movies in the sql database
    */
    public ArrayList<Movie> getRatedMovies() {
        ArrayList<Movie> ratedMovies = new ArrayList<>();
        for (Movie movie : movies) {
            if (movie.isRated()) { ratedMovies.add(movie); }
        }
        return ratedMovies;
    }

    /*
    ArrayList<Movie> getSavedMovies() takes no input
    It returns an arraylist of all the saved movies in the sql database
    
    */
    public ArrayList<Movie> getSavedMovies() {
        ArrayList<Movie> savedMovies = new ArrayList<>();
        for (Movie movie : movies) {
            if (movie.isSaved()) savedMovies.add(movie);
        }
        return savedMovies;
    }

    /*
    void rateMovie(Movie movie, double rating) takes a movie object and a double representing its rating
    It doesn't return anything
    
    */
    public void rateMovie(Movie movie, double rating) {
        movie.setRating(rating);
        connector.setRating(movie.getTitle(), rating);
    }

    /*
    void toggleSaved(Movie movie) takes a movie object as input
    It doesn't return anything
    
    */
    public void toggleSaved(Movie movie) {
        movie.setSaved();
        connector.toggleSavedStatus(movie.getTitle());
    }
}
