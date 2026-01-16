/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.movie;

/**
 *
 * @author alicemckinley
 * 
 * This is the main class of the program and this file should be run
 */

import com.mycompany.movie.controller.*;
import com.mycompany.movie.view.*;

public class MainClass {
    public static void main(String[] args) {
        com.mycompany.movie.view.UI gui = new com.mycompany.movie.view.UI();
        
        com.mycompany.movie.controller.MovieController controller = new com.mycompany.movie.controller.MovieController(gui);
        gui.controller = controller;
        com.mycompany.movie.model.DBConnector connector = new com.mycompany.movie.model.DBConnector("moviesList.sql", "moviesList.db");
        com.mycompany.movie.model.MovieCollection collection = new com.mycompany.movie.model.MovieCollection(connector);
        com.mycompany.movie.controller.MovieController.collection = collection;
        
        controller.testFunction();
        gui.initUI();
    }
}
