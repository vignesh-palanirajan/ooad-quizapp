package database;

import java.sql.Connection;
import java.sql.SQLException;

public class TestDatabase {
    public static void main(String[] args) {
        try {
            // Get the singleton instance of Database
            Database db = Database.getInstance();

            // Now get the connection from the instance
            try (Connection conn = db.getConnection()) {
                System.out.println("✅ Connection successful!");
            }
        } catch (SQLException e) {
            System.out.println("❌ Connection failed: " + e.getMessage());
        }
    }
}
