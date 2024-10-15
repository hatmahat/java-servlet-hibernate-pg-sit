package com.example.service;

import com.example.dao.UserDao;
import com.example.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserDao userDao;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // Scenario 1: Test getAllUsers() when the user list is non-empty
    @Test
    public void testGetAllUsers_withNonEmptyList() {
        List<User> mockUsers = new ArrayList<>();
        User user1 = new User();
        user1.setName("John Doe");
        user1.setEmail("john@example.com");

        User user2 = new User();
        user2.setName("Jane Doe");
        user2.setEmail("jane@example.com");

        mockUsers.add(user1);
        mockUsers.add(user2);

        // Mocking the behavior of userDao
        when(userDao.listUsers()).thenReturn(mockUsers);

        // Test the method
        List<User> result = userService.getAllUsers();
        assertEquals(2, result.size());
        assertEquals("John Doe", result.get(0).getName());
        assertEquals("Jane Doe", result.get(1).getName());
    }

    // Scenario 2: Test getAllUsers() when the user list is empty
    @Test
    public void testGetAllUsers_withEmptyList() {
        List<User> mockUsers = new ArrayList<>();

        // Mocking the behavior of userDao
        when(userDao.listUsers()).thenReturn(mockUsers);

        // Test the method
        List<User> result = userService.getAllUsers();
        assertEquals(0, result.size());
    }

    // Scenario 3: Test addUser() with valid inputs
    @Test
    public void testAddUser_withValidInputs() {
        String name = "John Doe";
        String email = "john@example.com";

        // Simulate successful saving of a user
        doNothing().when(userDao).saveUser(any(User.class));

        // Test the method
        userService.addUser(name, email);

        // Verify that userDao.saveUser() was called
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userDao).saveUser(userCaptor.capture());
        User capturedUser = userCaptor.getValue();

        assertEquals("John Doe", capturedUser.getName());
        assertEquals("john@example.com", capturedUser.getEmail());
    }

    // Scenario 4: Test addUser() when saveUser() throws an exception
    @Test
    public void testAddUser_whenSaveUserThrowsException() {
        String name = "John Doe";
        String email = "john@example.com";

        // Simulate exception during saveUser
        doThrow(new RuntimeException("Database error")).when(userDao).saveUser(any(User.class));

        try {
            // Test the method
            userService.addUser(name, email);
        } catch (RuntimeException e) {
            assertEquals("Database error", e.getMessage());
        }

        // Verify that saveUser() was attempted
        verify(userDao).saveUser(any(User.class));
    }
}
