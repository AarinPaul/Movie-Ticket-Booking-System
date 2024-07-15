//Movie Ticket Booking System

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.sql.*;

public class MainUI extends JFrame {

     JList<String> movieList;
     JTextArea bookedTicketsArea;
     JTextField nameField, emailField, phoneField;
     
     Connection Con; //Creating a Connection variable to connect to DB 
     
     //Constructor
     MainUI() {
          //Database Connection
          try{
               Con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Movies",
               "YOUR DB USERNAME","YOUR DB PASSWORD");
          }
          catch (SQLException e) {
               e.printStackTrace();
               JOptionPane.showMessageDialog(this, "Database connection failed: " + e.getMessage(),
               "Error", JOptionPane.ERROR_MESSAGE);
          }

          setTitle("Movie Ticket Booking System");
          setSize(800, 500);
          setVisible(true);
          getContentPane().setBackground(Color.LIGHT_GRAY);
          setLocationRelativeTo(null);
          setLayout(new BorderLayout());
          setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

          //Creating a mainPanel with GridBagLayout
          JPanel mainPanel = new JPanel(new GridBagLayout());
          mainPanel.setBackground(Color.LIGHT_GRAY);
          GridBagConstraints gbcs = new GridBagConstraints();
          gbcs.insets = new Insets(5,5,5,5);

          //Label and TextFields
          JLabel nameLabel = new JLabel("Name :");
          gbcs.gridx = 0;
          gbcs.gridy = 0;
          mainPanel.add(nameLabel,gbcs);

          nameField = new JTextField(20);
          gbcs.gridx = 1;
          mainPanel.add(nameField,gbcs);

          JLabel emailLabel = new JLabel("Email :");
          gbcs.gridx = 0;
          gbcs.gridy = 1;
          mainPanel.add(emailLabel,gbcs);
          
          emailField = new JTextField(20);
          gbcs.gridx = 1;
          mainPanel.add(emailField,gbcs);

          JLabel phoneLabel = new JLabel("Phone :");
          gbcs.gridx = 0;
          gbcs.gridy = 2;
          mainPanel.add(phoneLabel,gbcs);

          phoneField = new JTextField(20);
          gbcs.gridx = 1;
          mainPanel.add(phoneField,gbcs);

          //Movies List
          JLabel movieLabel = new JLabel("Available Movies :");
          gbcs.gridx = 0;
          gbcs.gridy = 3;
          mainPanel.add(movieLabel,gbcs);

          movieList = new JList<>(getMovieTitles());
          JScrollPane movieScrollPane = new JScrollPane(movieList);
          gbcs.gridx = 1;
          gbcs.gridy = 3;
          gbcs.gridheight = 2;
          mainPanel.add(movieScrollPane,gbcs);
          gbcs.gridheight = 1;

          //Booked Tickets Area
          JLabel ticketsLabel = new JLabel("Your Tickets :");
          gbcs.gridx = 0;
          gbcs.gridy = 4;
          mainPanel.add(ticketsLabel,gbcs);

          bookedTicketsArea = new JTextArea(10,30);
          bookedTicketsArea.setEditable(false);
          JScrollPane ticketScrollPane = new JScrollPane(bookedTicketsArea);
          gbcs.gridx = 1;
          gbcs.gridy = 5;
          mainPanel.add(ticketScrollPane,gbcs);

          add(mainPanel,BorderLayout.CENTER);

          //Buttons
          JButton bookButton = new JButton("Book Ticket");
          JButton viewButton = new JButton("View Your Ticket");
          JButton cancelButton = new JButton("Cancel Ticket");
          
          JPanel buttonsPanel = new JPanel();
          buttonsPanel.setPreferredSize(new Dimension(getWidth(),38));
          buttonsPanel.add(bookButton);
          buttonsPanel.add(viewButton);
          buttonsPanel.add(cancelButton);
          add(buttonsPanel,BorderLayout.SOUTH);

          //ActionListeners
          bookButton.addActionListener(new ActionListener() {
               public void actionPerformed(ActionEvent e){
                    bookTickets();
               }
          });

          viewButton.addActionListener(new ActionListener() {
               public void actionPerformed(ActionEvent e){
                    viewTickets();
               }
          });

          cancelButton.addActionListener(new ActionListener() {
               public void actionPerformed(ActionEvent e){
                    cancelTickets();
               }
          });
     } 
     
     //Functions to get Movie Titles from DB
     private String[] getMovieTitles() {
          ArrayList<String> movieTitles = new ArrayList<>();
          try{
               Statement stmt = Con.createStatement(); //used for executing a static SQL statement
               ResultSet rs = stmt.executeQuery("SELECT title FROM movieLists"); //used to execute the SQL DB Query statement
               while (rs.next()){
                    movieTitles.add(rs.getString("title"));
               }
               rs.close(); //closing the Query statement
               stmt.close(); //closing the static SQL statement
          }
          catch(SQLException e){
               e.printStackTrace();
               JOptionPane.showMessageDialog(this, "Failed to fetch movie titles: " + e.getMessage(),
               "Error", JOptionPane.ERROR_MESSAGE);
          }
          return movieTitles.toArray(new String[0]);
     }

     //Function to Book Tickets
     private void bookTickets(){
          String name = nameField.getText();
          String email = emailField.getText();
          String phone = phoneField.getText();
          String selectedMovie = movieList.getSelectedValue();

          if(name.isEmpty() || email.isEmpty() || phone.isEmpty() || selectedMovie == null){
               JOptionPane.showMessageDialog(this, "Please fill all fields and select a movie.",
               "Error", JOptionPane.ERROR_MESSAGE); 
                
               return;   
          }

          try{
               //Get Movie id for the selected Movie
               //PreparedStatement used for executing a precompiled SQL statement
               PreparedStatement movieStmt = Con.prepareStatement("SELECT id FROM movieLists WHERE title = ?");
               movieStmt.setString(1, selectedMovie);
               ResultSet movieRs = movieStmt.executeQuery();
               int movieID = -1;
               if(movieRs.next()){
                    movieID = movieRs.getInt("id");
               }
               movieRs.close();
               movieStmt.close();

               if(movieID == -1){
                    JOptionPane.showMessageDialog(this, "Selected movie not found.", 
                    "Error", JOptionPane.ERROR_MESSAGE);

                    return;
               }

               //Insert Tickets into Ticket Table of DB
               PreparedStatement pStmt = Con.prepareStatement("INSERT INTO tickets (movie_id, name, email, phone) VALUES (?, ?, ?, ?)");
               pStmt.setInt(1, movieID);
               pStmt.setString(2, name);
               pStmt.setString(3, email);
               pStmt.setString(4, phone);
               pStmt.executeUpdate();
               pStmt.close();

               JOptionPane.showMessageDialog(this, "Ticket booked successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
          }

          catch (SQLException e){
               e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to book ticket: " + e.getMessage(), 
            "Error", JOptionPane.ERROR_MESSAGE);
          }
     }

     //Function to View Tickets
     private void viewTickets(){
          bookedTicketsArea.setText(""); //Clear Previous Tickets
          String email = emailField.getText();

          if (email.isEmpty()){
               JOptionPane.showMessageDialog(this, "Please enter your email to view tickets.", "Error", JOptionPane.ERROR_MESSAGE);
               return; 
          }

          try{
               PreparedStatement pStmt = Con.prepareStatement("SELECT t.name, t.email, t.phone, m.title " +
               "FROM tickets t JOIN movieLists m ON t.movie_id = m.id " +
               "WHERE t.email = ?");

               pStmt.setString(1, email);
               ResultSet rs = pStmt.executeQuery();
               while(rs.next()){
                    bookedTicketsArea.append("Name: " + rs.getString("name") + "\n");
                    bookedTicketsArea.append("Email: " + rs.getString("email") + "\n");
                    bookedTicketsArea.append("Phone: " + rs.getString("phone") + "\n");
                    bookedTicketsArea.append("Movie: " + rs.getString("title") + "\n");
                    bookedTicketsArea.append("--------------------------\n"); 
               }
               rs.close();
               pStmt.close();
          }

          catch(SQLException e){
               e.printStackTrace();
               JOptionPane.showMessageDialog(this, "Failed to fetch tickets: " + e.getMessage(), 
               "Error", JOptionPane.ERROR_MESSAGE); 
          }
     }

     //Function to Cancel Tickets 
     private void cancelTickets(){
          String email = emailField.getText();
          String selectedMovie = movieList.getSelectedValue();

          if(email.isEmpty() || selectedMovie == null){
               JOptionPane.showMessageDialog(this, "Please enter your email and select a movie to cancel.", 
               "Error", JOptionPane.ERROR_MESSAGE); 

               return;
          }

          try{
               //Get the movie id of the selected movie
               PreparedStatement movieStmt = Con.prepareStatement("SELECT id FROM movieLists WHERE title = ?");
               movieStmt.setString(1, selectedMovie);
               ResultSet movieRs = movieStmt.executeQuery();

               int movieId = -1;
               if(movieRs.next()){
                    movieId = movieRs.getInt("id"); 
               }
               movieRs.close();
               movieStmt.close();

               if(movieId == -1){
                    JOptionPane.showMessageDialog(this, "Selected movie not found.", 
                    "Error", JOptionPane.ERROR_MESSAGE);

                    return;
               }

               //Delete tickets from the DB ticket table
               PreparedStatement pstmt = Con.prepareStatement("DELETE FROM tickets WHERE email = ? AND movie_id = ?");
               pstmt.setString(1, email);
               pstmt.setInt(2, movieId);
               int rowsAffected = pstmt.executeUpdate();
               pstmt.close();

               if(rowsAffected > 0){
                    JOptionPane.showMessageDialog(this, "Ticket canceled successfully!", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
               }
               else{
                    JOptionPane.showMessageDialog(this, "No booking found for the given email and movie.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
               }
          }

          catch(SQLException e){
               e.printStackTrace();
               JOptionPane.showMessageDialog(this, "Failed to cancel ticket: " + e.getMessage(), 
               "Error", JOptionPane.ERROR_MESSAGE);
          }
     }

     public static void main(String[] args) {
          @SuppressWarnings("unused")
          MainUI m = new MainUI();
     }
}
