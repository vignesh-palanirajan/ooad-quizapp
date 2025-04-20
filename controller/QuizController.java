package controller;

import dao.QuestionDAO;
import models.Question;

import java.util.ArrayList;
import java.util.List;

public class QuizController {

    private List<Question> questions;
    private int currentQuestionIndex;
    private int score;
    private final int userId;
    private final List<UserAnswer> incorrectAnswers;

    public QuizController(int userId) {
        this.userId = userId;
        this.questions = new ArrayList<>();
        this.currentQuestionIndex = 0;
        this.score = 0;
        this.incorrectAnswers = new ArrayList<>();
    }

    // Start quiz and load random questions
    public void startQuiz(int numberOfQuestions) {
        this.questions = QuestionDAO.getRandomQuestions(numberOfQuestions);
        this.currentQuestionIndex = 0;
        this.score = 0;
        this.incorrectAnswers.clear();
    }

    // Get current question
    public Question getCurrentQuestion() {
        if (currentQuestionIndex < questions.size()) {
            return questions.get(currentQuestionIndex);
        }
        return null;
    }

    // Submit the selected answer
    public void submitAnswer(String selectedOption) {
        Question current = getCurrentQuestion();
        if (current != null) {
            if (current.getCorrectOption().equalsIgnoreCase(selectedOption)) {
                score++;
            } else {
                incorrectAnswers.add(new UserAnswer(current, selectedOption));
            }
            currentQuestionIndex++;
        }
    }

    // Check if quiz is finished
    public boolean isQuizOver() {
        return currentQuestionIndex >= questions.size();
    }

    public int getScore() {
        return score;
    }

    public int getTotalQuestions() {
        return questions.size();
    }

    public int getUserId() {
        return userId;
    }

    public List<UserAnswer> getIncorrectAnswers() {
        return incorrectAnswers;
    }

    // Inner class to represent incorrect answers
    public static class UserAnswer {
        private final Question question;
        private final String markedAnswer;

        public UserAnswer(Question question, String markedAnswer) {
            this.question = question;
            this.markedAnswer = markedAnswer;
        }

        public Question getQuestion() {
            return question;
        }

        public String getMarkedAnswer() {
            return markedAnswer;
        }

        public String getCorrectAnswer() {
            return question.getCorrectOption();
        }
    }
}