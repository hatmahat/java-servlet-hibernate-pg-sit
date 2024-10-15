package com.example.controller;

import com.example.model.User;
import com.example.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class UserController {
    
    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public List<User> listUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/addUser")
    public String saveUser(@RequestParam String name, @RequestParam String email, @RequestParam String type) {
        if (name == "" || email == "") {
            return "Invalid name or email";
        }

        userService.addUser(name, email, type);
        return "User added successfully!";
    }

    @GetMapping("/discount")
    public String calculateDiscount(@RequestParam Long id, @RequestParam double purchaseAmount) {
        Double discount = userService.calculationDiscount(id, purchaseAmount);

        if (discount == null) {
            return "User not found";
        }

        double discountPercentage = discount * 100;
        return "User gets a discount of " + discountPercentage + "% on a purchase of $" + purchaseAmount;
    }
    
    
}
