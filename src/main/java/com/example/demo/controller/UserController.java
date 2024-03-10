package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
        logger.debug("UserController initialized");
    }

    @GetMapping("/all")
    public String listUsers(Model model) {
        logger.debug("Handling request to list all users");
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "users"; // The view name should be matched with your Thymeleaf template.
    }

    @GetMapping("/search")
    public String searchUsers(@RequestParam("name") String name, Model model) {
        logger.debug("Handling search for users");
        // Assuming you adjust the search functionality based on first and/or last name.
        List<User> searchResults = userRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(name, name);
        model.addAttribute("users", searchResults);
        return "users"; // Match with your Thymeleaf template.
    }

    @GetMapping("/add")
    public String showAddUserForm(Model model) {
        logger.debug("Showing form to add a new user");
        User user = new User();
        model.addAttribute("user", user);
        return "add-user"; // The view for adding a user, adjust if necessary.
    }

    @PostMapping("/add")
    public String addUser(@ModelAttribute("user") User user) {
        logger.debug("Adding a new user: {}", user);
        userRepository.save(user);
        return "redirect:/all"; // Redirect back to the listing after adding.
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        logger.debug("Showing form to update user with id: {}", id);
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        model.addAttribute("user", user);
        return "update"; // The view for updating a user, adjust if necessary.
    }

    @PostMapping("/update")
    public String updateUser(@ModelAttribute("user") User updatedUser) {
        logger.debug("Updating user: {}", updatedUser);
        // Fetch the existing user from the database
        User existingUser = userRepository.findById(updatedUser.getEmployeeId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + updatedUser.getEmployeeId()));
        
        // Update the existing user with the new values, but keep the original created_at value
        existingUser.setFirstName(updatedUser.getFirstName());
        existingUser.setLastName(updatedUser.getLastName());
        existingUser.setEmployeeEmail(updatedUser.getEmployeeEmail());
        existingUser.setEmployeeUsername(updatedUser.getEmployeeUsername());
        
        // Save the updated existing user back to the database
        userRepository.save(existingUser);
        return "redirect:/all"; // Redirect back to the listing after updating.
    }


    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") long id) {
        logger.debug("Deleting user with id: {}", id);
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        userRepository.delete(user);
        return "redirect:/all"; // Redirect back to the listing after deleting.
    }
}
