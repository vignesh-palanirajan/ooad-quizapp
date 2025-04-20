package dao;

import database.Database;
import models.Question;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ScoreDAO {

    public static void saveScore(int userId, int score, int totalQuestions, String scoringType) {
        String query = "INSERT INTO scores (user_id, score, total_questions, scoring_type) VALUES (?, ?, ?, ?)";

        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, score);
            stmt.setInt(3, totalQuestions);
            stmt.setString(4, scoringType);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int[] getLatestScore(int userId) {
        int[] scoreData = {0, 0}; // Default score 0/0
        String query = "SELECT score, total_questions FROM scores WHERE user_id = ? ORDER BY id DESC LIMIT 1";

        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                scoreData[0] = rs.getInt("score");
                scoreData[1] = rs.getInt("total_questions");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return scoreData;
    }
}
