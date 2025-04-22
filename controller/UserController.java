package controller;

import dao.UserDAO;
import models.User;

public class UserController {

    // Handle user login — returns full User object or null if login fails
    public User login(String username, String password) {
        if (isValidInput(username, password)) {
            return UserDAO.authenticateUser(username, password);
        }
        return null;
    }

    // Handle user registration — accepts role and sets it on the User object
    public boolean register(String username, String password, String email, String role) {
        if (isValidInput(username, password) && isValidRole(role)) {
            User user = new User();
            user.setUsername(username);
            user.setPassword(password); // ⚠️ Hash this in production
            user.setEmail(email);
            user.setRole(role); // Set the role passed from the form
            return UserDAO.registerUser(user);
        }
        return false;
    }

    // Validate input (basic check)
    public boolean isValidInput(String username, String password) {
        return username != null && !username.trim().isEmpty()
                && password != null && !password.trim().isEmpty();
    }

    // Validate role — only "Admin" and "Participant" are allowed
    private boolean isValidRole(String role) {
        return "Admin".equals(role) || "Participant".equals(role);
    }
}
