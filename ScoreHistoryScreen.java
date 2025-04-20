import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.Vector;
import database.Database;

public class ScoreHistoryScreen extends JFrame {
    private JTable scoreTable;
    private final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private final Color SECONDARY_COLOR = new Color(52, 73, 94);
    private final Color ACCENT_COLOR = new Color(230, 126, 34);
    private final Color BACKGROUND_COLOR = new Color(245, 245, 245);
    private final Color TABLE_HEADER_COLOR = new Color(52, 152, 219);
    private final Color TABLE_ROW_ALT = new Color(235, 245, 251);

    public ScoreHistoryScreen(int userId) {
        setTitle("Quiz Score History");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(BACKGROUND_COLOR);

        // Create a main panel with some padding
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        // Header panel with gradient
        JPanel headerPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, PRIMARY_COLOR, getWidth(), 0, SECONDARY_COLOR);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        headerPanel.setPreferredSize(new Dimension(getWidth(), 80));

        // Title with shadow effect
        JLabel title = new JLabel("Your Quiz Performance History", SwingConstants.LEFT);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        headerPanel.add(title, BorderLayout.WEST);

        // Add a visual summary label
        JLabel summaryLabel = new JLabel("View your past quiz results", SwingConstants.RIGHT);
        summaryLabel.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        summaryLabel.setForeground(new Color(255, 255, 255, 220));
        headerPanel.add(summaryLabel, BorderLayout.EAST);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Table with custom styling
        DefaultTableModel model = new DefaultTableModel(
                new String[]{"Type","Score", "Total Questions", "Percentage", "Date & Time"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };

        scoreTable = new JTable(model);
        scoreTable.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        scoreTable.setRowHeight(35);
        scoreTable.setSelectionBackground(new Color(ACCENT_COLOR.getRed(), ACCENT_COLOR.getGreen(), ACCENT_COLOR.getBlue(), 100));
        scoreTable.setSelectionForeground(Color.BLACK);
        scoreTable.setShowGrid(false);
        scoreTable.setShowHorizontalLines(true);
        scoreTable.setGridColor(new Color(230, 230, 230));
        scoreTable.setIntercellSpacing(new Dimension(0, 0));

        // Style the table header
        JTableHeader header = scoreTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 16));
        header.setBackground(TABLE_HEADER_COLOR);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 45));

        // Set column widths
        scoreTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        scoreTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        scoreTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        scoreTable.getColumnModel().getColumn(3).setPreferredWidth(150);
        scoreTable.getColumnModel().getColumn(4).setPreferredWidth(250);  // Date & Time

        // Custom renderer for alternating row colors
        scoreTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : TABLE_ROW_ALT);
                }
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                setHorizontalAlignment(column == 3 ? JLabel.LEFT : JLabel.CENTER);
                return c;
            }
        });

        loadScores(userId, model);

        JScrollPane scrollPane = new JScrollPane(scoreTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);

        // Panel to hold the table with a border
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(10, new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(0, 0, 0, 0)
        ));
        tablePanel.add(scrollPane);

        mainPanel.add(tablePanel, BorderLayout.CENTER);

        // Bottom panel with back button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        bottomPanel.setBackground(BACKGROUND_COLOR);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JButton dashboardButton = new JButton("Back to Dashboard");
        styleButton(dashboardButton, PRIMARY_COLOR);

        dashboardButton.addActionListener(e -> {
            dispose();
            new QuizDashboard(userId);
        });

        bottomPanel.add(dashboardButton);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    private void loadScores(int userId, DefaultTableModel model) {
        try (Connection conn = Database.getInstance().getConnection()) {
            String sql = "SELECT scoring_type,score,total_questions,timestamp FROM scores WHERE user_id = ? ORDER BY timestamp DESC";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                String scoringType = rs.getString("scoring_type");
                row.add(scoringType);
                int score = rs.getInt("score");
                int totalQuestions = rs.getInt("total_questions");

                row.add(score);

                row.add(totalQuestions);

                // Calculate and add percentage
                double percentage = (double) score / totalQuestions * 100;
                row.add(String.format("%.1f%%", percentage));

                // Format the timestamp to be more readable
                Timestamp timestamp = rs.getTimestamp("timestamp");
                row.add(formatTimestamp(timestamp));

                model.addRow(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error loading score history: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private String formatTimestamp(Timestamp timestamp) {
        if (timestamp == null) return "N/A";
        return String.format("%1$tB %1$td, %1$tY at %1$tI:%1$tM %1$tp", timestamp);
    }

    private void styleButton(JButton button, Color bgColor) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(8, bgColor.darker()),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setIconTextGap(10);

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


}

// Custom rounded border class
class RoundedBorder extends AbstractBorder {
    private final int radius;
    private final Color color;

    RoundedBorder(int radius, Color color) {
        this.radius = radius;
        this.color = color;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(color);
        g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        g2.dispose();
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(this.radius / 2, this.radius / 2, this.radius / 2, this.radius / 2);
    }

    @Override
    public boolean isBorderOpaque() {
        return true;
    }
}