package com.movieticket;

import com.movieticket.dao.BookedSeatDAO;
import com.movieticket.dao.BookingDAO;
import com.movieticket.dao.MovieDAO;
import com.movieticket.dao.PaymentDAO;
import com.movieticket.dao.SeatDAO;
import com.movieticket.dao.ShowDAO;
import com.movieticket.dao.TheatreDAO;
import com.movieticket.dao.UserDAO;
import com.movieticket.service.BookedSeatService;
import com.movieticket.service.BookingService;
import com.movieticket.service.MovieService;
import com.movieticket.service.PaymentService;
import com.movieticket.service.SeatService;
import com.movieticket.service.ShowService;
import com.movieticket.service.TheatreService;
import com.movieticket.service.UserService;
import com.movieticket.model.User;
import com.movieticket.model.Movie;
import com.movieticket.model.Theatre;
import com.movieticket.model.Seat;
import java.math.BigDecimal;
import com.movieticket.model.Show;
import java.time.LocalDate;
import java.time.LocalTime;
import com.movieticket.model.Booking;
import java.time.LocalDateTime;
import com.movieticket.model.BookedSeat;
import com.movieticket.model.Payment;
import com.movieticket.exception.MovieTicketException;

import java.util.Scanner;
import java.util.logging.Logger;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.List;

public class Main {

    private static final Logger logger =
            Logger.getLogger(Main.class.getName());

    private static void configureLogger() {

        Logger rootLogger = Logger.getLogger("");

        for (Handler handler : rootLogger.getHandlers()) {

            handler.setFormatter(new Formatter() {

                @Override
                public String format(LogRecord record) {

                    return record.getLevel()
                            + ": "
                            + record.getMessage()
                            + System.lineSeparator();
                }
            });
        }
    }

    public static void main(String[] args) {
        configureLogger();

        // DAO objects
        UserDAO userDAO = new UserDAO();
        MovieDAO movieDAO = new MovieDAO();
        TheatreDAO theatreDAO = new TheatreDAO();
        SeatDAO seatDAO = new SeatDAO();
        ShowDAO showDAO = new ShowDAO();
        BookingDAO bookingDAO = new BookingDAO();
        BookedSeatDAO bookedSeatDAO = new BookedSeatDAO();
        PaymentDAO paymentDAO = new PaymentDAO();

        // Service objects
        UserService userService = new UserService(userDAO);
        MovieService movieService = new MovieService(movieDAO);
        TheatreService theatreService = new TheatreService(theatreDAO);
        SeatService seatService = new SeatService(seatDAO);
        ShowService showService = new ShowService(showDAO);
        BookingService bookingService = new BookingService(bookingDAO);
        BookedSeatService bookedSeatService = new BookedSeatService(bookedSeatDAO);
        PaymentService paymentService = new PaymentService(paymentDAO);

        try (Scanner scanner = new Scanner(System.in)) {

            logger.info("Movie Ticket Booking System started successfully.");

            boolean running = true;

            while (running) {

                try {

                    displayMainMenu();

                    logger.info("Enter your choice:");

                    int choice = Integer.parseInt(scanner.nextLine());

                    switch (choice) {

                        case 1 -> userManagement(scanner, userService);

                        case 2 -> movieManagement(scanner, movieService);

                        case 3 -> theatreManagement(scanner, theatreService);

                        case 4 -> seatManagement(scanner, seatService);

                        case 5 -> showManagement(scanner, showService);

                        case 6 -> bookingManagement(
                                scanner,
                                bookingService,
                                bookedSeatService,
                                movieService,
                                theatreService,
                                showService,
                                seatService,
                                paymentService
                        );

                        case 7 -> bookedSeatManagement(scanner, bookedSeatService);

                        case 8 -> paymentManagement(scanner, paymentService);

                        case 0 -> {
                            running = false;
                            logger.info("Movie Ticket Booking System closed.");
                        }

                        default -> logger.warning(
                                "Invalid choice. Please enter a number from 0 to 8."
                        );
                    }

                } catch (MovieTicketException e) {

                    logger.warning(e.getMessage());

                } catch (NumberFormatException e) {

                    logger.warning("Please enter a valid number.");

                } catch (Exception e) {

                    logger.severe(
                            "Unexpected application error: " + e.getMessage()
                    );
                }
            }

        } catch (Exception e) {

            logger.severe(
                    "Application could not start: " + e.getMessage()
            );
        }
    }

    private static void displayMainMenu() {

        logger.info("""
                
                ========================================
                   MOVIE TICKET BOOKING SYSTEM
                ========================================
                
                1. User Management
                2. Movie Management
                3. Theatre Management
                4. Seat Management
                5. Show Management
                6. Booking Management
                7. Booked Seat Management
                8. Payment Management
                0. Exit
                
                ========================================
                """);
    }
    private static void userManagement(
            Scanner scanner,
            UserService userService) {

        boolean running = true;

        while (running) {

            logger.info("""
                
                ========================================
                     USER MANAGEMENT
                ========================================
                
                1. Add User
                2. Get User By ID
                3. Get All Users
                4. Update User
                5. Delete User
                0. Back
                
                ========================================
                """);

            logger.info("Enter your choice:");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {

                case 1 -> addUser(scanner, userService);

                case 2 -> getUserById(scanner, userService);

                case 3 -> getAllUsers(userService);

                case 4 -> updateUser(scanner, userService);

                case 5 -> deleteUser(scanner, userService);

                case 0 -> {
                    running = false;
                    logger.info("Returning to main menu.");
                }

                default -> logger.warning(
                        "Invalid choice. Please try again."
                );
            }
        }
    }
    private static void addUser(
            Scanner scanner,
            UserService userService) {

        logger.info("Enter user name:");
        String name = scanner.nextLine();

        logger.info("Enter email:");
        String email = scanner.nextLine();

        logger.info("Enter phone:");
        String phone = scanner.nextLine();

        logger.info("Enter password:");
        String password = scanner.nextLine();

        logger.info("Enter role:");
        String role = scanner.nextLine();

        User user = new User(
                name,
                email,
                phone,
                password,
                role
        );

        userService.addUser(user);

        logger.info("User added successfully through console.");
    }


    private static void getUserById(
            Scanner scanner,
            UserService userService) {

        logger.info("Enter user ID:");

        int userId = scanner.nextInt();
        scanner.nextLine();

        User user = userService.getUserById(userId);

        if (user != null) {

            logger.info("""
                
                -------- USER DETAILS --------
                User ID: %d
                Name: %s
                Email: %s
                Phone: %s
                Role: %s
                ------------------------------
                """.formatted(
                    user.getUserId(),
                    user.getName(),
                    user.getEmail(),
                    user.getPhone(),
                    user.getRole()
            ));
        }
    }


    private static void getAllUsers(
            UserService userService) {

        var users = userService.getAllUsers();

        if (users.isEmpty()) {
            logger.info("No users found.");
            return;
        }

        logger.info("-------- ALL USERS --------");

        for (User user : users) {

            logger.info("""
                
                User ID: %d
                Name: %s
                Email: %s
                Phone: %s
                Role: %s
                ------------------------
                """.formatted(
                    user.getUserId(),
                    user.getName(),
                    user.getEmail(),
                    user.getPhone(),
                    user.getRole()
            ));
        }
    }


    private static void updateUser(
            Scanner scanner,
            UserService userService) {

        logger.info("Enter user ID to update:");

        int userId = scanner.nextInt();
        scanner.nextLine();

        User user = userService.getUserById(userId);

        if (user == null) {
            logger.warning("User not found.");
            return;
        }

        logger.info("Enter new name:");
        user.setName(scanner.nextLine());

        logger.info("Enter new email:");
        user.setEmail(scanner.nextLine());

        logger.info("Enter new phone:");
        user.setPhone(scanner.nextLine());

        logger.info("Enter new password:");
        user.setPassword(scanner.nextLine());

        logger.info("Enter new role:");
        user.setRole(scanner.nextLine());

        userService.updateUser(user);

        logger.info("User updated successfully through console.");
    }


    private static void deleteUser(
            Scanner scanner,
            UserService userService) {

        logger.info("Enter user ID to delete:");

        int userId = scanner.nextInt();
        scanner.nextLine();

        userService.deleteUser(userId);

        logger.info("User deleted successfully through console.");
    }
    private static void movieManagement(
            Scanner scanner,
            MovieService movieService) {

        boolean running = true;

        while (running) {

            logger.info("""
                
                ========================================
                     MOVIE MANAGEMENT
                ========================================
                
                1. Add Movie
                2. Get Movie By ID
                3. Get All Movies
                4. Update Movie
                5. Delete Movie
                0. Back
                
                ========================================
                """);

            logger.info("Enter your choice:");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {

                case 1 -> addMovie(scanner, movieService);

                case 2 -> getMovieById(scanner, movieService);

                case 3 -> getAllMovies(movieService);

                case 4 -> updateMovie(scanner, movieService);

                case 5 -> deleteMovie(scanner, movieService);

                case 0 -> {
                    running = false;
                    logger.info("Returning to main menu.");
                }

                default -> logger.warning(
                        "Invalid choice. Please try again."
                );
            }
        }
    }
    private static void addMovie(
            Scanner scanner,
            MovieService movieService) {

        logger.info("Enter movie title:");
        String title = scanner.nextLine();

        logger.info("Enter language:");
        String language = scanner.nextLine();

        logger.info("Enter genre:");
        String genre = scanner.nextLine();

        logger.info("Enter duration in minutes:");
        int duration = scanner.nextInt();
        scanner.nextLine();

        logger.info("Enter release date (YYYY-MM-DD):");
        String releaseDateInput = scanner.nextLine();

        java.time.LocalDate releaseDate =
                java.time.LocalDate.parse(releaseDateInput);

        Movie movie = new Movie(
                title,
                language,
                genre,
                duration,
                releaseDate
        );

        movieService.addMovie(movie);

        logger.info("Movie added successfully through console.");
    }
    private static void getMovieById(
            Scanner scanner,
            MovieService movieService) {

        logger.info("Enter movie ID:");

        int movieId = scanner.nextInt();
        scanner.nextLine();

        Movie movie = movieService.getMovieById(movieId);

        if (movie != null) {

            logger.info("""
                
                -------- MOVIE DETAILS --------
                Movie ID: %d
                Title: %s
                Language: %s
                Genre: %s
                Duration: %d minutes
                Release Date: %s
                -------------------------------
                """.formatted(
                    movie.getMovieId(),
                    movie.getTitle(),
                    movie.getLanguage(),
                    movie.getGenre(),
                    movie.getDuration(),
                    movie.getReleaseDate()
            ));
        }
    }
    private static void getAllMovies(
            MovieService movieService) {

        var movies = movieService.getAllMovies();

        if (movies.isEmpty()) {
            logger.info("No movies found.");
            return;
        }

        logger.info("-------- ALL MOVIES --------");

        for (Movie movie : movies) {

            logger.info("""
                
                Movie ID: %d
                Title: %s
                Language: %s
                Genre: %s
                Duration: %d minutes
                Release Date: %s
                ------------------------
                """.formatted(
                    movie.getMovieId(),
                    movie.getTitle(),
                    movie.getLanguage(),
                    movie.getGenre(),
                    movie.getDuration(),
                    movie.getReleaseDate()
            ));
        }
    }
    private static void updateMovie(
            Scanner scanner,
            MovieService movieService) {

        logger.info("Enter movie ID to update:");

        int movieId = scanner.nextInt();
        scanner.nextLine();

        Movie movie = movieService.getMovieById(movieId);

        if (movie == null) {
            logger.warning("Movie not found.");
            return;
        }

        logger.info("Enter new movie title:");
        movie.setTitle(scanner.nextLine());

        logger.info("Enter new language:");
        movie.setLanguage(scanner.nextLine());

        logger.info("Enter new genre:");
        movie.setGenre(scanner.nextLine());

        logger.info("Enter new duration in minutes:");
        movie.setDuration(scanner.nextInt());
        scanner.nextLine();

        logger.info("Enter new release date (YYYY-MM-DD):");
        String releaseDateInput = scanner.nextLine();

        movie.setReleaseDate(
                java.time.LocalDate.parse(releaseDateInput)
        );

        movieService.updateMovie(movie);

        logger.info("Movie updated successfully through console.");
    }
    private static void deleteMovie(
            Scanner scanner,
            MovieService movieService) {

        logger.info("Enter movie ID to delete:");

        int movieId = scanner.nextInt();
        scanner.nextLine();

        movieService.deleteMovie(movieId);

        logger.info("Movie deleted successfully through console.");
    }
    private static void theatreManagement(
            Scanner scanner,
            TheatreService theatreService) {

        boolean running = true;

        while (running) {

            logger.info("""
                
                ========================================
                     THEATRE MANAGEMENT
                ========================================
                
                1. Add Theatre
                2. Get Theatre By ID
                3. Get All Theatres
                4. Update Theatre
                5. Delete Theatre
                0. Back
                
                ========================================
                """);

            logger.info("Enter your choice:");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {

                case 1 -> addTheatre(scanner, theatreService);

                case 2 -> getTheatreById(scanner, theatreService);

                case 3 -> getAllTheatres(theatreService);

                case 4 -> updateTheatre(scanner, theatreService);

                case 5 -> deleteTheatre(scanner, theatreService);

                case 0 -> {
                    running = false;
                    logger.info("Returning to main menu.");
                }

                default -> logger.warning(
                        "Invalid choice. Please try again."
                );
            }
        }
    }
    private static void addTheatre(
            Scanner scanner,
            TheatreService theatreService) {

        logger.info("Enter theatre name:");
        String name = scanner.nextLine();

        logger.info("Enter city:");
        String city = scanner.nextLine();

        logger.info("Enter address:");
        String address = scanner.nextLine();

        logger.info("Enter total seats:");
        int totalSeats = scanner.nextInt();
        scanner.nextLine();

        Theatre theatre = new Theatre(
                name,
                city,
                address,
                totalSeats
        );

        theatreService.addTheatre(theatre);

        logger.info("Theatre added successfully through console.");
    }
    private static void getTheatreById(
            Scanner scanner,
            TheatreService theatreService) {

        logger.info("Enter theatre ID:");

        int theatreId = scanner.nextInt();
        scanner.nextLine();

        Theatre theatre =
                theatreService.getTheatreById(theatreId);

        if (theatre != null) {

            logger.info("""
                
                -------- THEATRE DETAILS --------
                Theatre ID: %d
                Name: %s
                City: %s
                Address: %s
                Total Seats: %d
                ---------------------------------
                """.formatted(
                    theatre.getTheatreId(),
                    theatre.getName(),
                    theatre.getCity(),
                    theatre.getAddress(),
                    theatre.getTotalSeats()
            ));
        }
    }
    private static void getAllTheatres(
            TheatreService theatreService) {

        var theatres = theatreService.getAllTheatres();

        if (theatres.isEmpty()) {
            logger.info("No theatres found.");
            return;
        }

        logger.info("-------- ALL THEATRES --------");

        for (Theatre theatre : theatres) {

            logger.info("""
                
                Theatre ID: %d
                Name: %s
                City: %s
                Address: %s
                Total Seats: %d
                ---------------------------
                """.formatted(
                    theatre.getTheatreId(),
                    theatre.getName(),
                    theatre.getCity(),
                    theatre.getAddress(),
                    theatre.getTotalSeats()
            ));
        }
    }
    private static void updateTheatre(
            Scanner scanner,
            TheatreService theatreService) {

        logger.info("Enter theatre ID to update:");

        int theatreId = scanner.nextInt();
        scanner.nextLine();

        Theatre theatre =
                theatreService.getTheatreById(theatreId);

        if (theatre == null) {
            logger.warning("Theatre not found.");
            return;
        }

        logger.info("Enter new theatre name:");
        theatre.setName(scanner.nextLine());

        logger.info("Enter new city:");
        theatre.setCity(scanner.nextLine());

        logger.info("Enter new address:");
        theatre.setAddress(scanner.nextLine());

        logger.info("Enter new total seats:");
        theatre.setTotalSeats(scanner.nextInt());
        scanner.nextLine();

        theatreService.updateTheatre(theatre);

        logger.info("Theatre updated successfully through console.");
    }
    private static void deleteTheatre(
            Scanner scanner,
            TheatreService theatreService) {

        logger.info("Enter theatre ID to delete:");

        int theatreId = scanner.nextInt();
        scanner.nextLine();

        theatreService.deleteTheatre(theatreId);

        logger.info("Theatre deleted successfully through console.");
    }
    private static void seatManagement(
            Scanner scanner,
            SeatService seatService) {

        boolean running = true;

        while (running) {

            logger.info("""
                
                ========================================
                     SEAT MANAGEMENT
                ========================================
                
                1. Add Seat
                2. Get Seat By ID
                3. Get All Seats
                4. Update Seat
                5. Delete Seat
                0. Back
                
                ========================================
                """);

            logger.info("Enter your choice:");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {

                case 1 -> addSeat(scanner, seatService);

                case 2 -> getSeatById(scanner, seatService);

                case 3 -> getAllSeats(seatService);

                case 4 -> updateSeat(scanner, seatService);

                case 5 -> deleteSeat(scanner, seatService);

                case 0 -> {
                    running = false;
                    logger.info("Returning to main menu.");
                }

                default -> logger.warning(
                        "Invalid choice. Please try again."
                );
            }
        }
    }
    private static void addSeat(
            Scanner scanner,
            SeatService seatService) {

        logger.info("Enter theatre ID:");
        int theatreId = scanner.nextInt();
        scanner.nextLine();

        logger.info("Enter seat number:");
        String seatNumber = scanner.nextLine();

        logger.info("Enter seat type:");
        String seatType = scanner.nextLine();

        logger.info("Enter seat price:");
        BigDecimal price = scanner.nextBigDecimal();
        scanner.nextLine();

        Seat seat = new Seat(
                theatreId,
                seatNumber,
                seatType,
                price
        );

        seatService.addSeat(seat);

        logger.info("Seat added successfully through console.");
    }
    private static void getSeatById(
            Scanner scanner,
            SeatService seatService) {

        logger.info("Enter seat ID:");

        int seatId = scanner.nextInt();
        scanner.nextLine();

        Seat seat = seatService.getSeatById(seatId);

        if (seat != null) {

            logger.info("""
                
                -------- SEAT DETAILS --------
                Seat ID: %d
                Theatre ID: %d
                Seat Number: %s
                Seat Type: %s
                Price: %s
                ------------------------------
                """.formatted(
                    seat.getSeatId(),
                    seat.getTheatreId(),
                    seat.getSeatNumber(),
                    seat.getSeatType(),
                    seat.getPrice()
            ));
        }
    }
    private static void getAllSeats(
            SeatService seatService) {

        var seats = seatService.getAllSeats();

        if (seats.isEmpty()) {
            logger.info("No seats found.");
            return;
        }

        logger.info("-------- ALL SEATS --------");

        for (Seat seat : seats) {

            logger.info("""
                
                Seat ID: %d
                Theatre ID: %d
                Seat Number: %s
                Seat Type: %s
                Price: %s
                ------------------------
                """.formatted(
                    seat.getSeatId(),
                    seat.getTheatreId(),
                    seat.getSeatNumber(),
                    seat.getSeatType(),
                    seat.getPrice()
            ));
        }
    }
    private static void updateSeat(
            Scanner scanner,
            SeatService seatService) {

        logger.info("Enter seat ID to update:");

        int seatId = scanner.nextInt();
        scanner.nextLine();

        Seat seat = seatService.getSeatById(seatId);

        if (seat == null) {
            logger.warning("Seat not found.");
            return;
        }

        logger.info("Enter new theatre ID:");
        seat.setTheatreId(scanner.nextInt());
        scanner.nextLine();

        logger.info("Enter new seat number:");
        seat.setSeatNumber(scanner.nextLine());

        logger.info("Enter new seat type:");
        seat.setSeatType(scanner.nextLine());

        logger.info("Enter new seat price:");
        seat.setPrice(scanner.nextBigDecimal());
        scanner.nextLine();

        seatService.updateSeat(seat);

        logger.info("Seat updated successfully through console.");
    }
    private static void deleteSeat(
            Scanner scanner,
            SeatService seatService) {

        logger.info("Enter seat ID to delete:");

        int seatId = scanner.nextInt();
        scanner.nextLine();

        seatService.deleteSeat(seatId);

        logger.info("Seat deleted successfully through console.");
    }
    private static void showManagement(
            Scanner scanner,
            ShowService showService) {

        boolean running = true;

        while (running) {

            logger.info("""
                
                ========================================
                     SHOW MANAGEMENT
                ========================================
                
                1. Add Show
                2. Get Show By ID
                3. Get All Shows
                4. Update Show
                5. Delete Show
                0. Back
                
                ========================================
                """);

            logger.info("Enter your choice:");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {

                case 1 -> addShow(scanner, showService);

                case 2 -> getShowById(scanner, showService);

                case 3 -> getAllShows(showService);

                case 4 -> updateShow(scanner, showService);

                case 5 -> deleteShow(scanner, showService);

                case 0 -> {
                    running = false;
                    logger.info("Returning to main menu.");
                }

                default -> logger.warning(
                        "Invalid choice. Please try again."
                );
            }
        }
    }
    private static void addShow(
            Scanner scanner,
            ShowService showService) {

        logger.info("Enter theatre ID:");
        int theatreId = scanner.nextInt();
        scanner.nextLine();

        logger.info("Enter movie ID:");
        int movieId = scanner.nextInt();
        scanner.nextLine();

        logger.info("Enter show date (YYYY-MM-DD):");
        LocalDate showDate = LocalDate.parse(scanner.nextLine());

        logger.info("Enter start time (HH:MM):");
        LocalTime startTime = LocalTime.parse(scanner.nextLine());

        logger.info("Enter end time (HH:MM):");
        LocalTime endTime = LocalTime.parse(scanner.nextLine());

        Show show = new Show(
                theatreId,
                movieId,
                showDate,
                startTime,
                endTime
        );

        showService.addShow(show);

        logger.info("Show added successfully through console.");
    }
    private static void getShowById(
            Scanner scanner,
            ShowService showService) {

        logger.info("Enter show ID:");

        int showId = scanner.nextInt();
        scanner.nextLine();

        Show show = showService.getShowById(showId);

        if (show != null) {

            logger.info("""
                
                -------- SHOW DETAILS --------
                Show ID: %d
                Theatre ID: %d
                Movie ID: %d
                Show Date: %s
                Start Time: %s
                End Time: %s
                ------------------------------
                """.formatted(
                    show.getShowId(),
                    show.getTheatreId(),
                    show.getMovieId(),
                    show.getShowDate(),
                    show.getStartTime(),
                    show.getEndTime()
            ));
        }
    }
    private static void getAllShows(
            ShowService showService) {

        var shows = showService.getAllShows();

        if (shows.isEmpty()) {
            logger.info("No shows found.");
            return;
        }

        logger.info("-------- ALL SHOWS --------");

        for (Show show : shows) {

            logger.info("""
                
                Show ID: %d
                Theatre ID: %d
                Movie ID: %d
                Show Date: %s
                Start Time: %s
                End Time: %s
                ------------------------
                """.formatted(
                    show.getShowId(),
                    show.getTheatreId(),
                    show.getMovieId(),
                    show.getShowDate(),
                    show.getStartTime(),
                    show.getEndTime()
            ));
        }
    }
    private static void updateShow(
            Scanner scanner,
            ShowService showService) {

        logger.info("Enter show ID to update:");

        int showId = scanner.nextInt();
        scanner.nextLine();

        Show show = showService.getShowById(showId);

        if (show == null) {
            logger.warning("Show not found.");
            return;
        }

        logger.info("Enter new theatre ID:");
        show.setTheatreId(scanner.nextInt());
        scanner.nextLine();

        logger.info("Enter new movie ID:");
        show.setMovieId(scanner.nextInt());
        scanner.nextLine();

        logger.info("Enter new show date (YYYY-MM-DD):");
        show.setShowDate(
                LocalDate.parse(scanner.nextLine())
        );

        logger.info("Enter new start time (HH:MM):");
        show.setStartTime(
                LocalTime.parse(scanner.nextLine())
        );

        logger.info("Enter new end time (HH:MM):");
        show.setEndTime(
                LocalTime.parse(scanner.nextLine())
        );

        showService.updateShow(show);

        logger.info("Show updated successfully through console.");
    }
    private static void deleteShow(
            Scanner scanner,
            ShowService showService) {

        logger.info("Enter show ID to delete:");

        int showId = scanner.nextInt();
        scanner.nextLine();

        showService.deleteShow(showId);

        logger.info("Show deleted successfully through console.");
    }
    private static void bookingManagement(
            Scanner scanner,
            BookingService bookingService,
            BookedSeatService bookedSeatService,
            MovieService movieService,
            TheatreService theatreService,
            ShowService showService,
            SeatService seatService,
            PaymentService paymentService) {

        boolean running = true;

        while (running) {

            logger.info("""
                
                ========================================
                     BOOKING MANAGEMENT
                ========================================
                
                1. Add Booking
                2. Get Booking By ID
                3. Get All Bookings
                4. Update Booking
                5. Delete Booking
                0. Back
                
                ========================================
                """);

            logger.info("Enter your choice:");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {

                case 1 -> addBooking(
                        scanner,
                        bookingService,
                        bookedSeatService,
                        movieService,
                        theatreService,
                        showService,
                        seatService,
                        paymentService
                );

                case 2 -> getBookingById(scanner, bookingService);

                case 3 -> getAllBookings(bookingService);

                case 4 -> updateBooking(scanner, bookingService);

                case 5 -> deleteBooking(scanner, bookingService);

                case 0 -> {
                    running = false;
                    logger.info("Returning to main menu.");
                }

                default -> logger.warning(
                        "Invalid choice. Please try again."
                );
            }
        }
    }
    private static void addBooking(
            Scanner scanner,
            BookingService bookingService,
            BookedSeatService bookedSeatService,
            MovieService movieService,
            TheatreService theatreService,
            ShowService showService,
            SeatService seatService,
            PaymentService paymentService) {

        // ==============================
        // STEP 1: SEARCH MOVIE BY NAME
        // ==============================

        logger.info("Enter movie name:");

        String movieName = scanner.nextLine();

        List<Movie> movies =
                movieService.findMoviesByTitle(movieName);

        if (movies.isEmpty()) {

            logger.warning(
                    "No movies found with that name."
            );

            return;
        }

        logger.info("Movies found:");

        for (int i = 0; i < movies.size(); i++) {

            Movie movie = movies.get(i);

            logger.info(
                    (i + 1) + ". "
                            + movie.getTitle()
                            + " | "
                            + movie.getLanguage()
                            + " | "
                            + movie.getGenre()
            );
        }

        logger.info("Select movie number:");

        int movieChoice =
                Integer.parseInt(scanner.nextLine());

        if (movieChoice < 1 ||
                movieChoice > movies.size()) {

            logger.warning("Invalid movie selection.");

            return;
        }

        Movie selectedMovie =
                movies.get(movieChoice - 1);

        int movieId =
                selectedMovie.getMovieId();

        logger.info(
                "Selected movie: "
                        + selectedMovie.getTitle()
        );


        // ==============================
        // STEP 2: FIND THEATRES
        // ==============================

        List<Theatre> theatres =
                theatreService.findTheatresByMovieId(movieId);

        if (theatres.isEmpty()) {

            logger.warning(
                    "No theatres are showing this movie."
            );

            return;
        }

        logger.info("Theatres showing this movie:");

        for (int i = 0; i < theatres.size(); i++) {

            Theatre theatre = theatres.get(i);

            logger.info(
                    (i + 1) + ". "
                            + theatre.getName()
                            + " | "
                            + theatre.getCity()
                            + " | "
                            + theatre.getAddress()
            );
        }

        logger.info("Select theatre number:");

        int theatreChoice =
                Integer.parseInt(scanner.nextLine());

        if (theatreChoice < 1 ||
                theatreChoice > theatres.size()) {

            logger.warning("Invalid theatre selection.");

            return;
        }

        Theatre selectedTheatre =
                theatres.get(theatreChoice - 1);

        int theatreId =
                selectedTheatre.getTheatreId();

        logger.info(
                "Selected theatre: "
                        + selectedTheatre.getName()
        );


        // ==============================
        // STEP 3: FIND SHOWS
        // ==============================

        List<Show> shows =
                showService.findShowsByMovieId(movieId);

        List<Show> theatreShows = new java.util.ArrayList<>();

        for (Show show : shows) {

            if (show.getTheatreId() == theatreId) {
                theatreShows.add(show);
            }
        }

        if (theatreShows.isEmpty()) {

            logger.warning(
                    "No shows available at the selected theatre."
            );

            return;
        }

        logger.info("Available shows:");

        for (int i = 0; i < theatreShows.size(); i++) {

            Show show = theatreShows.get(i);

            logger.info(
                    (i + 1) + ". "
                            + "Date: "
                            + show.getShowDate()
                            + " | Time: "
                            + show.getStartTime()
                            + " - "
                            + show.getEndTime()
            );
        }

        logger.info("Select show number:");

        int showChoice =
                Integer.parseInt(scanner.nextLine());

        if (showChoice < 1 ||
                showChoice > theatreShows.size()) {

            logger.warning("Invalid show selection.");

            return;
        }

        Show selectedShow =
                theatreShows.get(showChoice - 1);

        int showId =
                selectedShow.getShowId();

        logger.info(
                "Selected show ID: "
                        + showId
        );

        // ==============================
        //  DISPLAY SEATS
        // ==============================

        List<Seat> seats =
                seatService.findAvailableSeatsByShowId(
                        theatreId,
                        showId
                );

        if (seats.isEmpty()) {
            logger.warning("No seats available for the selected show.");
            return;
        }

        logger.info("Available seats:");

        for (int i = 0; i < seats.size(); i++) {

            Seat seat = seats.get(i);

            logger.info(
                    (i + 1)
                            + ". "
                            + seat.getSeatNumber()
                            + " | Type: "
                            + seat.getSeatType()
                            + " | Price: "
                            + seat.getPrice()
            );
        }

        logger.info(
                "Select seat numbers separated by commas (example: 1,2,4):"
        );

        String seatInput = scanner.nextLine();

        String[] seatChoices = seatInput.split(",");

        List<Seat> selectedSeats = new java.util.ArrayList<>();

        for (String choice : seatChoices) {

            int seatChoice;

            try {
                seatChoice = Integer.parseInt(choice.trim());
            } catch (NumberFormatException e) {
                logger.warning("Invalid seat selection: " + choice);
                return;
            }

            if (seatChoice < 1 || seatChoice > seats.size()) {
                logger.warning(
                        "Invalid seat number: " + seatChoice
                );
                return;
            }

            Seat selectedSeat = seats.get(seatChoice - 1);

            if (selectedSeats.contains(selectedSeat)) {
                logger.warning(
                        "Seat selected more than once: "
                                + selectedSeat.getSeatNumber()
                );
                return;
            }

            selectedSeats.add(selectedSeat);
        }

        if (selectedSeats.isEmpty()) {
            logger.warning("No seats selected.");
            return;
        }

        logger.info("Selected seats:");

        for (Seat seat : selectedSeats) {

            logger.info(
                    seat.getSeatNumber()
                            + " | Type: "
                            + seat.getSeatType()
                            + " | Price: "
                            + seat.getPrice()
            );
        }

        // ==============================
        // STEP 4: USER ID
        // ==============================

        logger.info("Enter user ID:");

        int userId =
                Integer.parseInt(scanner.nextLine());


        // ==============================
        // STEP 5: BOOKING DATE & TIME
        // ==============================

        logger.info(
                "Enter booking date and time " +
                        "(YYYY-MM-DD HH:MM:SS):"
        );

        LocalDateTime bookingDate =
                LocalDateTime.parse(
                        scanner.nextLine(),
                        java.time.format.DateTimeFormatter.ofPattern(
                                "yyyy-MM-dd HH:mm:ss"
                        )
                );


        // ==============================
        // STEP 6: TOTAL AMOUNT
        // ==============================

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (Seat seat : selectedSeats) {

            totalAmount = totalAmount.add(
                    seat.getPrice()
            );
        }

        logger.info(
                "Total amount: ₹" + totalAmount
        );


        // ==============================
        // STEP 7: BOOKING STATUS
        // ==============================

        logger.info("Enter booking status:");

        String bookingStatus =
                scanner.nextLine();

        logger.info("Enter payment method:");
        String paymentMethod = scanner.nextLine();


        // ==============================
        // STEP 8: CREATE BOOKING
        // ==============================

        Booking booking = new Booking(
                showId,
                userId,
                bookingDate,
                totalAmount,
                bookingStatus
        );

        int bookingId = bookingService.addBooking(booking);

        for (Seat seat : selectedSeats) {

            BookedSeat bookedSeat =
                    new BookedSeat(
                            seat.getSeatId(),
                            bookingId
                    );

            bookedSeatService.addBookedSeat(bookedSeat);
        }
        Payment payment =
                new Payment(
                        bookingId,
                        totalAmount,
                        paymentMethod,
                        "SUCCESS",
                        LocalDateTime.now()
                );

        paymentService.addPayment(payment);

        logger.info("==============================");
        logger.info("       BOOKING CONFIRMED");
        logger.info("==============================");

        logger.info(
                "Movie: " + selectedMovie.getTitle()
        );

        logger.info(
                "Theatre: " + selectedTheatre.getName()
        );

        logger.info(
                "Show Date: " + selectedShow.getShowDate()
        );

        logger.info(
                "Show Time: " + selectedShow.getStartTime()
        );

        logger.info("Selected Seats:");

        for (Seat seat : selectedSeats) {
            logger.info(
                    seat.getSeatNumber()
                            + " | ₹"
                            + seat.getPrice()
            );
        }

        logger.info(
                "Total Amount: ₹" + totalAmount
        );

        logger.info(
                "Payment Method: " + paymentMethod
        );

        logger.info("Payment Status: SUCCESS");

        logger.info(
                "Booking ID: " + bookingId
        );

        logger.info("==============================");


    }
    private static void getBookingById(
            Scanner scanner,
            BookingService bookingService) {

        logger.info("Enter booking ID:");

        int bookingId =
                Integer.parseInt(scanner.nextLine());
        Booking booking =
                bookingService.getBookingById(bookingId);

        if (booking != null) {

            logger.info("""
                
                -------- BOOKING DETAILS --------
                Booking ID: %d
                Show ID: %d
                User ID: %d
                Booking Date: %s
                Total Amount: %s
                Booking Status: %s
                ---------------------------------
                """.formatted(
                    booking.getBookingId(),
                    booking.getShowId(),
                    booking.getUserId(),
                    booking.getBookingDate(),
                    booking.getTotalAmount(),
                    booking.getBookingStatus()
            ));

        } else {

            logger.warning(
                    "Booking not found."
            );
        }
    }
    private static void getAllBookings(
            BookingService bookingService) {

        var bookings = bookingService.getAllBookings();

        if (bookings.isEmpty()) {
            logger.info("No bookings found.");
            return;
        }

        logger.info("-------- ALL BOOKINGS --------");

        for (Booking booking : bookings) {

            logger.info("""
                
                Booking ID: %d
                Show ID: %d
                User ID: %d
                Booking Date: %s
                Total Amount: %s
                Booking Status: %s
                ----------------------------
                """.formatted(
                    booking.getBookingId(),
                    booking.getShowId(),
                    booking.getUserId(),
                    booking.getBookingDate(),
                    booking.getTotalAmount(),
                    booking.getBookingStatus()
            ));
        }
    }
    private static void updateBooking(
            Scanner scanner,
            BookingService bookingService) {

        logger.info("Enter booking ID to update:");

        int bookingId = scanner.nextInt();
        scanner.nextLine();

        Booking booking =
                bookingService.getBookingById(bookingId);

        if (booking == null) {
            logger.warning("Booking not found.");
            return;
        }

        logger.info("Enter new show ID:");
        booking.setShowId(scanner.nextInt());
        scanner.nextLine();

        logger.info("Enter new user ID:");
        booking.setUserId(scanner.nextInt());
        scanner.nextLine();

        logger.info("Enter new booking date and time (YYYY-MM-DD HH:MM:SS):");

        booking.setBookingDate(
                LocalDateTime.parse(
                        scanner.nextLine(),
                        java.time.format.DateTimeFormatter.ofPattern(
                                "yyyy-MM-dd HH:mm:ss"
                        )
                )
        );

        logger.info("Enter new total amount:");
        booking.setTotalAmount(scanner.nextBigDecimal());
        scanner.nextLine();

        logger.info("Enter new booking status:");
        booking.setBookingStatus(scanner.nextLine());

        bookingService.updateBooking(booking);

        logger.info("Booking updated successfully through console.");
    }
    private static void deleteBooking(
            Scanner scanner,
            BookingService bookingService) {

        logger.info("Enter booking ID to delete:");

        int bookingId = scanner.nextInt();
        scanner.nextLine();

        bookingService.deleteBooking(bookingId);

        logger.info("Booking deleted successfully through console.");
    }
    private static void bookedSeatManagement(
            Scanner scanner,
            BookedSeatService bookedSeatService) {

        boolean running = true;

        while (running) {

            logger.info("""
                
                ========================================
                     BOOKED SEAT MANAGEMENT
                ========================================
                
                1. Add Booked Seat
                2. Get Booked Seat By ID
                3. Get All Booked Seats
                4. Update Booked Seat
                5. Delete Booked Seat
                0. Back
                
                ========================================
                """);

            logger.info("Enter your choice:");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {

                case 1 -> addBookedSeat(scanner, bookedSeatService);

                case 2 -> getBookedSeatById(
                        scanner,
                        bookedSeatService
                );

                case 3 -> getAllBookedSeats(bookedSeatService);

                case 4 -> updateBookedSeat(
                        scanner,
                        bookedSeatService
                );

                case 5 -> deleteBookedSeat(
                        scanner,
                        bookedSeatService
                );

                case 0 -> {
                    running = false;
                    logger.info("Returning to main menu.");
                }

                default -> logger.warning(
                        "Invalid choice. Please try again."
                );
            }
        }
    }
    private static void addBookedSeat(
            Scanner scanner,
            BookedSeatService bookedSeatService) {

        logger.info("Enter seat ID:");
        int seatId = scanner.nextInt();
        scanner.nextLine();

        logger.info("Enter booking ID:");
        int bookingId = scanner.nextInt();
        scanner.nextLine();

        BookedSeat bookedSeat =
                new BookedSeat(seatId, bookingId);

        bookedSeatService.addBookedSeat(bookedSeat);

        logger.info(
                "Booked seat added successfully through console."
        );
    }
    private static void getBookedSeatById(
            Scanner scanner,
            BookedSeatService bookedSeatService) {

        logger.info("Enter booked seat ID:");

        int bookedSeatId = scanner.nextInt();
        scanner.nextLine();

        BookedSeat bookedSeat =
                bookedSeatService.getBookedSeatById(bookedSeatId);

        if (bookedSeat != null) {

            logger.info("""
                
                -------- BOOKED SEAT DETAILS --------
                Booked Seat ID: %d
                Seat ID: %d
                Booking ID: %d
                -------------------------------------
                """.formatted(
                    bookedSeat.getBookedSeatId(),
                    bookedSeat.getSeatId(),
                    bookedSeat.getBookingId()
            ));
        }
    }
    private static void getAllBookedSeats(
            BookedSeatService bookedSeatService) {

        var bookedSeats =
                bookedSeatService.getAllBookedSeats();

        if (bookedSeats.isEmpty()) {
            logger.info("No booked seats found.");
            return;
        }

        logger.info("-------- ALL BOOKED SEATS --------");

        for (BookedSeat bookedSeat : bookedSeats) {

            logger.info("""
                
                Booked Seat ID: %d
                Seat ID: %d
                Booking ID: %d
                ------------------------------
                """.formatted(
                    bookedSeat.getBookedSeatId(),
                    bookedSeat.getSeatId(),
                    bookedSeat.getBookingId()
            ));
        }
    }
    private static void updateBookedSeat(
            Scanner scanner,
            BookedSeatService bookedSeatService) {

        logger.info("Enter booked seat ID to update:");

        int bookedSeatId = scanner.nextInt();
        scanner.nextLine();

        BookedSeat bookedSeat =
                bookedSeatService.getBookedSeatById(bookedSeatId);

        if (bookedSeat == null) {
            logger.warning("Booked seat not found.");
            return;
        }

        logger.info("Enter new seat ID:");
        bookedSeat.setSeatId(scanner.nextInt());
        scanner.nextLine();

        logger.info("Enter new booking ID:");
        bookedSeat.setBookingId(scanner.nextInt());
        scanner.nextLine();

        bookedSeatService.updateBookedSeat(bookedSeat);

        logger.info(
                "Booked seat updated successfully through console."
        );
    }
    private static void deleteBookedSeat(
            Scanner scanner,
            BookedSeatService bookedSeatService) {

        logger.info("Enter booked seat ID to delete:");

        int bookedSeatId = scanner.nextInt();
        scanner.nextLine();

        bookedSeatService.deleteBookedSeat(bookedSeatId);

        logger.info(
                "Booked seat deleted successfully through console."
        );
    }
    private static void paymentManagement(
            Scanner scanner,
            PaymentService paymentService) {

        boolean running = true;

        while (running) {

            logger.info("""
                
                ========================================
                     PAYMENT MANAGEMENT
                ========================================
                
                1. Add Payment
                2. Get Payment By ID
                3. Get All Payments
                4. Update Payment
                5. Delete Payment
                0. Back
                
                ========================================
                """);

            logger.info("Enter your choice:");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {

                case 1 -> addPayment(scanner, paymentService);

                case 2 -> getPaymentById(scanner, paymentService);

                case 3 -> getAllPayments(paymentService);

                case 4 -> updatePayment(scanner, paymentService);

                case 5 -> deletePayment(scanner, paymentService);

                case 0 -> {
                    running = false;
                    logger.info("Returning to main menu.");
                }

                default -> logger.warning(
                        "Invalid choice. Please try again."
                );
            }
        }
    }
    private static void addPayment(
            Scanner scanner,
            PaymentService paymentService) {

        logger.info("Enter booking ID:");
        int bookingId = scanner.nextInt();
        scanner.nextLine();

        logger.info("Enter payment amount:");
        BigDecimal amount = scanner.nextBigDecimal();
        scanner.nextLine();

        logger.info("Enter payment method:");
        String paymentMethod = scanner.nextLine();

        logger.info("Enter payment status:");
        String paymentStatus = scanner.nextLine();

        logger.info("Enter payment date and time (YYYY-MM-DD HH:MM:SS):");

        LocalDateTime paymentDate =
                LocalDateTime.parse(
                        scanner.nextLine(),
                        java.time.format.DateTimeFormatter.ofPattern(
                                "yyyy-MM-dd HH:mm:ss"
                        )
                );

        Payment payment = new Payment(
                bookingId,
                amount,
                paymentMethod,
                paymentStatus,
                paymentDate
        );

        paymentService.addPayment(payment);

        logger.info(
                "Payment added successfully through console."
        );
    }
    private static void getPaymentById(
            Scanner scanner,
            PaymentService paymentService) {

        logger.info("Enter payment ID:");

        int paymentId = scanner.nextInt();
        scanner.nextLine();

        Payment payment =
                paymentService.getPaymentById(paymentId);

        if (payment != null) {

            logger.info("""
                
                -------- PAYMENT DETAILS --------
                Payment ID: %d
                Booking ID: %d
                Amount: %s
                Payment Method: %s
                Payment Status: %s
                Payment Date: %s
                ---------------------------------
                """.formatted(
                    payment.getPaymentId(),
                    payment.getBookingId(),
                    payment.getAmount(),
                    payment.getPaymentMethod(),
                    payment.getPaymentStatus(),
                    payment.getPaymentDate()
            ));
        }
    }
    private static void getAllPayments(
            PaymentService paymentService) {

        var payments = paymentService.getAllPayments();

        if (payments.isEmpty()) {
            logger.info("No payments found.");
            return;
        }

        logger.info("-------- ALL PAYMENTS --------");

        for (Payment payment : payments) {

            logger.info("""
                
                Payment ID: %d
                Booking ID: %d
                Amount: %s
                Payment Method: %s
                Payment Status: %s
                Payment Date: %s
                ----------------------------
                """.formatted(
                    payment.getPaymentId(),
                    payment.getBookingId(),
                    payment.getAmount(),
                    payment.getPaymentMethod(),
                    payment.getPaymentStatus(),
                    payment.getPaymentDate()
            ));
        }
    }
    private static void updatePayment(
            Scanner scanner,
            PaymentService paymentService) {

        logger.info("Enter payment ID to update:");

        int paymentId = scanner.nextInt();
        scanner.nextLine();

        Payment payment =
                paymentService.getPaymentById(paymentId);

        if (payment == null) {
            logger.warning("Payment not found.");
            return;
        }

        logger.info("Enter new booking ID:");
        payment.setBookingId(scanner.nextInt());
        scanner.nextLine();

        logger.info("Enter new payment amount:");
        payment.setAmount(scanner.nextBigDecimal());
        scanner.nextLine();

        logger.info("Enter new payment method:");
        payment.setPaymentMethod(scanner.nextLine());

        logger.info("Enter new payment status:");
        payment.setPaymentStatus(scanner.nextLine());

        logger.info(
                "Enter new payment date and time (YYYY-MM-DD HH:MM:SS):"
        );

        payment.setPaymentDate(
                LocalDateTime.parse(
                        scanner.nextLine(),
                        java.time.format.DateTimeFormatter.ofPattern(
                                "yyyy-MM-dd HH:mm:ss"
                        )
                )
        );

        paymentService.updatePayment(payment);

        logger.info(
                "Payment updated successfully through console."
        );
    }
    private static void deletePayment(
            Scanner scanner,
            PaymentService paymentService) {

        logger.info("Enter payment ID to delete:");

        int paymentId = scanner.nextInt();
        scanner.nextLine();

        paymentService.deletePayment(paymentId);

        logger.info(
                "Payment deleted successfully through console."
        );
    }
}
