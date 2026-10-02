package com.movieticket.service;

import com.movieticket.dao.UserDAO;
import com.movieticket.exception.ResourceNotFoundException;
import com.movieticket.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserDAO userDAO;

    @InjectMocks
    private UserService userService;

    // CREATE
    @Test
    void shouldAddUserSuccessfully() {

        User user = new User(
                "John",
                "john@gmail.com",
                "9876543210",
                "password123",
                "USER"
        );

        userService.addUser(user);

        verify(userDAO).addUser(user);
    }

    // READ - Get user by ID
    @Test
    void shouldGetUserByIdSuccessfully() {

        User user = new User(
                "John",
                "john@gmail.com",
                "9876543210",
                "password123",
                "USER"
        );

        user.setUserId(1);

        when(userDAO.getUserById(1)).thenReturn(user);

        User result = userService.getUserById(1);

        assertNotNull(result);
        assertEquals(1, result.getUserId());
        assertEquals("John", result.getName());

        verify(userDAO).getUserById(1);
    }

    // READ - User not found
    @Test
    void shouldThrowExceptionWhenUserDoesNotExist() {

        when(userDAO.getUserById(99)).thenReturn(null);

        assertThrows(
                ResourceNotFoundException.class,
                () -> userService.getUserById(99)
        );

        verify(userDAO).getUserById(99);
    }

    // READ - Get all users
    @Test
    void shouldGetAllUsersSuccessfully() {

        User user1 = new User(
                "John",
                "john@gmail.com",
                "9876543210",
                "password123",
                "USER"
        );

        User user2 = new User(
                "Alice",
                "alice@gmail.com",
                "9876543211",
                "password456",
                "USER"
        );

        List<User> users = Arrays.asList(user1, user2);

        when(userDAO.getAllUsers()).thenReturn(users);

        List<User> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(2, result.size());

        verify(userDAO).getAllUsers();
    }

    // UPDATE
    @Test
    void shouldUpdateUserSuccessfully() {

        User user = new User(
                "John Updated",
                "johnupdated@gmail.com",
                "9876543210",
                "newpassword",
                "USER"
        );

        user.setUserId(1);

        userService.updateUser(user);

        verify(userDAO).updateUser(user);
    }

    // DELETE
    @Test
    void shouldDeleteUserSuccessfully() {

        userService.deleteUser(1);

        verify(userDAO).deleteUser(1);
    }
}