/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.movie.controller;

/**
 *
 * @author alicemckinley
 */

import com.mycompany.movie.model.*;
import com.mycompany.movie.view.*;
import java.util.ArrayList;

public class MovieController {
    public com.mycompany.movie.view.UI ui;
    public static com.mycompany.movie.model.MovieCollection collection;

    public MovieController(com.mycompany.movie.view.UI uiFromView) {
        this.ui = uiFromView;
    }
    
    /*
    ArrayList<Movie> getMovies() takes no input
    It returns an arraylist containing all the movies in the sql database
    */
    public ArrayList<Movie> getMovies() {
        ArrayList<Movie> returnList = new ArrayList();
        for (Movie movie : com.mycompany.movie.model.MovieCollection.movies) {
            returnList.add(movie);
        }
        
        return returnList;
    }
    
    /*
    void saveMovie(Movie movie) takes a movie object as input
    It doesn't return anything
    It calls the method in the model to flip the saved status boolean for the particular movie in the database
    */
    public void saveMovie(Movie movie) {
        collection.toggleSaved(movie);
    }
    
    /*
    void rateMovie(Movie movie, double rating) takes a movie object and a double representing its rating as input
    It doesn't return anything
    It calls the method in the model to save a rating for a particular movie in the database
    */
    public void rateMovie(Movie movie, double rating) {
        collection.rateMovie(movie, rating);
    }
    
    /*
    ArrayList<Movie> getRatedMovies() takes no input
    It returns an arraylist of all the movies in the sql database with the rated property set to true
    */
    public ArrayList<Movie> getRatedMovies() {
        return collection.getRatedMovies();
    }
    
    /* 
    ArrayList<Movie> search(String query, ArrayList<Movie> movies) takes the search term and an ArrayList of movies as inputs
    It returns an ArrayList of movies that contain the search term
    */
    public ArrayList<Movie> search(String query, ArrayList<Movie> movies) {
        int start = 0;
        int end = movies.size() - 1;

        ArrayList<com.mycompany.movie.model.Movie> searchResults = new ArrayList<>();

        while (start <= end) {
            int mid = (start + end) / 2;
            int comparison = movies.get(mid).title.toLowerCase().compareTo(query.toLowerCase());

            if (comparison == 0) {
                searchResults.add(movies.get(mid));
                return searchResults;
            } else if (comparison < 0) {
                start = mid + 1;
            } else {
                end = mid - 1;
            }
        }
        for (com.mycompany.movie.model.Movie movie : movies) {
            if (movie.title.toLowerCase().contains(query.toLowerCase())) {
                searchResults.add(movie);
            }
        }
        return searchResults;
    }
    
    public void testFunction() {
        ArrayList<Movie> moviesTest = new ArrayList<>();
        moviesTest.add(new Movie("Titanic", "short", "long", 2.4, true, true));
        moviesTest.add(new Movie("Pirates of the Caribbean: The Curse of the Black Pearl", "short", "long", 2.4, true, true));
        moviesTest.add(new Movie("Shrek", "short", "long", 2.4, true, true));
        moviesTest.add(new Movie("Back to the Future", "short", "long", 2.4, true, true));
        moviesTest.add(new Movie("Bee Movie", "short", "long", 2.4, true, true));
        moviesTest.add(new Movie("Alien", "short", "long", 2.4, true, true));

        // Test 1: Exact match (binary search hit)
        ArrayList<Movie> result1 = search("Titanic", moviesTest);
        if (!(result1.size() == 1 && result1.get(0).title.equals("Titanic") ? "PASS" : "FAIL").equals("PASS")) {
            System.out.println("Failed test 1.");
        }

        // Test 2: No match
        ArrayList<Movie> result2 = search("prates of the carbean", moviesTest);
        if (!(result2.size() == 0 ? "PASS" : "FAIL").equals("PASS")) {
            System.out.println("Failed test 2.");
        }

        // Test 3: Case-insensitive match
        ArrayList<Movie> result3 = search("shrek", moviesTest);
        if (!(result3.size() == 1 && result3.get(0).title.equals("Shrek") ? "PASS" : "FAIL").equals("PASS")) {
            System.out.println("Failed test 3.");
        }
        
        // Test 4: Exact match, but expect the wrong movie
        ArrayList<Movie> result5 = search("Titanic", moviesTest);
        if (!(result5.size() == 1 && result5.get(0).title.equals("Shrek") ? "PASS" : "FAIL").equals("FAIL")) {
            System.out.println("Failed test 4.");
        }

        // Test 5: No match, but expect a result
        ArrayList<Movie> result6 = search("Future to the Back", moviesTest);
        if (!(result6.size() == 1 ? "PASS" : "FAIL").equals("FAIL")) {
            System.out.println("Failed test 5.");
        }

        // Test 6: Case-insensitive match, but expect the wrong movie
        ArrayList<Movie> result7 = search("alien", moviesTest);
        if (!(result7.size() == 1 && result7.get(0).title.equals("Bee Movie") ? "PASS" : "FAIL").equals("FAIL")) {
            System.out.println("Failed test 6.");
        }
    }
}
