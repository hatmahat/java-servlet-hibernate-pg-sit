package com.example.service;

import com.example.dao.UserDao;
import com.example.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserDao userDao; 

    @Override
    public List<User> getAllUsers() {
        return userDao.listUsers();
    }

    @Override
    public void addUser(String name, String email) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        userDao.saveUser(user);
    }
}
