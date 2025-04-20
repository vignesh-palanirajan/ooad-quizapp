import javax.swing.*;
import java.awt.*;

public class ScoringOptionsScreen extends JFrame {

    public ScoringOptionsScreen(int userId) {
        setTitle("Choose Scoring Method");
        setExtendedState(JFrame.MAXIMIZED_BOTH);  // Fullscreen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Background panel
        BackgroundPanel backgroundPanel = new BackgroundPanel("bg4.jpg");
        backgroundPanel.setLayout(new GridBagLayout());

        // Center panel
        JPanel optionBox = new RoundedPanel(30, new Color(240, 248, 255, 230)); // Light Blue with transparency
        optionBox.setLayout(new BoxLayout(optionBox, BoxLayout.Y_AXIS));
        optionBox.setPreferredSize(new Dimension(500, 350));

        JLabel titleLabel = new JLabel("Select Scoring Type");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 32));
        titleLabel.setForeground(Color.DARK_GRAY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Buttons for different scoring types
        JButton simpleButton = createOptionButton("Simple Scoring (+1 / 0)", () -> {
            dispose();
            new QuizScreen(userId, "simple");
        });

        JButton negativeButton = createOptionButton("Negative Scoring (+1 / -1)", () -> {
            dispose();
            new QuizScreen(userId, "negative");
        });

        JButton timeBasedButton = createOptionButton("Time-Based Scoring", () -> {
            dispose();
            new QuizScreen(userId, "time");
        });

        JButton backButton = createOptionButton("Back to Dashboard", () -> {
            dispose();
            new QuizDashboard(userId);
        });
        backButton.setBackground(new Color(255, 87, 87));

        // Add components to the panel
        optionBox.add(Box.createVerticalStrut(30));
        optionBox.add(titleLabel);
        optionBox.add(Box.createVerticalStrut(30));
        optionBox.add(simpleButton);
        optionBox.add(Box.createVerticalStrut(15));
        optionBox.add(negativeButton);
        optionBox.add(Box.createVerticalStrut(15));
        optionBox.add(timeBasedButton);
        optionBox.add(Box.createVerticalStrut(30));
        optionBox.add(backButton);

        backgroundPanel.add(optionBox);
        setContentPane(backgroundPanel);
        setVisible(true);
    }

    private JButton createOptionButton(String text, Runnable action) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.PLAIN, 18));
        btn.setMaximumSize(new Dimension(350, 50));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setBackground(new Color(100, 149, 237));  // Blue background
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn.addActionListener(e -> action.run());
        return btn;
    }

    // Background panel class
    class BackgroundPanel extends JPanel {
        private final Image image;

        public BackgroundPanel(String path) {
            image = new ImageIcon(path).getImage();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        }
    }

    // Rounded panel for styling
    class RoundedPanel extends JPanel {
        private final int radius;
        private final Color bgColor;

        public RoundedPanel(int radius, Color color) {
            this.radius = radius;
            this.bgColor = color;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bgColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ScoringOptionsScreen(101));
    }
}

