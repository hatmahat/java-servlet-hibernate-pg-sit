package com.example.controller;

import com.example.dao.UserDao;
import com.example.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class UserControllerTest {

    @InjectMocks // Inject mocked dependencies (like userDao) into userController
    private UserController userController;

    @Mock // Creates a mock version of the UserDao interface
    private UserDao userDao; 

    @BeforeEach
    public void setup() {
         MockitoAnnotations.openMocks(this);
    }
    
    @Test 
    public void testListUsers_withNonEmptyList() {
        // Mocking the list of users
        List<User> mockUsers = new ArrayList<>();
        User user1 = new User();
        user1.setName("John Doe");
        user1.setEmail("john.doe@gmail.com");

        User user2 = new User(); 
        user2.setName("Jane Doe");
        user2.setEmail("Jane Doe");

        mockUsers.add(user1);
        mockUsers.add(user2);

        // Define behaviour of mocked userDao
        when(userDao.listUsers()).thenReturn(mockUsers);

        // Call the method in UserController and assert the result
        List<User> result = userController.listUsers();
        assertEquals(2, result.size());
        assertEquals("John Doe", result.get(0).getName());
        assertEquals("Jane Doe", result.get(1).getName());
    }

    @Test 
    public void testListUsers_withEmptyList() {
        // Mocking an empty list of users 
        List<User> mockUsers = new ArrayList<>(); 

        // Define behaviour of mocked userDao
        when(userDao.listUsers()).thenReturn(mockUsers);

        // Call the method and assert the result
        List<User> result = userController.listUsers();
        assertEquals(0, result.size());
    }

    @Test
    public void testAddUser_withValidInputs() {
        String name = "John Doe";
        String email = "john.doe@gmail.com"; 

        // No need to mock the saveUser method; just verify it was called
        doNothing().when(userDao).saveUser(any(User.class));

        // Call the method
        String response = userController.saveUser(name, email);

        // Assert the result 
        assertEquals("User added successfully!", response);

        // Verify that the saveUser method was called with the correct user data
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userDao).saveUser(userCaptor.capture());
        User captureUser = userCaptor.getValue();
        assertEquals("John Doe", captureUser.getName());
        assertEquals("john.doe@gmail.com", captureUser.getEmail());
    }

    @Test 
    public void testAddUser_withInvalidInputs() {
        String name = "";
        String email = ""; 

        String response = userController.saveUser(name, email);

        // Assert the result 
        assertEquals("User added successfully!", response);

        // Verify that the saveUser method was still called, but with empty data
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userDao).saveUser(userCaptor.capture());
        User capturedUser = userCaptor.getValue();
        assertEquals("", capturedUser.getName());
        assertEquals("", capturedUser.getEmail());
    }
}
