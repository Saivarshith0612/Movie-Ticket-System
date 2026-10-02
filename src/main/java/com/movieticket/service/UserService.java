package com.movieticket.service;

import com.movieticket.dao.UserDAO;
import com.movieticket.model.User;
import com.movieticket.exception.ResourceNotFoundException;
import com.movieticket.exception.ValidationException;

import java.util.List;
import java.util.logging.Logger;

public class UserService {

    private static final Logger logger =
            Logger.getLogger(UserService.class.getName());

    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    // CREATE
    public void addUser(User user) {

        if (user == null) {
            throw new ValidationException("User cannot be null.");
        }

        if (user.getName() == null || user.getName().isBlank()) {
            throw new ValidationException("User name cannot be empty.");
        }

        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ValidationException("User email cannot be empty.");
        }

        userDAO.addUser(user);

        logger.info("User registration completed successfully.");
    }

    // READ - Get user by ID
    public User getUserById(int userId) {

        if (userId <= 0) {
            throw new ValidationException(
                    "User ID must be greater than zero."
            );
        }

        User user = userDAO.getUserById(userId);

        if (user == null) {
            throw new ResourceNotFoundException(
                    "User not found with ID: " + userId
            );
        }

        logger.info("User retrieved successfully.");

        return user;
    }

    // READ - Get all users
    public List<User> getAllUsers() {

        List<User> users = userDAO.getAllUsers();

        logger.info("Retrieved " + users.size() + " user(s).");

        return users;
    }

    // UPDATE
    public void updateUser(User user) {

        if (user == null) {
            throw new ValidationException("User cannot be null.");
        }

        if (user.getUserId() <= 0) {
            throw new ValidationException(
                    "User ID must be greater than zero."
            );
        }

        userDAO.updateUser(user);

        logger.info("User update completed successfully.");
    }

    // DELETE
    public void deleteUser(int userId) {

        if (userId <= 0) {
            throw new ValidationException(
                    "User ID must be greater than zero."
            );
        }

        userDAO.deleteUser(userId);

        logger.info("User deletion completed successfully.");
    }
}