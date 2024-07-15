
# Movie Ticket Booking System

This project is a simple Movie Ticket Booking System built using Java Swing for the GUI and MySQL for the database. Users can book, view, and cancel movie tickets through the graphical interface.


## Table of Contents

 - Prerequisites
 - Database Setup
- Installation
- Usage
- Code Overview
- License


## Prerequisites

Before you begin, ensure you have met the following requirements:

- Java Development Kit (JDK) installed on your machine.
- MySQL database server installed and running.
- A MySQL user with access to create databases and tables.

## Database Setup

- Open your MySQL command line or MySQL Workbench.
- Create the Movies database and tables by executing the following SQL script:

```bash
CREATE DATABASE Movies;

USE Movies;

CREATE TABLE movieLists (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    avaiable_seats INT NOT NULL
);

CREATE TABLE tickets (
    id INT AUTO_INCREMENT PRIMARY KEY,
    movie_id INT,
    FOREIGN KEY (movie_id) REFERENCES movieLists(id),
    name VARCHAR(255),
    email VARCHAR(255),
    phone VARCHAR(255)
);

INSERT INTO movieLists (title, avaiable_seats) VALUES 
('JURASSIC PARK', 100),
('SCORPION', 50),
('IRON MAN 2', 200),
('MANJUMMEL BOYS', 100),
('SCAM:1992', 150);

```


## Installation

- Clone this repository to your local machine:

```bash
  https://github.com/AarinPaul/Movie-Ticket-Booking-System.git
```
- Navigate to the project directory:

```bash
  cd MovieTicketBookingSystem
```
- Open the project in your preferred Java IDE (such as IntelliJ IDEA, Eclipse, or NetBeans).

- Ensure you have the MySQL JDBC driver added to your project's classpath. You can download it from [here](https://dev.mysql.com/downloads/connector/j/).

- Update the database connection details in the MainUI class if necessary:

```bash
  Con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Movies", 
  "DB UserName", "Your Password");
```
## Usage/Examples

- Run the MainUI class from your IDE.

- The application window will open, allowing you to:

    - Book a Ticket: Enter your name, email, phone number, select a movie from the list, and click "Book Ticket".

    - View Your Tickets: Enter your email and click "View Your Tickets" to see all your booked tickets.

    - Cancel a Ticket: Enter your email, select a movie from the list, and click "Cancel Ticket" to cancel your booking.



## Code Overview

- MainUI: The main class that sets up the GUI and handles user interactions.
- Database Connection: Establishes a connection to the MySQL database.
- Movie List: Retrieves the list of movies from the database.
- Book Tickets: Inserts a new ticket into the tickets table.
- View Tickets: Retrieves and displays the tickets booked by a user.
- Cancel Tickets: Deletes a ticket from the tickets table based on the user's email and selected movie.
## License

This project is licensed under the [GPL-3.0 LICENSE](https://github.com/AarinPaul/Movie-Ticket-Booking-System/blob/main/LICENSE). See the LICENSE file for details

