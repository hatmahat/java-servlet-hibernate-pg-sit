package com.example.controller;

import com.example.dao.UserDao;
import com.example.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class UserController {
    
    @Autowired
    private UserDao userDao;

    @GetMapping("/users")
    public List<User> listUsers() {
        return userDao.listUsers();
    }

    @PostMapping("/addUser")
    public String saveUser(@RequestParam String name, @RequestParam String email) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        userDao.saveUser(user);
        return "User added successfully!";
    }
    
}
