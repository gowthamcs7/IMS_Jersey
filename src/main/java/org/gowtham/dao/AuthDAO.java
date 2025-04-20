package org.gowtham.dao;


import org.gowtham.helper.DatabaseConnection;

import org.gowtham.model.User;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthDAO {

    public User getUserByEmail(String email) {
        String query = "SELECT id, name, email, password, role, createdAt FROM users WHERE email = ?";


        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();


            if (rs.next()) {
                System.out.println("🟢 [DEBUG] User Found: " + rs.getString("email"));
                return new User(
                        rs.getLong("id"),

                        rs.getString("email"),
                        rs.getString("password"), // Hashed password
                        rs.getString("role"),
                        rs.getTimestamp("createdAt")

                );
            }
            else {
                System.out.println("❌ [DEBUG] No user found for email: " + email);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean registerUser(User user) {
        String query = "INSERT INTO users (name, email, password, role, createdAt) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {


            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getRole());
            stmt.setTimestamp(5, user.getCreatedAt());

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
