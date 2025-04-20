import dao.ScoreDAO;
import models.Question;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import database.Database;

public class ScoreScreen extends JFrame {

    public ScoreScreen(int userId, String scoring_type) {
        this(userId, scoring_type, null, null, null);
    }

    public ScoreScreen(int userId, String scoring_type,
                       List<Question> incorrectQuestions,
                       List<String> selectedOptions,
                       List<Question> skippedQuestions) {
        setTitle("Quiz Score");
        setExtendedState(JFrame.MAXIMIZED_BOTH);  // Fullscreen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Background panel with image
        BackgroundPanel backgroundPanel = new BackgroundPanel("bg4.jpg");
        backgroundPanel.setLayout(new GridBagLayout()); // For centering the inner box

        // Calculate panel height based on content
        int panelHeight = 400;
        boolean hasIncorrect = incorrectQuestions != null && !incorrectQuestions.isEmpty();
        boolean hasSkipped = skippedQuestions != null && !skippedQuestions.isEmpty();

        if (hasIncorrect && hasSkipped) {
            panelHeight = 700; // Both incorrect and skipped questions
        } else if (hasIncorrect || hasSkipped) {
            panelHeight = 600; // Either incorrect or skipped questions
        }

        // Inner box panel (center box)
        JPanel boxPanel = new RoundedPanel(30, new Color(173, 216, 230, 200)); // Light blue with transparency
        boxPanel.setLayout(new BoxLayout(boxPanel, BoxLayout.Y_AXIS));
        boxPanel.setPreferredSize(new Dimension(600, panelHeight));
        boxPanel.setOpaque(false);

        // Title label
        JLabel titleLabel = new JLabel("Quiz Over", JLabel.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 40));
        titleLabel.setForeground(Color.DARK_GRAY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Score info
        int[] scoreData = ScoreDAO.getLatestScore(userId);
        int score = scoreData[0];
        int totalQuestions = scoreData[1];

        JLabel scoreLabel = new JLabel("Your Score: " + score + " / " + totalQuestions);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 28));
        scoreLabel.setForeground(Color.BLACK);
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add components to boxPanel
        boxPanel.add(Box.createVerticalStrut(30));
        boxPanel.add(titleLabel);
        boxPanel.add(Box.createVerticalStrut(30));
        boxPanel.add(scoreLabel);
        boxPanel.add(Box.createVerticalStrut(30));

        // Add incorrect questions section if available
        if (hasIncorrect) {
            JLabel incorrectLabel = new JLabel("Questions You Marked Incorrectly:");
            incorrectLabel.setFont(new Font("Arial", Font.BOLD, 22));
            incorrectLabel.setForeground(Color.BLACK);
            incorrectLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            boxPanel.add(incorrectLabel);
            boxPanel.add(Box.createVerticalStrut(15));

            // Create panel for incorrect questions
            JPanel incorrectQuestionsPanel = new JPanel();
            incorrectQuestionsPanel.setLayout(new BoxLayout(incorrectQuestionsPanel, BoxLayout.Y_AXIS));
            incorrectQuestionsPanel.setOpaque(false);

            int count = 1;
            for (int i = 0; i < incorrectQuestions.size(); i++) {
                Question q = incorrectQuestions.get(i);
                String selectedOption = selectedOptions.get(i);

                JPanel questionPanel = new JPanel();
                questionPanel.setLayout(new BoxLayout(questionPanel, BoxLayout.Y_AXIS));
                questionPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 5));
                questionPanel.setOpaque(false);

                JLabel questionText = new JLabel("<html><b>" + count + ". " + q.getQuestion() + "</b></html>");
                questionText.setFont(new Font("Arial", Font.PLAIN, 16));

                JLabel yourAnswer = new JLabel("<html>Your Answer: <span style='color:red'>" +
                        getOptionText(q, selectedOption) + "</span></html>");
                yourAnswer.setFont(new Font("Arial", Font.PLAIN, 16));

                JLabel correctAnswer = new JLabel("<html>Correct Answer: <span style='color:green'>" +
                        getOptionText(q, q.getCorrectOption()) + "</span></html>");
                correctAnswer.setFont(new Font("Arial", Font.PLAIN, 16));

                questionPanel.add(questionText);
                questionPanel.add(yourAnswer);
                questionPanel.add(correctAnswer);
                incorrectQuestionsPanel.add(questionPanel);
                incorrectQuestionsPanel.add(Box.createRigidArea(new Dimension(0, 10)));

                count++;
            }

            // Create a scroll pane for incorrect questions
            JScrollPane incorrectScrollPane = new JScrollPane(incorrectQuestionsPanel);
            incorrectScrollPane.setOpaque(false);
            incorrectScrollPane.getViewport().setOpaque(false);
            incorrectScrollPane.setBorder(BorderFactory.createEmptyBorder());
            incorrectScrollPane.setPreferredSize(new Dimension(550, 150));
            incorrectScrollPane.setMaximumSize(new Dimension(550, 150));
            incorrectScrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);

            boxPanel.add(incorrectScrollPane);
            boxPanel.add(Box.createVerticalStrut(20));
        }

        // Add skipped questions section if available
        if (hasSkipped) {
            JLabel skippedLabel = new JLabel("Questions You Skipped:");
            skippedLabel.setFont(new Font("Arial", Font.BOLD, 22));
            skippedLabel.setForeground(Color.BLACK);
            skippedLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            boxPanel.add(skippedLabel);
            boxPanel.add(Box.createVerticalStrut(15));

            // Create panel for skipped questions
            JPanel skippedQuestionsPanel = new JPanel();
            skippedQuestionsPanel.setLayout(new BoxLayout(skippedQuestionsPanel, BoxLayout.Y_AXIS));
            skippedQuestionsPanel.setOpaque(false);

            int count = 1;
            for (Question q : skippedQuestions) {
                JPanel questionPanel = new JPanel();
                questionPanel.setLayout(new BoxLayout(questionPanel, BoxLayout.Y_AXIS));
                questionPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 5));
                questionPanel.setOpaque(false);

                JLabel questionText = new JLabel("<html><b>" + count + ". " + q.getQuestion() + "</b></html>");
                questionText.setFont(new Font("Arial", Font.PLAIN, 16));

                JLabel correctAnswer = new JLabel("<html>Correct Answer: <span style='color:green'>" +
                        getOptionText(q, q.getCorrectOption()) + "</span></html>");
                correctAnswer.setFont(new Font("Arial", Font.PLAIN, 16));

                questionPanel.add(questionText);
                questionPanel.add(correctAnswer);
                skippedQuestionsPanel.add(questionPanel);
                skippedQuestionsPanel.add(Box.createRigidArea(new Dimension(0, 10)));

                count++;
            }

            // Create a scroll pane for skipped questions
            JScrollPane skippedScrollPane = new JScrollPane(skippedQuestionsPanel);
            skippedScrollPane.setOpaque(false);
            skippedScrollPane.getViewport().setOpaque(false);
            skippedScrollPane.setBorder(BorderFactory.createEmptyBorder());
            skippedScrollPane.setPreferredSize(new Dimension(550, 150));
            skippedScrollPane.setMaximumSize(new Dimension(550, 150));
            skippedScrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);

            boxPanel.add(skippedScrollPane);
            boxPanel.add(Box.createVerticalStrut(20));
        }

        // Buttons
        JButton scoreboardButton = new JButton("Scoreboard");
        styleButton(scoreboardButton, new Color(0, 123, 255));
        scoreboardButton.addActionListener(e -> {
            dispose();
            new ScoreboardScreen(userId, scoring_type);
        });

        JButton dashboardButton = new JButton("Back to Dashboard");
        styleButton(dashboardButton, new Color(0, 123, 255));
        dashboardButton.addActionListener(e -> {
            dispose();
            new QuizDashboard(userId);
        });

        JButton exitButton = new JButton("Exit");
        styleButton(exitButton, new Color(220, 53, 69));
        exitButton.addActionListener(e -> System.exit(0));

        boxPanel.add(scoreboardButton);
        boxPanel.add(Box.createVerticalStrut(15));
        boxPanel.add(dashboardButton);
        boxPanel.add(Box.createVerticalStrut(15));
        boxPanel.add(exitButton);
        boxPanel.add(Box.createVerticalStrut(20));

        // Center the boxPanel using GridBagLayout
        backgroundPanel.add(boxPanel, new GridBagConstraints());

        setContentPane(backgroundPanel);
        setVisible(true);
    }

    // Helper method to get the text of an option based on the option letter
    private String getOptionText(Question q, String optionLetter) {
        switch (optionLetter) {
            case "A": return q.getOptionA();
            case "B": return q.getOptionB();
            case "C": return q.getOptionC();
            case "D": return q.getOptionD();
            default: return "[No option selected]";
        }
    }

    private void styleButton(JButton button, Color bgColor) {
        button.setFont(new Font("Arial", Font.PLAIN, 18));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setMaximumSize(new Dimension(250, 50));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
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

    // Custom JPanel to draw background image
    class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel(String imagePath) {
            backgroundImage = new ImageIcon(imagePath).getImage();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    // Custom rounded panel with translucent color
    class RoundedPanel extends JPanel {
        private final int cornerRadius;
        private final Color backgroundColor;

        public RoundedPanel(int radius, Color bgColor) {
            this.cornerRadius = radius;
            this.backgroundColor = bgColor;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(backgroundColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
            g2.dispose();
            super.paintComponent(g);
        }
    }
}
