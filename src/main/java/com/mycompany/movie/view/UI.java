/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.movie.view;

/**
 *
 * @author alicemckinley
 */

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import com.mycompany.movie.model.*;

/**
 * JPanel that displays an image
 */
class ImagePanel extends JPanel {
    BufferedImage image;

    /**
     * Loads image from the provided file path
     * 
     * @param imagePath Path to the image file
     */
    public ImagePanel(String imagePath) {
        try {
            File myFile = new File(imagePath);
            image = ImageIO.read(myFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Paints scaled image to panel
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        }
    }
}

/**
 * Contains the app's user interface
 */
public class UI {
    private CardLayout cardLayout;
    private JPanel panel;
    private JPanel searchBarPanel;
    private JPanel container;
    private JFrame frame;
    public int buttonIndex = 0;
    private JLabel selectedMovieTitle;
    private JLabel selectedMovieDescription;
    private Movie selectedMovie;
    private ImagePanel moviePanel;
    private JComboBox<String> ratingDropdown;
    

    public com.mycompany.movie.controller.MovieController controller;
    public ArrayList<com.mycompany.movie.model.Movie> movies;
    public ArrayList<com.mycompany.movie.model.Movie> searchResults;

    public UI() {
        this.cardLayout = new CardLayout();
        this.panel = new JPanel(cardLayout);
        this.searchBarPanel = new JPanel(null);
        this.container = new JPanel(new BorderLayout());
        this.frame = new JFrame("Movie App");

        searchBarPanel.setOpaque(false);
        searchBarPanel.setBackground(new Color(255, 255, 255, 0));
        searchBarPanel.setBounds(0, 45, 1200, 50);
    }
    
    /*
    void updateHomePage(ArrayList<com.mycompany.movie.model.Movie> searchResults) takes an arraylist of movies 
    It doesn't return anything
    It updates the homepage to conatin the appropriate movie cards based on what has been searched
    */
    public void updateHomePage(ArrayList<com.mycompany.movie.model.Movie> searchResults) { 
        JPanel homePanel = (JPanel) panel.getComponent(1); 
        JScrollPane scrollPane = null;
        
        insertionSort(searchResults);

        for (Component comp : homePanel.getComponents()) {
            if (comp instanceof JScrollPane) {
                scrollPane = (JScrollPane) comp;
                break;
            }
        }

        if (scrollPane != null) {
            JPanel movieListPanel = (JPanel) scrollPane.getViewport().getView();
            movieListPanel.removeAll(); 

            ArrayList<JButton> buttons = new ArrayList<JButton>();
            
            //new
            insertionSort(searchResults);

            for (com.mycompany.movie.model.Movie movie : searchResults) {
                JPanel movieCard = new JPanel();
                movieCard.setLayout(new BoxLayout(movieCard, BoxLayout.Y_AXIS));
                movieCard.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                movieCard.setBackground(new Color(83, 84, 85));

                JLabel titleLabel = new JLabel(movie.title);
                titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
                titleLabel.setForeground(Color.WHITE);
                titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

                JButton goButton = new JButton("VIEW");
                buttons.add(goButton);
                goButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                goButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        buttonIndex = buttons.indexOf(goButton);
                        // true is new
                        updateMoviePage(true);
                        cardLayout.show(panel, "Movie Page");
                        container.revalidate();
                        container.repaint();
                    }
                });

                BufferedImage placeholderImage = null;
                try {
                    placeholderImage = ImageIO.read(new File(
                            "/Users/alicemckinley/NetBeansProjects/movie/src/main/java/com/mycompany/movie/images/placeholder.png"));
                } catch (IOException e) {
                    System.out.println("Placeholder image not found.");
                }

                if (placeholderImage != null) {
                    ImageIcon icon = new ImageIcon(placeholderImage.getScaledInstance(150, 200, Image.SCALE_SMOOTH));
                    JLabel imageLabel = new JLabel(icon);
                    imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                    movieCard.add(imageLabel);
                }

                movieCard.add(Box.createRigidArea(new Dimension(0, 10)));
                movieCard.add(titleLabel);
                movieCard.add(Box.createRigidArea(new Dimension(0, 10)));
                movieCard.add(goButton);
                movieCard.setMaximumSize(new Dimension(180, 300));
                movieListPanel.add(movieCard);
                movieListPanel.add(Box.createRigidArea(new Dimension(20, 0)));
            }

            JButton backButton = new JButton("Back to Home Page");
            backButton.setBounds(50, 55, 150, 30);
            backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateHomePage(movies);
                cardLayout.show(panel, "Home Page");
                container.revalidate();
                container.repaint();
                }  
            });
            homePanel.add(backButton);
            
            movieListPanel.revalidate();
            movieListPanel.repaint();
        }

        homePanel.revalidate();
        homePanel.repaint();
    }
    
    /*
    void initUI() takes no input
    It initializes the GUI at the beginning of the program
    */
    public void initUI() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);

        panel.add(createStartPage(), "Start Page");
        panel.add(createHomePage(), "Home Page");
        panel.add(createMoviePage(), "Movie Page");
        panel.add(createSavedPage(), "Saved Page");
        panel.add(createWatchedPage(), "Watched Page");
        updateWatchedPage();
        updateSavedPage();

        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");

        searchField.setBounds(850, 10, 200, 30);
        searchButton.setBounds(1060, 10, 100, 30);
        
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String searchQuery = searchField.getText().toLowerCase();
                //ArrayList<Movie> searchedMovies = controller.search(searchQuery, movies);
                searchResults = controller.search(searchQuery, movies);
                updateHomePage(searchResults);
                container.revalidate();
                container.repaint();
            }
        });

        searchBarPanel.add(searchField);
        searchBarPanel.add(searchButton);
        searchBarPanel.setVisible(false); 

        container.add(searchBarPanel);
        container.add(panel);

        frame.add(container);
        frame.setVisible(true);

        cardLayout.show(panel, "Start Page");
    }

    /**
     * Creates the start page with a background, text, and start button
     * 
     * @return ImagePanel of start page
     */
    public ImagePanel createStartPage() {
        ImagePanel startPanel = new ImagePanel("/Users/alicemckinley/NetBeansProjects/movie/src/main/java/com/mycompany/movie/images/startpagebg.png");
        startPanel.setLayout(null);

        JLabel welcomeText = new JLabel("WELCOME TO");
        welcomeText.setFont(new Font("SansSerif", Font.BOLD, 52));
        welcomeText.setSize(welcomeText.getPreferredSize());
        welcomeText.setLocation((1200 - welcomeText.getWidth()) / 2, 260); 

        JLabel appNameText = new JLabel("MOVIE APP");
        appNameText.setFont(new Font("SansSerif", Font.BOLD, 52));
        appNameText.setSize(appNameText.getPreferredSize());
        appNameText.setLocation((1200 - appNameText.getWidth()) / 2, 350); 

        JButton startButton = new JButton("GET STARTED");
        startButton.setBorder(null);
        startButton.setPreferredSize(new Dimension(190, 100));
        startButton.setFont(new Font("SansSerif", Font.BOLD, 24));
        startButton.setBounds((1200 - startButton.getPreferredSize().width) / 2, 580,
                startButton.getPreferredSize().width, startButton.getPreferredSize().height);

        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(panel, "Home Page");
                searchBarPanel.setVisible(true);
                container.revalidate();
                container.repaint();
            }

        });

        startPanel.add(welcomeText);
        startPanel.add(appNameText);
        startPanel.add(startButton);

        return startPanel;
    }
    
    /*
    void insertionSort(ArrayList<Movie> movies) takes an arraylist of movies
    It uses the insertion sort algorithm to sort the movies
    */
    public static void insertionSort(ArrayList<Movie> movies) {
        for (int i = 1; i < movies.size(); i++) {
            Movie key = movies.get(i);
            int j = i - 1;

            while (j >= 0 && movies.get(j).title.compareToIgnoreCase(key.title) > 0) {
                movies.set(j + 1, movies.get(j));
                j = j - 1;
            }
            movies.set(j + 1, key);
        }
    }

    /**
     * Creates the home page with a horizontally scrollable list of movies and
     * search/navigation bar
     * 
     * @return ImagePanel of home page
     */
    public ImagePanel createHomePage() {
        ImagePanel homePanel = new ImagePanel(
                "/Users/alicemckinley/NetBeansProjects/movie/src/main/java/com/mycompany/movie/images/homepagebg.png");
        homePanel.setLayout(null);

        container.revalidate();
        container.repaint();

        JButton navSavedButton = new JButton("Open Saved List");
        JButton navWatchedButton = new JButton("Open Watched List");

        navSavedButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(panel, "Saved Page");
                container.revalidate();
                container.repaint();
            }
        });

        navWatchedButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(panel, "Watched Page");
                container.revalidate();
                container.repaint();

            }
        });

        movies = controller.getMovies();
        insertionSort(movies);

        JPanel movieListPanel = new JPanel();
        movieListPanel.setLayout(new BoxLayout(movieListPanel, BoxLayout.X_AXIS));
        movieListPanel.setBackground(new Color(255, 248, 226));

        ArrayList<JButton> buttons = new ArrayList<JButton>();

        for (Movie movie : movies) {

            JPanel movieCard = new JPanel(); 
            movieCard.setLayout(new BoxLayout(movieCard, BoxLayout.Y_AXIS));
            movieCard.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            movieCard.setBackground(new Color(83, 84, 85)); 

            JLabel titleLabel = new JLabel(movie.title);
            titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
            titleLabel.setForeground(Color.WHITE);
            titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            JButton goButton = new JButton("VIEW");
            buttons.add(goButton);
            goButton.setAlignmentX(Component.CENTER_ALIGNMENT);

            goButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    buttonIndex = buttons.indexOf(goButton);
                    updateMoviePage(false); 
                    cardLayout.show(panel, "Movie Page");
                    container.revalidate();
                    container.repaint();
                }
            });

            BufferedImage placeholderImage = null;
            try {
                placeholderImage = ImageIO.read(new File(
                        "/Users/alicemckinley/NetBeansProjects/movie/src/main/java/com/mycompany/movie/images/placeholder.png"));
            } catch (IOException e) {
                System.out.println("Placeholder image not found.");
            }

            if (placeholderImage != null) {
                ImageIcon icon = new ImageIcon(placeholderImage.getScaledInstance(150, 200, Image.SCALE_SMOOTH));
                JLabel imageLabel = new JLabel(icon);
                imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                movieCard.add(imageLabel);
            }

            movieCard.add(Box.createRigidArea(new Dimension(0, 10))); 
            movieCard.add(titleLabel);
            movieCard.add(Box.createRigidArea(new Dimension(0, 10)));
            movieCard.add(goButton);
            movieCard.setMaximumSize(new Dimension(180, 300));
            movieListPanel.add(movieCard);
            movieListPanel.add(Box.createRigidArea(new Dimension(20, 0))); 
        }

        JScrollPane scrollPane = new JScrollPane(movieListPanel);
        scrollPane.setBounds(30, 270, 1200, 320);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);

        homePanel.add(scrollPane);

        searchBarPanel.add(navSavedButton);
        navSavedButton.setBounds(250, 10, 150, 30);
        searchBarPanel.add(navWatchedButton);
        navWatchedButton.setBounds(450, 10, 150, 30);

        return homePanel;
    }
   
    /*
    void updateMoviePage(boolean searched) takes a boolean for whether the home page is displaying searched movies or not
    It doesn't return anything
    It updates the detailed movie page depending on which movie needs to be displayed
    */
    public void updateMoviePage(boolean searched) {
        if (!searched) {
            if (movies != null && !movies.isEmpty() && buttonIndex < movies.size()) {
                //new
                insertionSort(movies);
                Movie movie = movies.get(buttonIndex);
                selectedMovie = movie;
                selectedMovieTitle.setText(movie.title);
                selectedMovieDescription.setText("<html><body style='width: 800px'>" + movie.longDescription + "</body></html>");

                if (ratingDropdown != null) { 
                    if (movie.isRated()) {
                        ratingDropdown.setSelectedIndex(((int) ((movie.rating - 0.5) / 0.5)) - 1);
                    } else {
                        ratingDropdown.setSelectedIndex(-1); 
                    }
                } 
                moviePanel.revalidate();
                moviePanel.repaint();
            }
        } else {
            if (searchResults != null && !searchResults.isEmpty() && buttonIndex < searchResults.size()) {
                //new
                insertionSort(searchResults);
                Movie movie = searchResults.get(buttonIndex);
                selectedMovie = movie;
                selectedMovieTitle.setText(movie.title);
                selectedMovieDescription.setText("<html><body style='width: 800px'>" + movie.longDescription + "</body></html>");

                if (ratingDropdown != null) { 
                    if (movie.isRated()) {
                        ratingDropdown.setSelectedIndex(((int) ((movie.rating - 0.5) / 0.5)) - 1);
                    } else {
                        ratingDropdown.setSelectedIndex(-1); 
                    }
                } 
                moviePanel.revalidate();
                moviePanel.repaint();
            }
        }
    }

    /**
     * Creates an expanded movie page
     * 
     * @return ImagePanel of movie page
     */
    public ImagePanel createMoviePage() {
        moviePanel = new ImagePanel("/Users/alicemckinley/NetBeansProjects/movie/src/main/java/com/mycompany/movie/images/homepagebg.png");
        moviePanel.setLayout(null);

        selectedMovieTitle = new JLabel();
        selectedMovieTitle.setBounds(100, 300, 800, 50);
        selectedMovieTitle.setFont(new Font("SansSerif", Font.BOLD, 24));

        selectedMovieDescription = new JLabel();
        selectedMovieDescription.setBounds(100, 370, 1500, 200);
        selectedMovieDescription.setFont(new Font("SansSerif", Font.PLAIN, 18));
        selectedMovieDescription.setVerticalAlignment(JLabel.TOP);

        moviePanel.add(selectedMovieTitle);
        moviePanel.add(selectedMovieDescription);

        JButton backButton = new JButton("Back to Home Page");
        backButton.setBounds(50, 55, 150, 30);
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(panel, "Home Page");
                container.revalidate();
                container.repaint();
            }  
        });
        moviePanel.add(backButton);

        JButton saveMovieButton = new JButton("Save/Unsave Movie");
        saveMovieButton.setBounds(100, 500, 170, 40);
        saveMovieButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.saveMovie(selectedMovie);
                updateSavedPage();
            }
        });

        moviePanel.add(saveMovieButton);

        JLabel ratingLabel = new JLabel("Rate movie: ");
        ratingLabel.setBounds(815, 267, 100, 100);
        ratingLabel.setFont(new Font("SansSerif", Font.BOLD, 15));
        moviePanel.add(ratingLabel);

        ratingDropdown = new JComboBox<>(new String[] {"1", "1.5", "2", "2.5", "3", "3.5", "4", "4.5", "5"});
        ratingDropdown.setBounds(910, 305, 100, 30);
        ratingDropdown.setSelectedIndex(-1);
        moviePanel.add(ratingDropdown);
        ratingDropdown.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (ratingDropdown.getSelectedItem() != null) {
                    double rating = Double.parseDouble((String) ratingDropdown.getSelectedItem());
                    controller.rateMovie(selectedMovie, (double) rating);
                    updateWatchedPage();
                }
            }
        });

        return moviePanel;
    }

    /*
    void updateSavedPage() takes no inputs
    It updates the saved page with the currently saved movies
    */
    public void updateSavedPage() {
        JPanel savedPanel = (JPanel) panel.getComponent(3);
        savedPanel.removeAll();

        JButton backButton = new JButton("Back to Home Page");
        backButton.setBounds(50, 55, 150, 30);
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(panel, "Home Page");
                container.revalidate();
                container.repaint();
            }
        });

        savedPanel.add(backButton);

        JPanel movieListPanel = new JPanel();
        movieListPanel.setLayout(new BoxLayout(movieListPanel, BoxLayout.X_AXIS));
        movieListPanel.setBackground(new Color(255, 248, 226));

        ArrayList<Movie> savedMovies = new ArrayList<>();

        for (Movie movie : movies) {
            if (movie.saved == true) {
                savedMovies.add(movie);
            }
        }

        for (Movie movie : savedMovies) {
            JPanel movieCard = new JPanel();
            movieCard.setLayout(new BoxLayout(movieCard, BoxLayout.Y_AXIS));
            movieCard.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            movieCard.setBackground(new Color(83, 84, 85));

            JLabel titleLabel = new JLabel(movie.title);
            titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
            titleLabel.setForeground(Color.WHITE);
            titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);


            movieCard.add(titleLabel);
            movieCard.add(Box.createRigidArea(new Dimension(0, 10)));

            BufferedImage placeholderImage = null;
            try {
                placeholderImage = ImageIO.read(new File("/Users/alicemckinley/NetBeansProjects/movie/src/main/java/com/mycompany/movie/images/placeholder.png"));
            } catch (IOException e) {
                System.out.println("Placeholder image not found.");
            }

            if (placeholderImage != null) {
                ImageIcon icon = new ImageIcon(placeholderImage.getScaledInstance(150, 200, Image.SCALE_SMOOTH));
                JLabel imageLabel = new JLabel(icon);
                imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                movieCard.add(imageLabel);
            }

            movieCard.setMaximumSize(new Dimension(180, 300));
            movieListPanel.add(movieCard);
            movieListPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        }

        JScrollPane scrollPane = new JScrollPane(movieListPanel);
        scrollPane.setBounds(30, 270, 1200, 320);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);

        savedPanel.add(scrollPane);
        savedPanel.revalidate();
        savedPanel.repaint();
    }
    
    /**
     * Creates page containing saved movies
     * 
     * @return ImagePanel of saved page
     */
    public ImagePanel createSavedPage() {
        ImagePanel savedPanel = new ImagePanel("/Users/alicemckinley/NetBeansProjects/movie/src/main/java/com/mycompany/movie/images/homepagebg.png");
        savedPanel.setLayout(null);

        JButton backButton = new JButton("Back to Home Page");
        backButton.setBounds(50, 55, 150, 30);
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(panel, "Home Page");
                container.revalidate();
                container.repaint();

            }
        });

        savedPanel.add(backButton);
        
        movies = controller.getMovies();

        JPanel movieListPanel = new JPanel();
        movieListPanel.setLayout(new BoxLayout(movieListPanel, BoxLayout.X_AXIS));
        movieListPanel.setBackground(new Color(255, 248, 226));

        ArrayList<JButton> buttons = new ArrayList<JButton>();
        
        for (Movie movie : movies) {
            if (movie.saved) {
                JPanel movieCard = new JPanel(); 
                movieCard.setLayout(new BoxLayout(movieCard, BoxLayout.Y_AXIS));
                movieCard.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                movieCard.setBackground(new Color(83, 84, 85)); 

                JLabel titleLabel = new JLabel(movie.title);
                titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
                titleLabel.setForeground(Color.WHITE);
                titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

                JButton goButton = new JButton("VIEW");
                buttons.add(goButton);
                goButton.setAlignmentX(Component.CENTER_ALIGNMENT);

                BufferedImage placeholderImage = null;
                try {
                    placeholderImage = ImageIO.read(new File("/Users/alicemckinley/NetBeansProjects/movie/src/main/java/com/mycompany/movie/images/placeholder.png"));
                } catch (IOException e) {
                    System.out.println("Placeholder image not found.");
                }

                if (placeholderImage != null) {
                    ImageIcon icon = new ImageIcon(placeholderImage.getScaledInstance(150, 200, Image.SCALE_SMOOTH));
                    JLabel imageLabel = new JLabel(icon);
                    imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                    movieCard.add(imageLabel);
                }

                movieCard.add(Box.createRigidArea(new Dimension(0, 10))); 
                movieCard.add(titleLabel);
                movieCard.add(Box.createRigidArea(new Dimension(0, 10)));
                movieCard.add(goButton);
                movieCard.setMaximumSize(new Dimension(180, 300));
                movieListPanel.add(movieCard);
                movieListPanel.add(Box.createRigidArea(new Dimension(20, 0))); 
            }

            JScrollPane scrollPane = new JScrollPane(movieListPanel);
            scrollPane.setBounds(30, 270, 1200, 320);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
            scrollPane.setBorder(null);
        }
        
        return savedPanel;
    }
    
    /*
    void updateWatchedPage() takes no inputs
    It updates the watched page with the currently rated movies
    */
    public void updateWatchedPage() {
        JPanel watchedPanel = (JPanel) panel.getComponent(4); 
        watchedPanel.removeAll();

        JButton backButton = new JButton("Back to Home Page");
        backButton.setBounds(50, 55, 150, 30);
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(panel, "Home Page");
                container.revalidate();
                container.repaint();
            }
        });

        watchedPanel.add(backButton);

        JPanel movieListPanel = new JPanel();
        movieListPanel.setLayout(new BoxLayout(movieListPanel, BoxLayout.X_AXIS));
        movieListPanel.setBackground(new Color(255, 248, 226));

        ArrayList<Movie> ratedMovies = controller.getRatedMovies();

        for (Movie movie : ratedMovies) {
            JPanel movieCard = new JPanel();
            movieCard.setLayout(new BoxLayout(movieCard, BoxLayout.Y_AXIS));
            movieCard.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            movieCard.setBackground(new Color(83, 84, 85));

            JLabel titleLabel = new JLabel(movie.title);
            titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
            titleLabel.setForeground(Color.WHITE);
            titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);


            movieCard.add(titleLabel);
            movieCard.add(Box.createRigidArea(new Dimension(0, 10)));

            BufferedImage placeholderImage = null;
            try {
                placeholderImage = ImageIO.read(new File("/Users/alicemckinley/NetBeansProjects/movie/src/main/java/com/mycompany/movie/images/placeholder.png"));
            } catch (IOException e) {
                System.out.println("Placeholder image not found.");
            }

            if (placeholderImage != null) {
                ImageIcon icon = new ImageIcon(placeholderImage.getScaledInstance(150, 200, Image.SCALE_SMOOTH));
                JLabel imageLabel = new JLabel(icon);
                imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                movieCard.add(imageLabel);
            }

            JLabel ratingLabel = new JLabel("Rating: " + movie.rating);
            ratingLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
            ratingLabel.setForeground(Color.WHITE);
            ratingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            movieCard.add(ratingLabel);

            movieCard.setMaximumSize(new Dimension(180, 300));
            movieListPanel.add(movieCard);
            movieListPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        }

        JScrollPane scrollPane = new JScrollPane(movieListPanel);
        scrollPane.setBounds(30, 270, 1200, 320);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);

        watchedPanel.add(scrollPane);
        watchedPanel.revalidate();
        watchedPanel.repaint();
    }

    /**
     * Creates page containing rated movies
     * 
     * @return ImagePanel of watched page
     */
    public ImagePanel createWatchedPage() {
        ImagePanel watchedPanel = new ImagePanel("/Users/alicemckinley/NetBeansProjects/movie/src/main/java/com/mycompany/movie/images/homepagebg.png");
        watchedPanel.setLayout(null);

        JButton backButton = new JButton("Back to Home Page");
        backButton.setBounds(50, 55, 150, 30);
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(panel, "Home Page");
                container.revalidate();
                container.repaint();
            }
        });

        watchedPanel.add(backButton);
        
        ArrayList<Movie> ratedMovies = controller.getRatedMovies();

        JPanel movieListPanel = new JPanel();
        movieListPanel.setLayout(new BoxLayout(movieListPanel, BoxLayout.X_AXIS));
        movieListPanel.setBackground(new Color(255, 248, 226));
        
        
        for (Movie movie : ratedMovies) {
            JPanel movieCard = new JPanel(); 
            movieCard.setLayout(new BoxLayout(movieCard, BoxLayout.Y_AXIS));
            movieCard.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            movieCard.setBackground(new Color(83, 84, 85)); 

            JLabel titleLabel = new JLabel(movie.title);
            titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
            titleLabel.setForeground(Color.WHITE);
            titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            BufferedImage placeholderImage = null;
            try {
                placeholderImage = ImageIO.read(new File("/Users/alicemckinley/NetBeansProjects/movie/src/main/java/com/mycompany/movie/images/placeholder.png"));
            } catch (IOException e) {
                System.out.println("Placeholder image not found.");
            }

            if (placeholderImage != null) {
                ImageIcon icon = new ImageIcon(placeholderImage.getScaledInstance(150, 200, Image.SCALE_SMOOTH));
                JLabel imageLabel = new JLabel(icon);
                imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                movieCard.add(imageLabel);
            }

            movieCard.add(Box.createRigidArea(new Dimension(0, 10))); 
            movieCard.add(titleLabel);
            movieCard.add(Box.createRigidArea(new Dimension(0, 10)));

            movieCard.setMaximumSize(new Dimension(180, 300));
            movieListPanel.add(movieCard);
            movieListPanel.add(Box.createRigidArea(new Dimension(20, 0))); 
        }

        JScrollPane scrollPane = new JScrollPane(movieListPanel);
        scrollPane.setBounds(30, 270, 1200, 320);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);

        return watchedPanel;
    }
}
