package com.example.dao;

import com.example.model.User;
import java.util.List;

public interface UserDao {
    void saveUser(User user);
    List<User> listUsers();
}
