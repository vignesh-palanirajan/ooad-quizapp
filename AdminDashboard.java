import dao.UserDAO;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.sql.*;

public class AdminDashboard extends JFrame {
    private JTable userTable;
    private JButton viewScoresButton, addQuestionButton;
    private JScrollPane tableScrollPane;
    private BufferedImage backgroundImage;

    public AdminDashboard() {
        setTitle("QuizWise - Admin Dashboard");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

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

        // Anti-aliasing for text
        UIManager.put("Button.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("Label.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("TextField.font", new Font("Segoe UI", Font.PLAIN, 14));

        // Top Bar
        JPanel topBar = new JPanel();
        topBar.setBackground(new Color(0, 0, 0));
        topBar.setLayout(new BoxLayout(topBar, BoxLayout.X_AXIS));
        topBar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel titleLabel = new JLabel("🛠️ Admin Dashboard");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        topBar.add(titleLabel);
        topBar.add(Box.createHorizontalGlue());

        // Profile Icon
        JLabel profileLabel = new JLabel("👤");
        profileLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        profileLabel.setForeground(Color.WHITE);
        profileLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        profileLabel.setToolTipText("Profile");

        // Dropdown menu
        JPopupMenu profileMenu = new JPopupMenu();
        profileMenu.setBackground(Color.WHITE);

        JMenuItem logoutItem = createHoverableMenuItem("🚪 Log Out");

        logoutItem.addActionListener(ev -> {
            dispose();
            new LoginForm();
        });

        profileMenu.add(logoutItem);

        profileLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent e) {
                profileMenu.show(profileLabel, e.getX(), e.getY() + profileLabel.getHeight());
            }
        });

        topBar.add(profileLabel);
        backgroundPanel.add(topBar, BorderLayout.NORTH);

        // Main content panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setOpaque(false);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        backgroundPanel.add(mainPanel, BorderLayout.CENTER);

        // Center overlay panel
        JPanel overlayPanel = new JPanel(new BorderLayout());
        overlayPanel.setBackground(new Color(173, 216, 230, 200));
        overlayPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.add(overlayPanel, BorderLayout.CENTER);

        // Header in overlay
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setOpaque(false);
        JLabel welcomeLabel = new JLabel("Admin Control Panel", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Serif", Font.BOLD, 36));
        welcomeLabel.setForeground(new Color(40, 90, 160));
        headerPanel.add(welcomeLabel);
        overlayPanel.add(headerPanel, BorderLayout.NORTH);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setOpaque(false);

        viewScoresButton = new JButton("📊 View Highest Scores");
        addQuestionButton = new JButton("➕ Add Questions");

        styleButton(viewScoresButton, new Color(41, 128, 185));
        styleButton(addQuestionButton, new Color(39, 174, 96));

        buttonPanel.add(viewScoresButton);
        buttonPanel.add(addQuestionButton);

        overlayPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Table panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setOpaque(false);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Table setup
        String[] columns = {"User ID", "Username", "Role", "Highest Score", "Actions"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        userTable = new JTable(model);
        userTable.setRowHeight(28);
        userTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        userTable.getTableHeader().setBackground(new Color(44, 62, 80));
        userTable.getTableHeader().setForeground(Color.WHITE);
        userTable.setGridColor(new Color(200, 200, 200));
        userTable.setShowGrid(true);

        userTable.getColumn("Actions").setCellRenderer(new ButtonRenderer());
        userTable.getColumn("Actions").setCellEditor(new ButtonEditor(new JCheckBox(), this));

        tableScrollPane = new JScrollPane(userTable);
        tableScrollPane.setOpaque(false);
        tableScrollPane.getViewport().setOpaque(false);
        tablePanel.add(tableScrollPane, BorderLayout.CENTER);

        overlayPanel.add(tablePanel, BorderLayout.CENTER);
        tableScrollPane.setVisible(false);

        // Button Actions
        viewScoresButton.addActionListener(e -> {
            loadUserData();
            tableScrollPane.setVisible(true);
            revalidate();
            repaint();
        });

        addQuestionButton.addActionListener(e -> openAddQuestionDialog());

        setVisible(true);
    }

    private JMenuItem createHoverableMenuItem(String text) {
        JMenuItem item = new JMenuItem(text);
        item.setBackground(Color.WHITE);
        item.setOpaque(true);
        item.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        item.setFont(new Font("SansSerif", Font.PLAIN, 14));

        item.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                item.setBackground(new Color(220, 220, 250));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                item.setBackground(Color.WHITE);
            }
        });

        return item;
    }

    private void styleButton(JButton button, Color bgColor) {
        button.setFont(new Font("SansSerif", Font.BOLD, 18));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setPreferredSize(new Dimension(250, 50));
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

    private void loadUserData() {
        DefaultTableModel model = (DefaultTableModel) userTable.getModel();
        model.setRowCount(0);

        try (ResultSet rs = UserDAO.getHighestScoresOfParticipants()) {
            while (rs.next()) {
                int userId = rs.getInt("id");
                String username = rs.getString("username");
                String role = rs.getString("role");
                int highestScore = rs.getInt("highest_score");

                model.addRow(new Object[]{userId, username, role, highestScore, "Delete"});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading user data: " + e.getMessage());
        }
    }

    public void deleteUser(int userId) {
        int confirm = JOptionPane.showConfirmDialog(this, "Delete this user?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (UserDAO.deleteUser(userId)) {
                JOptionPane.showMessageDialog(this, "User deleted.");
                loadUserData();
            } else {
                JOptionPane.showMessageDialog(this, "Deletion failed.");
            }
        }
    }

    private void openAddQuestionDialog() {
        JDialog dialog = new JDialog(this, "➕ Add New Question", true);
        dialog.setSize(520, 420);
        dialog.setLocationRelativeTo(this);

        JPanel dialogPanel = new JPanel(new BorderLayout());
        dialogPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        dialogPanel.setBackground(new Color(255, 255, 255));

        JLabel titleLabel = new JLabel("Add New Question", JLabel.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        titleLabel.setForeground(new Color(40, 90, 160));
        dialogPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 15));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JTextField questionField = new JTextField();
        JTextField optionA = new JTextField();
        JTextField optionB = new JTextField();
        JTextField optionC = new JTextField();
        JTextField optionD = new JTextField();
        JTextField correctOption = new JTextField();

        formPanel.add(new JLabel("Question:"));
        formPanel.add(questionField);
        formPanel.add(new JLabel("Option A:"));
        formPanel.add(optionA);
        formPanel.add(new JLabel("Option B:"));
        formPanel.add(optionB);
        formPanel.add(new JLabel("Option C:"));
        formPanel.add(optionC);
        formPanel.add(new JLabel("Option D:"));
        formPanel.add(optionD);
        formPanel.add(new JLabel("Correct Option (A/B/C/D):"));
        formPanel.add(correctOption);

        dialogPanel.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBackground(Color.WHITE);

        JButton submitButton = new JButton("✅ Add");
        JButton cancelButton = new JButton("❌ Cancel");

        submitButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        submitButton.setBackground(new Color(39, 174, 96));
        submitButton.setForeground(Color.WHITE);
        submitButton.setFocusPainted(false);
        submitButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        submitButton.setPreferredSize(new Dimension(120, 40));

        cancelButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        cancelButton.setBackground(new Color(149, 165, 166));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFocusPainted(false);
        cancelButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        cancelButton.setPreferredSize(new Dimension(120, 40));

        submitButton.addActionListener(e -> {
            String q = questionField.getText().trim();
            String a = optionA.getText().trim();
            String b = optionB.getText().trim();
            String c = optionC.getText().trim();
            String d = optionD.getText().trim();
            String correct = correctOption.getText().trim().toUpperCase();

            if (q.isEmpty() || a.isEmpty() || b.isEmpty() || c.isEmpty() || d.isEmpty() || !correct.matches("[ABCD]")) {
                JOptionPane.showMessageDialog(dialog, "Please enter valid data.");
                return;
            }

            if (UserDAO.insertQuestion(q, a, b, c, d, correct)) {
                JOptionPane.showMessageDialog(dialog, "Question added successfully.");
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Failed to add question.");
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);
        dialogPanel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.add(dialogPanel);
        dialog.setVisible(true);
    }

    public static void main(String[] args) {
        new AdminDashboard();
    }
}