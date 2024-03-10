package com.example.demo.service;

import com.example.demo.model.User; //User model class
import com.example.demo.repository.UserRepository; //UserRepository interface
import org.springframework.stereotype.Service; //Service

import java.util.List; // Importing the List interface

@Service //Spring service component
public class UserService 
{

    private final UserRepository userRepository; // declaring UserRepository

    // inject the UserRepository 
    public UserService(UserRepository userRepository) 
    {
        this.userRepository = userRepository; // Init the userRepository field
    }

    // retrieve all users from the database
    public List<User> getAllUsers() 
    {
        return userRepository.findAll(); // Calling the findAll() method from the UserRepository
    }
}
