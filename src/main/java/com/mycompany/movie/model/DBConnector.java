/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.movie.model;

/**
 *
 * @author alekr5316
 */

import java.util.ArrayList;
import java.nio.file.*;
import java.sql.*;

public class DBConnector {
    String dbFilePath;
    String sqlFilePath;
    String url;
    private ArrayList<Movie> movies = new ArrayList<>(); 
    
    /*INPUTS
    sqlf: a string representing the file name of the sql file to be executed (not the full path)
    dbf: A string representing the name of the database file(not the full path)
    */
    public DBConnector(String sqlf, String dbf){
        this.dbFilePath = dbf; 
        this.sqlFilePath = sqlf;  
        String fPath = "/Users/alicemckinley/NetBeansProjects/movie/src/main/java/com/mycompany/movie/";
        this.url = "jdbc:sqlite:" + fPath+this.dbFilePath;
        
        try (Connection conn = DriverManager.getConnection(this.url);
            Statement stmt = conn.createStatement()) {
            if (Files.exists(Paths.get(fPath+this.sqlFilePath))) {
                String sqlStatements = new String(Files.readAllBytes(Paths.get(fPath + this.sqlFilePath)));
                /*if (sqlStatements != null && !sqlStatements.trim().isEmpty()) {
                    String[] statements = sqlStatements.split(";");
                    for (String statement : statements) {
                        statement = statement.trim();
                        if (!statement.isEmpty()) {
                            stmt.execute(statement);
                        }
                    }
                }


                ResultSet countRs = stmt.executeQuery("SELECT COUNT(*) FROM Movies");
                if (countRs.next()) {
                    int count = countRs.getInt(1);
                    System.out.println("Found " + count + " movies in the database");
                    if (count == 0) {
                        System.out.println("No movies in database! Check SQL INSERT statements.");
                    }
                }*/

                ResultSet rs = stmt.executeQuery("SELECT * FROM Movies");

                while (rs.next()) {
                    this.movies.add(new Movie(rs.getString("Title"), rs.getString("ShortDescription"), rs.getString("LongDescription"), rs.getDouble("Rating"), rs.getBoolean("Rated"), rs.getBoolean("Saved")));
                }

                rs.close();

            } else {
                System.out.println("Error: " + sqlFilePath + " not found.");
            }      
            
        } catch (Exception e) {
            System.out.println("Exception message: " + e.getMessage());
            e.printStackTrace();
            System.out.println("SQL Connection not working");
        } 
    }

    /*
    void setRating(String title, double rating) takes a title and rating of a movie
    It doesn't return anything
    It modifies the particular movie's rating in the database
    */
    public void setRating(String title, double rating){
        try (Connection conn = DriverManager.getConnection(this.url);
             PreparedStatement stmt = conn.prepareStatement(
                     "UPDATE movies SET rating = ?, Rated = TRUE WHERE title = ?")) {

            stmt.setDouble(1, rating);
            stmt.setString(2, title);
            int rowsAffected = stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
    void toggleSavedStatus(String title) takes a title of a movie
    It doesn't return anything
    It flips the particular movie's saved boolean in the database
    */
    public void toggleSavedStatus(String title) {
        try (Connection conn = DriverManager.getConnection(this.url)) {

            PreparedStatement Stmt = conn.prepareStatement(
                "SELECT saved FROM movies WHERE title = ?"
            );
            Stmt.setString(1, title);
            ResultSet rs = Stmt.executeQuery();

            if (rs.next()) {
                boolean currentStatus = rs.getBoolean("saved");

                boolean newStatus = !currentStatus;

                PreparedStatement updateStmt = conn.prepareStatement(
                    "UPDATE movies SET saved = ? WHERE title = ?"
                );
                updateStmt.setBoolean(1, newStatus);
                updateStmt.setString(2, title);
                updateStmt.executeUpdate();
            } else {
               System.out.println("Movie not found in the database.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /*
    ArrayList<Movie> getMovies() doesn't take any input
    It returns an ArrayList of all the movies in the database
    */
    public ArrayList<Movie> getMovies() {
        return movies;
    }
}
