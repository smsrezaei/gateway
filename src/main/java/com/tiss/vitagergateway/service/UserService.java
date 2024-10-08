package com.tiss.vitagergateway.service;


import java.util.ArrayList;
import java.util.List;

import com.tiss.vitagergateway.entity.User;
import com.tiss.vitagergateway.repository.UserRepository;
import org.springframework.stereotype.Service;


@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> allUsers() {
        List<User> users = new ArrayList<>();

        userRepository.findAll().forEach(users::add);

        return users;
    }
}