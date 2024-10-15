package com.example.service;

import com.example.model.User;
import com.example.repository.UserRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.constants.AppConstants;
import com.example.exception.UserNotFoundException;

import java.util.Arrays;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserRepo userRepo; 

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
}
