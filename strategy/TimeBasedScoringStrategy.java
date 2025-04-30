package strategy;

import models.Question;

public class TimeBasedScoringStrategy implements ScoringStrategy {
    @Override
    public int calculateScore(Question question, String selectedOption, long timeTaken) {
        if (selectedOption.equals(question.getCorrectOption()) && timeTaken <= 30000) {
            return 1;
        }
        return 0;
    }
}
