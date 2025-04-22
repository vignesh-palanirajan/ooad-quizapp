import dao.QuestionDAO;
import dao.ScoreDAO;
import models.Question;
import strategy.*;
import factory.ScoringStrategyFactory;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class QuizScreen extends JFrame {
    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private int attempted = 0;
    private int correctAnswers = 0;
    private int wrongAnswers = 0;
    private int userId;
    private String scoringType;
    private ScoringStrategy scoringStrategy;

    private List<Question> incorrectQuestions = new ArrayList<>();
    private List<String> selectedOptions = new ArrayList<>();
    private List<Question> skippedQuestions = new ArrayList<>();

    private JLabel questionLabel;
    private JRadioButton optionA, optionB, optionC, optionD;
    private ButtonGroup optionsGroup;
    private JButton nextButton;
    private JLabel timerLabel;
    private long questionStartTime;
    private Timer timer;
    private int timeRemaining = 30;

    public QuizScreen(int userId, String scoringType) {
        this.userId = userId;
        this.scoringType = scoringType;
        questions = QuestionDAO.getRandomQuestions(10);

        scoringStrategy = ScoringStrategyFactory.getScoringStrategy(scoringType);


        setTitle("Quiz");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel background = new JLabel(new ImageIcon("bg4.jpg")) {
            @Override
            protected void paintComponent(Graphics g) {
                ImageIcon icon = new ImageIcon("bg4.jpg");
                Image image = icon.getImage();
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            }
        };
        setContentPane(background);
        background.setLayout(new GridBagLayout());

        JPanel centerBox = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(173, 216, 230, 200));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
            }

            @Override
            public boolean isOpaque() {
                return false;
            }
        };
        centerBox.setLayout(new BoxLayout(centerBox, BoxLayout.Y_AXIS));
        centerBox.setPreferredSize(new Dimension(800, 500));
        centerBox.setMaximumSize(new Dimension(800, 500));
        centerBox.setAlignmentX(Component.CENTER_ALIGNMENT);

        timerLabel = new JLabel("", SwingConstants.LEFT);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        timerLabel.setForeground(Color.BLACK);
        timerLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        if ("time".equals(scoringType)) {
            timerLabel.setText("Time Left: 30");
        }
        centerBox.add(Box.createRigidArea(new Dimension(0, 10)));
        centerBox.add(timerLabel);
        centerBox.add(Box.createRigidArea(new Dimension(0, 10)));

        questionLabel = new JLabel("", SwingConstants.CENTER);
        questionLabel.setFont(new Font("Arial", Font.BOLD, 27));
        questionLabel.setForeground(Color.BLACK);
        questionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerBox.add(questionLabel);
        centerBox.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel optionsPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        optionA = new JRadioButton();
        optionB = new JRadioButton();
        optionC = new JRadioButton();
        optionD = new JRadioButton();
        Font optionFont = new Font("Arial", Font.PLAIN, 27);
        for (JRadioButton option : new JRadioButton[]{optionA, optionB, optionC, optionD}) {
            option.setFont(optionFont);
            option.setForeground(Color.BLACK);
            option.setOpaque(false);
        }

        optionsGroup = new ButtonGroup();
        optionsGroup.add(optionA);
        optionsGroup.add(optionB);
        optionsGroup.add(optionC);
        optionsGroup.add(optionD);

        optionsPanel.setOpaque(false);
        optionsPanel.add(optionA);
        optionsPanel.add(optionB);
        optionsPanel.add(optionC);
        optionsPanel.add(optionD);
        centerBox.add(optionsPanel);
        centerBox.add(Box.createRigidArea(new Dimension(0, 20)));

        nextButton = new JButton("Next");
        nextButton.setFont(new Font("Arial", Font.BOLD, 20));
        nextButton.setBackground(new Color(70, 130, 180));
        nextButton.setForeground(Color.WHITE);
        nextButton.setFocusPainted(false);
        nextButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        nextButton.addActionListener(e -> {
            stopTimer();
            checkAnswer();
            currentQuestionIndex++;
            if (currentQuestionIndex < questions.size()) {
                displayQuestion();
            } else {
                showScoreScreen();
            }
        });
        centerBox.add(nextButton);

        background.add(centerBox);

        displayQuestion();
        setVisible(true);
    }

    private void displayQuestion() {
        if (currentQuestionIndex < questions.size()) {
            Question q = questions.get(currentQuestionIndex);
            questionLabel.setText("<html><div style='text-align: center;'>" + q.getQuestion() + "</div></html>");
            optionA.setText(q.getOptionA());
            optionB.setText(q.getOptionB());
            optionC.setText(q.getOptionC());
            optionD.setText(q.getOptionD());
            optionsGroup.clearSelection();

            if ("time".equals(scoringType)) {
                questionStartTime = System.currentTimeMillis();
                timeRemaining = 30;
                timerLabel.setText("Time Left: " + timeRemaining);
                startTimer();
            } else {
                timerLabel.setText("");
            }
        }
    }

    private void startTimer() {
        timer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                timeRemaining--;
                timerLabel.setText("Time Left: " + timeRemaining);
                if (timeRemaining <= 0) {
                    stopTimer();
                    checkAnswer();
                    currentQuestionIndex++;
                    if (currentQuestionIndex < questions.size()) {
                        displayQuestion();
                    } else {
                        showScoreScreen();
                    }
                }
            }
        });
        timer.setInitialDelay(0);
        timer.start();
    }

    private void stopTimer() {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
    }

    private void checkAnswer() {
        if (currentQuestionIndex < questions.size()) {
            Question q = questions.get(currentQuestionIndex);
            String correct = q.getCorrectOption();
            String selectedOption = "";
            boolean selected = false;

            if (optionA.isSelected()) {
                selected = true;
                selectedOption = "A";
            } else if (optionB.isSelected()) {
                selected = true;
                selectedOption = "B";
            } else if (optionC.isSelected()) {
                selected = true;
                selectedOption = "C";
            } else if (optionD.isSelected()) {
                selected = true;
                selectedOption = "D";
            }

            boolean selectedCorrect = selectedOption.equals(correct);

            if (selected) {
                attempted++;
                if (!selectedCorrect) {
                    incorrectQuestions.add(q);
                    selectedOptions.add(selectedOption);
                }
            } else {
                skippedQuestions.add(q);
            }

            long timeTaken = System.currentTimeMillis() - questionStartTime;
            int deltaScore = scoringStrategy.calculateScore(q, selectedOption, timeTaken);
            score += deltaScore;

            if ("negative".equals(scoringType)) {
                if (deltaScore == 1) correctAnswers++;
                else if (deltaScore == -1) wrongAnswers++;
            }
        }
    }

    private void showScoreScreen() {
        StringBuilder result = new StringBuilder();
        result.append("Quiz Completed!\n");
        result.append("Your Score: ").append(score).append(" / ").append(questions.size()).append("\n");
        result.append("Attempted Questions: ").append(attempted).append("\n");

        if ("negative".equals(scoringType)) {
            result.append("Correct Answers: ").append(correctAnswers).append("\n");
            result.append("Wrong Answers: ").append(wrongAnswers).append("\n");
        }

        JOptionPane.showMessageDialog(this, result.toString());
        ScoreDAO.saveScore(userId, score, 10, scoringType);
        dispose();
        new ScoreScreen(userId, scoringType, incorrectQuestions, selectedOptions, skippedQuestions);
    }
}
