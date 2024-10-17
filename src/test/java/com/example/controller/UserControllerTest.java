package com.example.controller;

import com.example.constants.AppConstants;
import com.example.exception.UserNotFoundException;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @Test
    public void listUsers_SuccessCase_ReturnUsersList() {
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

    @Test
    public void listUsers_FailedCase_ReturnEmptyList() {
        List<User> mockUsers = new ArrayList<>();
        when(userService.getAllUsers()).thenReturn(mockUsers);

        List<User> result = userController.listUsers();
        assertEquals(0, result.size());
    }

    @Test
    public void saveUser_SuccessCase_WithValidInput() {
        String name = "John Doe";
        String email = "john@example.com";

        doNothing().when(userService).addUser(name, email, AppConstants.NO_MEMBERSHIP);

        String response = userController.saveUser(name, email, AppConstants.NO_MEMBERSHIP);
        assertEquals("User added successfully!", response);

        verify(userService).addUser(name, email, AppConstants.NO_MEMBERSHIP);
    }

    @Test
    public void saveUser_FailedCase_InvalidNameOrEmail() { 
        String name = "";
        String email = "";

        doNothing().when(userService).addUser(name, email, AppConstants.NO_MEMBERSHIP);

        String response = userController.saveUser(name, email, AppConstants.NO_MEMBERSHIP);
        assertEquals("Invalid name or email", response);
    }

    @Test
    public void calculateDiscount_SuccessCase_ReturnDiscountString() {
        Long userId = 1L;
        double purchaseAmount = 150.0;

        // Mock the service to return a 20% discount
        when(userService.calculationDiscount(userId, purchaseAmount)).thenReturn(AppConstants.VIP_DISCOUNT_OVER_100);

        // Call the controller method
        String response = userController.calculateDiscount(userId, purchaseAmount);

        // Verify the response
        assertEquals("User gets a discount of 20.0% on a purchase of $150.0", response);
    }

    @Test
    public void calculateDiscount_FailedCase_UserNotFound() {
        Long userId = 99L;
        double purchaseAmount = 150.0;

        // Mock the service to throw UserNotFoundException
        when(userService.calculationDiscount(userId, purchaseAmount)).thenThrow(new UserNotFoundException("User with ID " + userId + " not found"));

        // Call the controller method and expect UserNotFoundException to be thrown
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userController.calculateDiscount(userId, purchaseAmount);
        });

        // Verify the response is the exception message
        assertEquals("User with ID 99 not found", exception.getMessage());

        // Verify that the service method was called
        verify(userService).calculationDiscount(userId, purchaseAmount);
    }
}
