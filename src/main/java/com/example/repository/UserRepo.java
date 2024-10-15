package com.example.repository;

import com.example.model.User;
import java.util.List;

public interface UserRepo {
    void saveUser(User user);
    List<User> listUsers();
    User getUserById(Long id);
}
