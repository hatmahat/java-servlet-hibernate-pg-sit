package com.example.service;

import com.example.model.User;
import com.example.repository.UserRepo;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.constants.AppConstants;
import com.example.exception.UserNotFoundException;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    private final CloseableHttpClient httpClient; 
    private final UserRepo userRepo;
    private final String externalApiUrl;               

    public UserServiceImpl(CloseableHttpClient httpClient, UserRepo userRepo, @Value("${external.api.url}") String externalApiUrl) {
        this.httpClient = httpClient;
        this.userRepo = userRepo;
        this.externalApiUrl = externalApiUrl;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepo.listUsers();
    }

    @Override
    public void addUser(String name, String email, String type) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);

        if (Arrays.asList(AppConstants.MEMBERSHIP_TYPES).contains(type)) {
            user.setMembershipStatus(type);
        } else {
            user.setMembershipStatus(AppConstants.NO_MEMBERSHIP);
        }

        userRepo.saveUser(user);
    }

    @Override
    public Double calculationDiscount(Long id, double purchaseAmount) {
        User user = userRepo.getUserById(id);
        if (user == null) {
            throw new UserNotFoundException("User with ID " + id + " not found");
        }

        if (user.getMembershipStatus().equals(AppConstants.VIP_MEMBERSHIP)) {
            if (purchaseAmount > 100) {
                return AppConstants.VIP_DISCOUNT_OVER_100; // 20% discount
            } else {
                return AppConstants.VIP_DISCOUNT_UNDER_100; // 10% discount
            }
        } else if (user.getMembershipStatus().equals(AppConstants.REGULAR_MEMBERSHIP)) {
            if (purchaseAmount > 100) {
                return AppConstants.REGULAR_DISCOUNT_OVER_100; // 10% discount 
            } else {
                return AppConstants.NO_DISCOUNT; // No discount
            }
        } else {
            return AppConstants.NO_DISCOUNT; // Non-members get no discount
        }
    }

    @Override
    public Map<String, Object> fetchTodo() {
        try {
            HttpGet request = new HttpGet(externalApiUrl);
            CloseableHttpResponse response = httpClient.execute(request);  // Use injected httpClient

            if (response.getEntity() == null) {
                throw new RuntimeException("API Error: Response entity is null");
            }

            String JSONString = EntityUtils.toString(response.getEntity(), "UTF-8");

            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(JSONString, Map.class);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching todo data from external API: " + e.getMessage());
        }
    }
}
