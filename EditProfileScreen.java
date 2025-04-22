import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;
import dao.UserDAO;
import models.User;

public class EditProfileScreen extends JFrame {
    private BufferedImage backgroundImage;

    public EditProfileScreen(int userId) {
        setTitle("QuizWise - Edit Profile");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Load background image
        try {
            backgroundImage = ImageIO.read(new File("bg4.jpg"));
        } catch (IOException e) {
            System.out.println("Background image not found.");
        }

        User user = UserDAO.getUserById(userId);
        if (user == null) {
            JOptionPane.showMessageDialog(this, "User not found!");
            dispose();
            new LoginForm();
            return;
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

        JLabel titleLabel = new JLabel("");
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

        JMenuItem dashboardItem = createHoverableMenuItem("🏠 Dashboard");
        JMenuItem logoutItem = createHoverableMenuItem("🚪 Log Out");

        dashboardItem.addActionListener(ev -> {
            dispose();
            new QuizDashboard(userId);
        });

        logoutItem.addActionListener(ev -> {
            dispose();
            new LoginForm();
        });

        profileMenu.add(dashboardItem);
        profileMenu.add(logoutItem);

        profileLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent e) {
                profileMenu.show(profileLabel, e.getX(), e.getY() + profileLabel.getHeight());
            }
        });

        topBar.add(profileLabel);
        backgroundPanel.add(topBar, BorderLayout.NORTH);

        // Main Panel with overlay
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        GridBagConstraints centerGbc = new GridBagConstraints();
        backgroundPanel.add(centerPanel, BorderLayout.CENTER);

        // Overlay Panel
        JPanel overlayPanel = new JPanel(new BorderLayout(0, 20));
        overlayPanel.setBackground(new Color(173, 216, 230, 200));
        overlayPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        // Center the overlay panel
        centerPanel.add(overlayPanel, centerGbc);

        // Header
        JLabel header = new JLabel("Edit Your Profile", JLabel.CENTER);
        header.setFont(new Font("Serif", Font.BOLD, 36));
        header.setForeground(new Color(40, 90, 160));
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        overlayPanel.add(header, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 10, 15, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        JTextField usernameField = new JTextField(user.getUsername());
        JPasswordField passwordField = new JPasswordField(user.getPassword());
        JTextField emailField = new JTextField(user.getEmail());

        Dimension textFieldSize = new Dimension(300, 40);
        usernameField.setPreferredSize(textFieldSize);
        passwordField.setPreferredSize(textFieldSize);
        emailField.setPreferredSize(textFieldSize);

        // Style the text fields
        Font fieldFont = new Font("SansSerif", Font.PLAIN, 16);
        usernameField.setFont(fieldFont);
        passwordField.setFont(fieldFont);
        emailField.setFont(fieldFont);

        JLabel userLabel = new JLabel("👤 Username:");
        JLabel passLabel = new JLabel("🔒 Password:");
        JLabel emailLabel = new JLabel("📧 Email:");

        Font labelFont = new Font("SansSerif", Font.BOLD, 16);
        userLabel.setFont(labelFont);
        passLabel.setFont(labelFont);
        emailLabel.setFont(labelFont);

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(userLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(passLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(emailLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(emailField, gbc);

        overlayPanel.add(formPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);

        JButton saveButton = new JButton("💾 Save");
        JButton cancelButton = new JButton("↩️ Back");
        JButton viewScoresButton = new JButton("📊 View Scores");

        styleButton(saveButton, new Color(0, 123, 255));
        styleButton(cancelButton, new Color(220, 53, 69));
        styleButton(viewScoresButton, new Color(40, 167, 69));

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(viewScoresButton);

        overlayPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Button Actions
        saveButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            String email = emailField.getText().trim();

            if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields.");
                return;
            }

            user.setUsername(username);
            user.setPassword(password);
            user.setEmail(email);

            if (UserDAO.updateUser(user)) {
                JOptionPane.showMessageDialog(this, "✅ Profile updated successfully!");
                dispose();
                new QuizDashboard(userId);
            } else {
                JOptionPane.showMessageDialog(this, "❌ Update failed.");
            }
        });

        cancelButton.addActionListener(e -> {
            dispose();
            new QuizDashboard(userId);
        });

        viewScoresButton.addActionListener(e -> {
            dispose();
            new ScoreHistoryScreen(userId);
        });

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
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
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
}