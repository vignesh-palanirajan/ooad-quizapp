package dao;

import database.Database;
import models.User;

import java.sql.*;

public class UserDAO {

    // Register a new user (Participant by default)
    public static boolean registerUser(User user) {
        String checkQuery = "SELECT id FROM users WHERE username = ?";
        String insertQuery = "INSERT INTO users (username, password, email, role) VALUES (?, ?, ?, ?)";

        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
             PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {

            // Check if the username already exists
            checkStmt.setString(1, user.getUsername());
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                System.out.println("⚠️ Username already exists!");
                return false;
            }

            // Insert new user into the database
            insertStmt.setString(1, user.getUsername());
            insertStmt.setString(2, user.getPassword());
            insertStmt.setString(3, user.getEmail());
            insertStmt.setString(4, user.getRole()); // Set the role (either Admin or Participant)
            insertStmt.executeUpdate();

            return true;
        } catch (SQLException e) {
            System.out.println("❌ Registration failed: " + e.getMessage());
            return false;
        }
    }

    // Authenticate user — returns full User object
    public static User authenticateUser(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setEmail(rs.getString("email"));
                user.setRole(rs.getString("role"));
                return user;
            }
        } catch (SQLException e) {
            System.out.println("❌ Login failed: " + e.getMessage());
        }
        return null;
    }

    // Get user details by ID
    public static User getUserById(int userId) {
        String query = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setEmail(rs.getString("email"));
                user.setRole(rs.getString("role"));
                return user;
            }
        } catch (SQLException e) {
            System.out.println("❌ Fetching user failed: " + e.getMessage());
        }
        return null;
    }

    // Update user info
    public static boolean updateUser(User user) {
        String query = "UPDATE users SET username = ?, password = ?, email = ? WHERE id = ?";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setInt(4, user.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("❌ Update failed: " + e.getMessage());
            return false;
        }
    }

    // Delete a user by ID
    public static boolean deleteUser(int userId) {
        String query = "DELETE FROM users WHERE id = ?";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("❌ Deletion failed: " + e.getMessage());
            return false;
        }
    }

    // Get highest score of each participant
    public static ResultSet getHighestScoresOfParticipants() {
        String query = """
        SELECT u.id, u.username, u.role, MAX(s.score) AS highest_score
        FROM users u
        JOIN scores s ON u.id = s.user_id
        WHERE u.role = 'Participant'
        GROUP BY u.id, u.username, u.role
    """;

        try {
            Connection conn = Database.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            return stmt.executeQuery();
        } catch (SQLException e) {
            System.out.println("❌ Failed to fetch scores: " + e.getMessage());
            return null;
        }
    }



    // Get all participants
    public static ResultSet getAllParticipants() {
        String query = "SELECT * FROM users WHERE role = 'Participant'";

        try {
            Connection conn = Database.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            return stmt.executeQuery();
        } catch (SQLException e) {
            System.out.println("❌ Failed to fetch participants: " + e.getMessage());
            return null;
        }
    }

    public static boolean insertQuestion(String question, String a, String b, String c, String d, String correct) {
        String sql = "INSERT INTO questions (question, option_a, option_b, option_c, option_d, correct_option) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, question);
            stmt.setString(2, a);
            stmt.setString(3, b);
            stmt.setString(4, c);
            stmt.setString(5, d);
            stmt.setString(6, correct);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("❌ Failed to insert question: " + e.getMessage());
            return false;
        }
    }

}
