package com.movieticket.dao;

import com.movieticket.model.User;
import com.movieticket.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class UserDAO {

    // SQL Queries
    private static final String INSERT_USER = """
            INSERT INTO users
            (name, email, phone, password, role)
            VALUES (?, ?, ?, ?, ?)
            """;

    private static final String SELECT_USER_BY_ID = """
            SELECT user_id, name, email, phone, password, role
            FROM users
            WHERE user_id = ?
            """;

    private static final String SELECT_ALL_USERS = """
            SELECT user_id, name, email, phone, password, role
            FROM users
            """;

    private static final String UPDATE_USER = """
            UPDATE users
            SET name = ?, email = ?, phone = ?, password = ?, role = ?
            WHERE user_id = ?
            """;

    private static final String DELETE_USER = """
            DELETE FROM users
            WHERE user_id = ?
            """;

    // Logger
    private static final Logger logger =
            Logger.getLogger(UserDAO.class.getName());

    // CREATE
    public void addUser(User user) {

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(INSERT_USER)
        ) {

            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPhone());
            statement.setString(4, user.getPassword());
            statement.setString(5, user.getRole());

            statement.executeUpdate();

            logger.info("User added successfully!");

        } catch (SQLException e) {
            logger.severe("Error while adding user: " + e.getMessage());
        }
    }

    // READ - Get user by ID
    public User getUserById(int userId) {

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(SELECT_USER_BY_ID)
        ) {

            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    User user = new User();

                    user.setUserId(resultSet.getInt("user_id"));
                    user.setName(resultSet.getString("name"));
                    user.setEmail(resultSet.getString("email"));
                    user.setPhone(resultSet.getString("phone"));
                    user.setPassword(resultSet.getString("password"));
                    user.setRole(resultSet.getString("role"));

                    return user;
                }
            }

        } catch (SQLException e) {
            logger.severe("Error while retrieving user: " + e.getMessage());
        }

        return null;
    }

    // READ - Get all users
    public List<User> getAllUsers() {

        List<User> users = new ArrayList<>();

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(SELECT_ALL_USERS);
                ResultSet resultSet = statement.executeQuery()
        ) {

            while (resultSet.next()) {

                User user = new User();

                user.setUserId(resultSet.getInt("user_id"));
                user.setName(resultSet.getString("name"));
                user.setEmail(resultSet.getString("email"));
                user.setPhone(resultSet.getString("phone"));
                user.setPassword(resultSet.getString("password"));
                user.setRole(resultSet.getString("role"));

                users.add(user);
            }

        } catch (SQLException e) {
            logger.severe("Error while retrieving users: " + e.getMessage());
        }

        return users;
    }

    // UPDATE
    public void updateUser(User user) {

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(UPDATE_USER)
        ) {

            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPhone());
            statement.setString(4, user.getPassword());
            statement.setString(5, user.getRole());
            statement.setInt(6, user.getUserId());

            statement.executeUpdate();

            logger.info("User updated successfully!");

        } catch (SQLException e) {
            logger.severe("Error while updating user: " + e.getMessage());
        }
    }

    // DELETE
    public void deleteUser(int userId) {

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(DELETE_USER)
        ) {

            statement.setInt(1, userId);

            statement.executeUpdate();

            logger.info("User deleted successfully!");

        } catch (SQLException e) {
            logger.severe("Error while deleting user: " + e.getMessage());
        }
    }
}