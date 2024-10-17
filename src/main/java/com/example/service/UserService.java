package com.example.service;

import com.example.model.User;
import java.util.List;
import java.util.Map;

public interface UserService {
    List<User> getAllUsers();
    void addUser(String name, String email, String type);
    Double calculationDiscount(Long id, double purchaseAmount);
    public Map<String, Object> fetchTodo();
} 