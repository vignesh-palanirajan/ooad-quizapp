package strategy;

import models.Question;

public class SimpleScoringStrategy implements ScoringStrategy {
    @Override
    public int calculateScore(Question question, String selectedOption, long timeTaken) {
        return selectedOption.equals(question.getCorrectOption()) ? 1 : 0;
    }
}
