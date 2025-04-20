package org.gowtham.service;

import org.gowtham.dao.AuthDAO;
import org.gowtham.helper.PasswordUtil;
import org.gowtham.model.User;
import org.gowtham.helper.JwtUtil;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Timestamp;

public class AuthService {
    private AuthDAO authDAO;

    public AuthService() {
        this.authDAO = new AuthDAO();
    }

    public String login(String email, String password) {
        System.out.println("Checking credentials...");

        User user = authDAO.getUserByEmail(email);
        if (user == null) {
            System.out.println("User not found!");
            return null;
        }

        System.out.println("Entered Password: " + password);
        System.out.println("Stored Hash: " + user.getPassword());

        boolean passwordMatches = BCrypt.checkpw(password, user.getPassword());
        System.out.println("Password Match Result: " + passwordMatches);

        if (passwordMatches) {
            System.out.println("Password matched! Generating token...");
            try {
                String token = JwtUtil.generateToken(user);
                System.out.println("Generated Token: " + token);
                return token;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }



        } else {
            System.out.println("Password mismatch!");
            return null;
        }
    }




    public boolean register(String name, String email, String password, String role) {
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
            System.out.println("Raw Password: " + password);
            System.out.println("Generated Hash: " + hashedPassword);  // Debugging

            Timestamp createdAt = new Timestamp(System.currentTimeMillis());
            User newUser = new User(0, name, email, hashedPassword, role, createdAt);
            return authDAO.registerUser(newUser);
        }




}
