package com.example.controller;

import com.example.model.User;
import com.example.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // Scenario 1: Test GET /users with a non-empty list
    @Test
    public void testListUsers_withNonEmptyList() {
        List<User> mockUsers = new ArrayList<>();
        User user1 = new User();
        user1.setName("John Doe");
        user1.setEmail("john@example.com");

        User user2 = new User();
        user2.setName("Jane Doe");
        user2.setEmail("jane@example.com");

        mockUsers.add(user1);
        mockUsers.add(user2);

        when(userService.getAllUsers()).thenReturn(mockUsers);

        List<User> result = userController.listUsers();
        assertEquals(2, result.size());
        assertEquals("John Doe", result.get(0).getName());
        assertEquals("Jane Doe", result.get(1).getName());
    }

    // Scenario 2: Test GET /users with an empty list
    @Test
    public void testListUsers_withEmptyList() {
        List<User> mockUsers = new ArrayList<>();
        when(userService.getAllUsers()).thenReturn(mockUsers);

        List<User> result = userController.listUsers();
        assertEquals(0, result.size());
    }

    // Scenario 3: Test POST /addUser with valid inputs
    @Test
    public void testAddUser_withValidInputs() {
        String name = "John Doe";
        String email = "john@example.com";

        doNothing().when(userService).addUser(name, email);

        String response = userController.saveUser(name, email);
        assertEquals("User added successfully!", response);

        verify(userService).addUser(name, email);
    }

    // Scenario 4: Test POST /addUser with invalid inputs (e.g., empty name or email)
    @Test
    public void testAddUser_withEmptyNameOrEmail() {
        String name = "";
        String email = "";

        doNothing().when(userService).addUser(name, email);

        String response = userController.saveUser(name, email);
        assertEquals("Invalid name or email", response);
    }
}
