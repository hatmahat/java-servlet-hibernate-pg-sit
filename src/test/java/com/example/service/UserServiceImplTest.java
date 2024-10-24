package com.example.service;

import com.example.constants.AppConstants;
import com.example.exception.UserNotFoundException;
import com.example.model.User;
import com.example.repository.UserRepo;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepo userRepo;

    @Mock
    private CloseableHttpClient mockHttpClient;

    @Mock
    private CloseableHttpResponse mockResponse;

    private String externalApiUrl = "http://localhost:8089/todos/1";

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this); // Initializes and Injects Mocks
        userService = new UserServiceImpl(mockHttpClient, userRepo, externalApiUrl);
    }

    @Test 
    public void getAllUsers_SuccessCase_ReturnUsersList() {
        List<User> mockUsers = new ArrayList<>();
        User user1 = new User();
        user1.setName("John Doe");
        user1.setEmail("john@example.com");

        User user2 = new User();
        user2.setName("Jane Doe");
        user2.setEmail("jane@example.com");

        mockUsers.add(user1);
        mockUsers.add(user2);

        // Mocking the behavior of userRepo
        when(userRepo.listUsers()).thenReturn(mockUsers);

        // Test the method
        List<User> result = userService.getAllUsers();
        assertEquals(2, result.size());
        assertEquals("John Doe", result.get(0).getName());
        assertEquals("Jane Doe", result.get(1).getName());
    }

    @Test
    public void getAllUsers_SuccessCase_ReturnEmptyList() {
        List<User> mockUsers = new ArrayList<>();

        // Mocking the behavior of userRepo
        when(userRepo.listUsers()).thenReturn(mockUsers);

        // Test the method
        List<User> result = userService.getAllUsers();
        assertEquals(0, result.size());
    }

    @Test
    public void addUser_SuccessCase_ValidInputs() { 
        String name = "John Doe";
        String email = "john@example.com";

        // Simulate successful saving of a user
        doNothing().when(userRepo).saveUser(any(User.class));

        // Test the method
        userService.addUser(name, email, AppConstants.NO_MEMBERSHIP);

        // Verify that userRepo.saveUser() was called
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepo).saveUser(userCaptor.capture());
        User capturedUser = userCaptor.getValue();

        assertEquals("John Doe", capturedUser.getName());
        assertEquals("john@example.com", capturedUser.getEmail());
        assertEquals("NONE", capturedUser.getMembershipStatus());
    }

    @Test
    public void addUser_FailedCase_DatabaseError() {
        String name = "John Doe";
        String email = "john@example.com";

        // Simulate exception during saveUser
        doThrow(new RuntimeException("Database error")).when(userRepo).saveUser(any(User.class));

        try {
            // Test the method
            userService.addUser(name, email, AppConstants.NO_MEMBERSHIP);
        } catch (RuntimeException e) {
            assertEquals("Database error", e.getMessage());
        }

        // Verify that saveUser() was attempted
        verify(userRepo).saveUser(any(User.class));
    }

    @Test
    public void calculateDiscount_SuccessCase_ReturnVIPDiscount() {
        Long userId = 1L;
        double purchaseAmount = 150.0;
        
        User user = new User();
        user.setId(userId);
        user.setName("Alice");
        user.setEmail("alice@gmail.com");
        user.setMembershipStatus(AppConstants.VIP_MEMBERSHIP);
 
        // Mock the repo to return the user
        when(userRepo.getUserById(userId)).thenReturn(user);
 
        // Call the service method
        double discount = userService.calculationDiscount(userId, purchaseAmount);
 
        // Assert the discount is 20%
        assertEquals(AppConstants.VIP_DISCOUNT_OVER_100, discount);
    }

    @Test
    public void calculateDiscount_SuccessCase_ReturnVIPDiscountUnder100() {
        Long userId = 1L;
        double purchaseAmount = 99.0;
        
        User user = new User();
        user.setId(userId);
        user.setName("Alice");
        user.setEmail("alice@gmail.com");
        user.setMembershipStatus(AppConstants.VIP_MEMBERSHIP);
 
        // Mock the repo to return the user
        when(userRepo.getUserById(userId)).thenReturn(user);
 
        // Call the service method
        double discount = userService.calculationDiscount(userId, purchaseAmount);
 
        // Assert the discount is 20%
        assertEquals(AppConstants.VIP_DISCOUNT_UNDER_100, discount);
    }

    @Test
    public void calculateDiscount_FailedCase_UserNotFound() {
        Long userId = 99L;
        double purchaseAmount = 150.0;

        // Mock the repo to return null (user not found)
        when(userRepo.getUserById(userId)).thenReturn(null);

        // Call the service method and expect UserNotFoundException
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userService.calculationDiscount(userId, purchaseAmount);
        });

        // Verify the exception message
        assertEquals("User with ID 99 not found", exception.getMessage());
    }

    @Test
    public void fetchTodo_SuccessCase_ReturnObject() throws Exception { 
        // Arrange
        String jsonResponse = "{ \"userId\": 1, \"id\": 1, \"title\": \"delectus aut autem\", \"completed\": false }";

        // Mock the response entity
        StringEntity entity = new StringEntity(jsonResponse);
        when(mockHttpClient.execute(any(HttpGet.class))).thenReturn(mockResponse);
        when(mockResponse.getEntity()).thenReturn(entity);

        // Act
        Map<String, Object> result = userService.fetchTodo();

        // Assert
        assertEquals(1, result.get("userId"));
        assertEquals(1, result.get("id"));
        assertEquals("delectus aut autem", result.get("title"));
        assertEquals(false, result.get("completed"));
    }

    @Test
    public void fetchTodo_FailedCase_FailedToFetch() throws Exception {
        // Arrange
        when(mockHttpClient.execute(any(HttpGet.class))).thenThrow(new RuntimeException("API Error"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.fetchTodo();
        });

        // Assert the exception message
        assertEquals("Error fetching todo data from external API: API Error", exception.getMessage());
    }
}
