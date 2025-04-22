import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;
import dao.UserDAO;
import controller.UserController;
import models.User;

public class LoginForm extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private BufferedImage backgroundImage;
    // Define colors for consistency
    private final Color PRIMARY_COLOR = new Color(41, 128, 185); // A nice blue
    private final Color SECONDARY_COLOR = new Color(52, 73, 94); // Dark blue-gray
    private final Color TEXT_COLOR = new Color(44, 62, 80); // Dark gray for text
    private final Color LIGHT_BG = new Color(255, 255, 255, 240); // Almost white background

    public LoginForm() {
        setTitle("QuizWise");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Full screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
            backgroundImage = ImageIO.read(new File("bg1.jpg")); // Make sure bg1.jpg is in project root
        } catch (IOException e) {
            System.out.println("Background image not found.");
            // Set a fallback gradient background if image not found
            backgroundImage = createGradientBackground(1920, 1080);
        }

        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };

        backgroundPanel.setLayout(new GridBagLayout());

        JPanel formPanel = createFormPanel();
        backgroundPanel.add(formPanel);
        add(backgroundPanel);

        // Center on screen
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel createFormPanel() {
        // Create a rounded panel with shadow effect
        JPanel formPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Paint shadow
                g2d.setColor(new Color(0, 0, 0, 50));
                g2d.fillRoundRect(5, 5, getWidth() - 10, getHeight() - 10, 20, 20);

                // Paint panel background
                g2d.setColor(LIGHT_BG);
                g2d.fillRoundRect(0, 0, getWidth() - 5, getHeight() - 5, 20, 20);
                g2d.dispose();
            }
        };

        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        formPanel.setPreferredSize(new Dimension(450, 500));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Logo/Icon (you can replace this with your actual logo)
        JLabel logoLabel = new JLabel("🎓", JLabel.CENTER);
        logoLabel.setFont(new Font("SansSerif", Font.PLAIN, 60));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        formPanel.add(logoLabel, gbc);

        // Title
        JLabel titleLabel = new JLabel("Welcome to QuizNest", JLabel.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        titleLabel.setForeground(PRIMARY_COLOR);
        gbc.gridy++; gbc.insets = new Insets(5, 10, 0, 10);
        formPanel.add(titleLabel, gbc);

        // Subtitle
        JLabel subtitle = new JLabel("Login to test your knowledge!", JLabel.CENTER);
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 16));
        subtitle.setForeground(SECONDARY_COLOR);
        gbc.gridy++; gbc.insets = new Insets(0, 10, 20, 10);
        formPanel.add(subtitle, gbc);

        // Username field with icon
        JPanel usernamePanel = createInputField("👤  Username", usernameField = new JTextField(15));
        gbc.gridy++; gbc.insets = new Insets(10, 10, 5, 10);
        formPanel.add(usernamePanel, gbc);

        // Password field with icon
        JPanel passwordPanel = createInputField("🔒  Password", passwordField = new JPasswordField(15));
        gbc.gridy++; gbc.insets = new Insets(10, 10, 15, 10);
        formPanel.add(passwordPanel, gbc);

        // Login button
        JButton loginButton = new JButton("LOGIN");
        styleButton(loginButton, PRIMARY_COLOR, Color.BLACK);
        gbc.gridy++; gbc.insets = new Insets(15, 30, 5, 30);
        formPanel.add(loginButton, gbc);

        // Sign up link
        JPanel signUpPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        signUpPanel.setOpaque(false);
        JLabel accountLabel = new JLabel("Don't have an account?");
        accountLabel.setForeground(TEXT_COLOR);
        JButton signUpButton = new JButton("Sign Up");
        signUpButton.setForeground(PRIMARY_COLOR);
        signUpButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        signUpButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        signUpButton.setContentAreaFilled(false);
        signUpButton.setBorderPainted(false);
        signUpButton.setFocusPainted(false);

        signUpPanel.add(accountLabel);
        signUpPanel.add(signUpButton);
        gbc.gridy++; gbc.insets = new Insets(5, 10, 10, 10);
        formPanel.add(signUpPanel, gbc);

        // Set default button (respond to Enter key)
        getRootPane().setDefaultButton(loginButton);

        // Add action listeners
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                showError("Please enter all fields");
                return;
            }

            UserController userController = new UserController();
            User user = userController.login(username, password);

            if (user != null) {
                // Store the logged-in user in Session
                Session.setLoggedInUser(user);

                // Check user role
                if ("Admin".equalsIgnoreCase(user.getRole())) {
                    showSuccess("Admin login successful!");
                    this.dispose();
                    new AdminDashboard(); // AdminDashboard will access the user from Session
                } else {
                    showSuccess("Participant Login successful!");
                    this.dispose();
                    new QuizDashboard(user.getId()); // Redirect to Quiz Dashboard
                }
            } else {
                showError("Invalid username or password");
            }
        });

        signUpButton.addActionListener(e -> {
            this.dispose();
            new SignUpForm();
        });

        return formPanel;
    }

    private JPanel createInputField(String labelText, JTextField field) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setOpaque(false);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.PLAIN, 14));
        label.setForeground(SECONDARY_COLOR);

        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, PRIMARY_COLOR),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        field.setOpaque(false);

        panel.add(label, BorderLayout.NORTH);
        panel.add(field, BorderLayout.CENTER);

        return panel;
    }

    private void styleButton(JButton button, Color bg, Color fg) {
        button.setFont(new Font("SansSerif", Font.BOLD, 18));
        button.setBackground(bg);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setPreferredSize(new Dimension(250, 50));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bg.darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bg);
            }
        });
    }

    private BufferedImage createGradientBackground(int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = image.createGraphics();

        // Create gradient from top to bottom
        GradientPaint gp = new GradientPaint(
                0, 0, new Color(52, 152, 219),
                0, height, new Color(41, 128, 185)
        );

        g2.setPaint(gp);
        g2.fillRect(0, 0, width, height);
        g2.dispose();

        return image;
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        try {
            // Set system look and feel for better appearance
            //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> new LoginForm());
    }
}