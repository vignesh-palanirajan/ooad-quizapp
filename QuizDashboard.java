//QuizDashboard.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;

public class QuizDashboard extends JFrame {
    private int userId;
    private BufferedImage backgroundImage;

    public QuizDashboard(int userId) {
        this.userId = userId;
        setTitle("QuizNest - Dashboard");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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

        JMenuItem customizeItem = createHoverableMenuItem("🛠 Customize Profile");
        JMenuItem logoutItem = createHoverableMenuItem("🚪 Log Out");

        customizeItem.addActionListener(ev -> {
            dispose();
            new EditProfileScreen(userId);
        });

        logoutItem.addActionListener(ev -> {
            dispose();
            new LoginForm();
        });

        profileMenu.add(customizeItem);
        profileMenu.add(logoutItem);

        profileLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent e) {
                profileMenu.show(profileLabel, e.getX(), e.getY() + profileLabel.getHeight());
            }
        });

        topBar.add(profileLabel);
        add(topBar, BorderLayout.NORTH);

        // Center layout
        backgroundPanel.setLayout(new GridBagLayout());
        JPanel overlayPanel = new JPanel(new GridBagLayout());
        overlayPanel.setBackground(new Color(255, 255, 255, 230));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel welcomeLabel = new JLabel("Welcome to QuizNest", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Serif", Font.BOLD, 36));
        welcomeLabel.setForeground(new Color(40, 90, 160));

        JLabel subLabel = new JLabel("Select \" Start Quiz \" to take the Quiz ", JLabel.CENTER);
        subLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
        subLabel.setForeground(Color.DARK_GRAY);

        JButton startQuizButton = new JButton("📝 Start Quiz");
        styleButton(startQuizButton, new Color(60, 179, 113));
        startQuizButton.addActionListener(e -> {
            dispose();
            new ScoringOptionsScreen(userId);
        });

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        overlayPanel.add(welcomeLabel, gbc);
        gbc.gridy++;
        overlayPanel.add(subLabel, gbc);
        gbc.gridy++;
        overlayPanel.add(startQuizButton, gbc);

        backgroundPanel.add(overlayPanel);
        add(backgroundPanel);
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
        button.setPreferredSize(new Dimension(250, 50));
        // Add hover effect
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
        new QuizDashboard(1);
    }
}
