import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.sql.*;
import database.Database;

public class ScoreboardScreen extends JFrame {
    private BufferedImage backgroundImage;

    public ScoreboardScreen(int userId, String scoringType) {
        setTitle("QuizWise - " + scoringType.substring(0, 1).toUpperCase() + scoringType.substring(1) + " Scoreboard");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Load background image
        try {
            backgroundImage = ImageIO.read(new File("bg4.jpg"));
        } catch (IOException e) {
            System.out.println("Background image not found.");
        }

        // Background panel
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        setContentPane(backgroundPanel);
        backgroundPanel.setLayout(new BorderLayout());

        // Top Bar
        JPanel topBar = new JPanel();
        topBar.setBackground(new Color(0, 0, 0));
        topBar.setLayout(new BoxLayout(topBar, BoxLayout.X_AXIS));
        topBar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        String scoringTitle = scoringType.substring(0, 1).toUpperCase() + scoringType.substring(1);
        //JLabel titleLabel = new JLabel("🏆 " + scoringTitle + " Difficulty Leaderboard");
        JLabel titleLabel = new JLabel("");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        topBar.add(titleLabel);
        topBar.add(Box.createHorizontalGlue());

        backgroundPanel.add(topBar, BorderLayout.NORTH);

        // Main content panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setOpaque(false);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        backgroundPanel.add(mainPanel, BorderLayout.CENTER);

        // Center overlay panel
        JPanel overlayPanel = new JPanel(new BorderLayout(0, 20));
        overlayPanel.setBackground(new Color(255, 255, 255, 230));
        overlayPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));
        mainPanel.add(overlayPanel, BorderLayout.CENTER);

        // Header
        JLabel header = new JLabel("🏆 "+"Top Performers in " + scoringTitle, JLabel.CENTER);
        header.setFont(new Font("Serif", Font.BOLD, 36));
        header.setForeground(new Color(40, 90, 160));
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        overlayPanel.add(header, BorderLayout.NORTH);

        // Table model with custom renderer
        String[] columns = {"Rank", "Username", "Score", "Total Questions", "Percentage", "Date"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };

        JTable table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        table.setRowHeight(32);
        table.setShowGrid(true);
        table.setGridColor(new Color(230, 230, 230));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));
        table.getTableHeader().setBackground(new Color(44, 62, 80));
        table.getTableHeader().setForeground(Color.WHITE);

        // Center-align all columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        overlayPanel.add(scrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);

        JButton backButton = new JButton("↩️ Back");
        styleButton(backButton, new Color(220, 53, 69));

        backButton.addActionListener(e -> {
            dispose();
            new ScoreScreen(userId, scoringType);
        });

        buttonPanel.add(backButton);
        overlayPanel.add(buttonPanel, BorderLayout.SOUTH);

        // DB connection and query
        try (Connection conn = Database.getInstance().getConnection()) {
            String query = "SELECT u.username, s.score, s.total_questions, s.scoring_type, s.timestamp " +
                    "FROM scores s JOIN users u ON s.user_id = u.id " +
                    "WHERE s.scoring_type = ? " +
                    "ORDER BY s.score DESC";

            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, scoringType);

            ResultSet rs = stmt.executeQuery();

            int rank = 1;
            while (rs.next()) {
                String username = rs.getString("username");
                int score = rs.getInt("score");
                int total = rs.getInt("total_questions");
                double percentage = (score * 100.0) / total;
                Timestamp date = rs.getTimestamp("timestamp");

                tableModel.addRow(new Object[]{
                        rank++,
                        username,
                        score,
                        total,
                        String.format("%.1f%%", percentage),
                        date.toString().substring(0, 16) // Format date to show only date and time
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading scoreboard: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }

        setVisible(true);
    }

    private void styleButton(JButton button, Color bgColor) {
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(180, 45));
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
    }

    public static void main(String[] args) {
        // Example: display scoreboard for "easy" scoring type
        new ScoreboardScreen(1, "easy");
    }
}